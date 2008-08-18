package de.zib.gndms.logic.model.dspace;

import de.zib.gndms.logic.action.MandatoryOptionMissingException;
import de.zib.gndms.logic.model.config.SetupAction;
import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.dspace.MetaSubspace;
import de.zib.gndms.model.dspace.StorageSize;
import de.zib.gndms.model.dspace.Subspace;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Calendar;


/**
 * Creates a new subspace
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 14.08.2008 Time: 17:37:51
 */
public class SetupSubspaceAction extends SetupAction<Void> {
    private String scope;
    private String name;
    private String path;
    private Boolean visible;
    private StorageSize size;
    private Calendar tod;


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
            if (tod == null && (isCreating() || hasOption("tod")))
                setTod(getISO8601Option("tod", Calendar.getInstance()));
        }
        catch (MandatoryOptionMissingException e) {
            throw new IllegalStateException(e);
        }
        catch (ParseException e) {
            throw new IllegalStateException(e);
        }

        requireParameter("scope", name);
        requireParameter("name", name);
    }


    @SuppressWarnings({ "FeatureEnvy" })
    @Override
    public Void execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
        final ImmutableScopedName pk = new ImmutableScopedName(getScope(), getName());

        MetaSubspace meta = prepareMeta(em, pk);
        Subspace subspace = prepareSubspace(meta);

        
        if (size != null)
            subspace.setTotalSize(getSize());
        
        if (path != null)
            subspace.setPath(getPath());
        
        if (tod != null)
            subspace.setTerminationTime(getTod());

        if (isCreating()) {
            em.persist(subspace);
            em.persist(meta);            
        }
        
        return null;
    }


    private MetaSubspace prepareMeta(final EntityManager em, final ImmutableScopedName pkParam) {
        MetaSubspace meta= em.find(MetaSubspace.class, pkParam);
        if (meta == null) {
            if (isUpdating())
                throw new IllegalStateException("No matching metasubspace found for update");
            meta = new MetaSubspace();
            meta.setScopedName(pkParam);
        }
        if (isVisibleToPublic() != null)
            meta.setVisibleToPublic(isVisibleToPublic());
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

    @SuppressWarnings({ "ReturnOfDateField" })
    public Calendar getTod() {
        return tod;
    }


    @SuppressWarnings({ "AssignmentToDateFieldFromParameter" })
    public void setTod(final Calendar todParam) {
        tod = todParam;
    }
}
