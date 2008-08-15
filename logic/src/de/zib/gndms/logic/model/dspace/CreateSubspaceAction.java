package de.zib.gndms.logic.model.dspace;

import de.zib.gndms.logic.model.config.ConfigAction;
import de.zib.gndms.logic.action.MandatoryOptionMissingException;
import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.dspace.MetaSubspace;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.dspace.StorageSize;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 14.08.2008 Time: 17:37:51
 */
public class CreateSubspaceAction extends ConfigAction<Void> {
    private String scope;
    private String name;
    private Boolean visibleToPublic;
    private StorageSize availableSize;
    private StorageSize size;


    @Override
    public void initialize() {
        super.initialize();    // Overridden method
        try {
            if (scope == null)
                setScope(getOption("scope", "http://www.c3grid.de/G2/"));
            if (name == null)
                setName(getOption("name"));
            if (visibleToPublic == null)
                setIsVisibleToPublic(isBooleanOptionSet("visible", true));
            if (availableSize == null) {
                availableSize = new StorageSize();
                availableSize.setAmount(0);
            }
            if (size == null) {
                size = new StorageSize();
                size.setAmount(getIntOption("size"));
            }
        }
        catch (MandatoryOptionMissingException e) {
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

        if (meta != null)
            throw new IllegalArgumentException("Subspace already existing!");
        meta = new MetaSubspace();

        meta.setScopedName(pk);
        meta.setVisibleToPublic(isVisibleToPublic());

        Subspace subspace = new Subspace();
        subspace.setMetaSubspace(meta);
        subspace.setAvailableSize(getAvailableSize());
        subspace.setTotalSize(getSize());

        em.persist(meta);
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
        return visibleToPublic;
    }


    public StorageSize getAvailableSize() {
        return availableSize;
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
        visibleToPublic = visibleToPublicParam;
    }


    public void setAvailableSize(final StorageSize availableSizeParam) {
        availableSize = availableSizeParam;
    }


    public void setSize(final StorageSize sizeParam) {
        size = sizeParam;
    }
}
