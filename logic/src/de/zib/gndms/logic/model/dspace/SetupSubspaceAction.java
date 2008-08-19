package de.zib.gndms.logic.model.dspace;

import de.zib.gndms.logic.action.MandatoryOptionMissingException;
import de.zib.gndms.logic.model.config.ConfigActionHelp;
import de.zib.gndms.logic.model.config.ConfigOption;
import de.zib.gndms.logic.model.config.SetupAction;
import de.zib.gndms.logic.model.ModelChangedAction;
import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.dspace.MetaSubspace;
import de.zib.gndms.model.dspace.StorageSize;
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
@ConfigActionHelp(shortHelp = "Setup a subspace", longHelp = "Used to prepare the database schema for a certain GNDMS function by creating, updating, and deleting subspaces")
public class SetupSubspaceAction extends SetupAction<Void> {
    @ConfigOption(descr="The scope of the subspace (Part of PK)")
    private String scope;

    @ConfigOption(descr="The name of the subspace in the scope (Part of PK)")
    private String name;

    @ConfigOption(descr="Local filesystem root path for all slices stored in this subspace")
    private String path;

    @ConfigOption(descr="If set, this subspace is included in the public service listing")
    private Boolean visible;

    @ConfigOption(descr="Maximum storage size available in this subspace")
    private StorageSize size;


    @Override
    public void initialize() {
        super.initialize();    // Overridden method
        try {
            if (scope == null && (isCreating() || hasOption("scope")))
                setScope(getOption("scope"));
            if (name == null && (isCreating() || hasOption("name")))
                setName(getOption("name"));
            if (visible == null && (isCreating() || hasOption("visible")))
                setIsVisibleToPublic(isBooleanOptionSet("visible", true));
            if (size == null && (isCreating() || hasOption("size"))) {
                size = new StorageSize();
                size.setAmount(getIntOption("size"));
            }
            if (path == null && (isCreating() || hasOption("path"))) {
                setPath(getOption("path"));
            }
        }
        catch (MandatoryOptionMissingException e) {
            throw new IllegalStateException(e);
        }

        requireParameter("scope", name);
        requireParameter("name", name);
    }


    @SuppressWarnings({ "FeatureEnvy", "MethodWithMoreThanThreeNegations" })
    @Override
    public Void execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
        final ImmutableScopedName pk = new ImmutableScopedName(getScope(), getName());

        MetaSubspace meta = prepareMeta(em, pk);
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
        
       return null;
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

            final StorageSize avail = new StorageSize();
            avail.setAmount(getSize().getAmount());
            avail.setUnit(getSize().getUnit());
        }
        else
            subspace = metaParam.getInstance();
        return subspace;
    }


    public String getScope() {
        return scope;
    }


    public String getName() {
        return name;
    }


    @SuppressWarnings({ "NonBooleanMethodNameMayNotStartWithQuestion" })
    public Boolean isVisibleToPublic() {
        return visible;
    }

    public StorageSize getSize() {
        return size;
    }


    public void setScope(final String scopeParam) {
        scope = scopeParam;
    }


    public void setName(final String nameParam) {
        name = nameParam;
    }


    public void setIsVisibleToPublic(final Boolean visibleToPublicParam) {
        visible = visibleToPublicParam;
    }


    public void setSize(final StorageSize sizeParam) {
        size = sizeParam;
    }


    public String getPath() {
        return path;
    }

    public void setPath(final String pathParam) {
        path = pathParam;
    }

}
