package de.zib.gndms.logic.model.dspace;

import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.logic.model.ModelChangedAction;
import de.zib.gndms.logic.model.config.ConfigActionHelp;
import de.zib.gndms.logic.model.config.ConfigActionResult;
import de.zib.gndms.logic.model.config.ConfigOption;
import de.zib.gndms.logic.model.config.SetupAction;
import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.dspace.DSpace;
import de.zib.gndms.model.dspace.DSpaceRef;
import de.zib.gndms.model.dspace.MetaSubspace;
import de.zib.gndms.model.dspace.Subspace;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;


/**
 * Creates a new subspace
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 14.08.2008 Time: 17:37:51
 */
@ConfigActionHelp(shortHelp = "Setup a subspace", longHelp = "Used to prepare the database schema for GNDMS by creating, updating, and deleting subspaces")
public class SetupSubspaceAction extends SetupAction<ConfigActionResult> {
    @ConfigOption(descr="The key of the subspace (QName)")
    private ImmutableScopedName subspace;

    @ConfigOption(descr="Local filesystem root path for all slices stored in this subspace")
    private String path;

    @ConfigOption(descr="GridFTP path for all slices stored under path")
    private String gsiFtpPath;

    @ConfigOption(descr="If set, this subspace is included in the public service listing")
    private Boolean visible;

    @ConfigOption(descr="Maximum storage size available in this subspace")
    private Long size;


    @Override
    public void initialize() {
        super.initialize();    // Overridden method
        try {
            if (subspace == null && (isCreating() || hasOption("subspace")))
                setSubspace(getISNOption("subspace"));
            if (visible == null && (isCreating() || hasOption("visible")))
                setIsVisibleToPublic(isBooleanOptionSet("visible", true));
            if (size == null && (isCreating() || hasOption("size"))) {
                size = new Long( getIntOption("size") );
            }
            if (path == null && (isCreating() || hasOption("path"))) {
                setPath(getOption("path"));
            }
            if (gsiFtpPath== null && (isCreating() || hasOption("gsiFtpPath"))) {
                setGsiFtpPath(getOption("gsiFtpPath"));
            }
        }
        catch ( MandatoryOptionMissingException e) {
            throw new IllegalStateException(e);
        }

        requireParameter("subspace", subspace);
    }


    @SuppressWarnings({ "FeatureEnvy", "MethodWithMoreThanThreeNegations" })
    @Override
    public ConfigActionResult execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
        MetaSubspace meta = prepareMeta(em, subspace);
        Subspace subspace = prepareSubspace(meta);

       switch (getMode()) {
           case CREATE:
           case UPDATE:
               if (isVisibleToPublic() != null)
                   meta.setVisibleToPublic(isVisibleToPublic());

               if (size != null)
                   subspace.setTotalSize(getSize());

               if (path != null)
                   subspace.setPath(getPath());

               if (gsiFtpPath != null)
                   subspace.setGsiFtpPath(getGsiFtpPath());

               if (subspace.getDSpaceRef() == null) {
                   DSpaceRef ref = new DSpaceRef();

                   // HACK: There should be a better way than this...
                   DSpace dspace =
                           (DSpace) em.createNamedQuery("findDSpaceInstances").getSingleResult();

                   ref.setGridSiteId(null);
                   ref.setResourceKeyValue(dspace.getId());
                   subspace.setDSpaceRef(ref);
               }

               if (! em.contains(subspace))
                    em.persist(subspace);

               if (! em.contains(meta))
                    em.persist(meta);
               break;
           case DELETE:
               em.remove(meta);
               em.remove(subspace);
               break;
       }

        // Register resources that require refreshing
       getPostponedActions().addAction(new ModelChangedAction(subspace));
        
       return ok();
    }


    private MetaSubspace prepareMeta(final EntityManager em, final ImmutableScopedName pkParam) {
        MetaSubspace meta= em.find(MetaSubspace.class, pkParam);
        if (meta == null) {
            if (! isCreating())
                throw new IllegalStateException("No matching metasubspace found for update");
            meta = new MetaSubspace();
            meta.setScopedName(pkParam);
        }
        return meta;
    }


    @SuppressWarnings({ "FeatureEnvy" })
    private Subspace prepareSubspace(final MetaSubspace metaParam) {
        Subspace subspace;
        if (isCreating()) {
            if (metaParam.getInstance() != null)
                throw new IllegalStateException("Cant overwrite metasubspace's subspace");
            subspace = new Subspace();
            subspace.setId(nextUUID());
            subspace.setMetaSubspace(metaParam);
            metaParam.setInstance(subspace);

            //final StorageSize avail = new StorageSize();
            //avail.setAmount(getSize().getAmount());
            //avail.setUnit(getSize().getUnit());
            //subspace.setAvailableSize(avail);
            final long avail = getSize( );
            subspace.setAvailableSize(avail);
        }
        else
            subspace = metaParam.getInstance();
        return subspace;
    }


    public ImmutableScopedName getSubspace() {
        return subspace;
    }


    public void setSubspace(final ImmutableScopedName subspaceParam) {
        subspace = subspaceParam;
    }


    @SuppressWarnings({ "NonBooleanMethodNameMayNotStartWithQuestion" })
    public Boolean isVisibleToPublic() {
        return visible;
    }

    public long getSize() {
        return size;
    }


    public void setIsVisibleToPublic(final Boolean visibleToPublicParam) {
        visible = visibleToPublicParam;
    }


    public void setSize(final long sizeParam) {
        size = sizeParam;
    }


    public String getPath() {
        return path;
    }

    public void setPath(final String pathParam) {
        path = pathParam;
    }


    public String getGsiFtpPath() {
        return gsiFtpPath;
    }


    public void setGsiFtpPath(final String gsiFtpPathParam) {
        gsiFtpPath = gsiFtpPathParam;
    }
}
