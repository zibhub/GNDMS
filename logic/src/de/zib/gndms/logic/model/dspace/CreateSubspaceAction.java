package de.zib.gndms.logic.model.dspace;

import de.zib.gndms.logic.action.MandatoryOptionMissingException;
import de.zib.gndms.logic.model.config.ConfigAction;
import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.dspace.MetaSubspace;
import de.zib.gndms.model.dspace.StorageSize;
import de.zib.gndms.model.dspace.Subspace;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;
import java.util.Calendar;
import java.text.ParseException;


/**
 * Creates a new subspace
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 14.08.2008 Time: 17:37:51
 */
public class CreateSubspaceAction extends ConfigAction<Void> {
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
            if (scope == null)
                setScope(getOption("scope", "http://www.c3grid.de/G2/"));
            if (name == null)
                setName(getOption("name"));
            if (visible == null)
                setIsVisibleToPublic(isBooleanOptionSet("visible", true));
            if (size == null) {
                size = new StorageSize();
                size.setAmount(getIntOption("size"));
            }
            if (path == null) {
                setPath(getOption("path"));
            }
            if (tod == null)
                setTod(getISO8601Option("tod", Calendar.getInstance()));
        }
        catch (MandatoryOptionMissingException e) {
            throw new IllegalStateException(e);
        }
        catch (ParseException e) {
            throw new IllegalStateException(e);
        }

        requireParameter("name", name);
        requireParameter("size", size);
    }


    @SuppressWarnings({ "FeatureEnvy" })
    @Override
    public Void execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
        final ImmutableScopedName pk = new ImmutableScopedName(getScope(), getName());
        MetaSubspace meta= em.find(MetaSubspace.class, pk);
        if (meta == null) {
            meta = new MetaSubspace();
            meta.setScopedName(pk);
            meta.setVisibleToPublic(isVisibleToPublic());
            em.persist(meta);
        }
        else if (meta.getInstance() != null)
            throw new IllegalArgumentException("Subspace already existing!");

        Subspace subspace = new Subspace();
        subspace.setMetaSubspace(meta);
        final StorageSize avail = new StorageSize();
        avail.setAmount(getSize().getAmount());
        avail.setUnit(getSize().getUnit());
        subspace.setAvailableSize(avail);
        subspace.setTotalSize(getSize());
        subspace.setPath(getPath());
        subspace.setTerminationTime(getTod());
        meta.setInstance(subspace);
        em.persist(subspace);

        return null;
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
