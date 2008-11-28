package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.model.gorfx.OfferType;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.AbstractTask;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
import de.zib.gndms.kit.factory.FactoryInstance;
import de.zib.gndms.kit.factory.Factory;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;


/**
 * Adds some type-safety to TaskActions
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 02.10.2008 Time: 13:00:56
 */
public abstract class ORQTaskAction<K extends AbstractORQ> extends TaskAction
    implements FactoryInstance<OfferType, ORQTaskAction<?>>
{
    private Factory<OfferType, ORQTaskAction<?>> factory;
    private OfferType key;


    protected ORQTaskAction() {
        super();
    }


    public ORQTaskAction(final @NotNull EntityManager em, final @NotNull AbstractTask model) {
        super(em, model);
    }


    public ORQTaskAction( final @NotNull EntityManager em, final @NotNull String pk, Class<? extends AbstractTask> cls ) {
        super(em, pk, cls );
    }

    
    public ORQTaskAction( final @NotNull EntityManager em, final @NotNull String pk ) {
        super(em, pk, Task.class );
    }


    @SuppressWarnings({ "ThrowableInstanceNeverThrown" })
    @Override
    protected void onCreated(final AbstractTask model) {
        try {
            super.onCreated(model);    // Overridden method
        }
        catch (TransitException e) {
            if (e.isDemandingAbort()) throw e; // dont continue on failure

            EntityManager em = getOwnEntityManager();
            try {
                em.getTransaction().begin();
                final OfferType ot = em.find(OfferType.class, getOrq().getOfferType());
                if (ot == null)
                    fail(new NullPointerException("Unsupported OfferType"));
                getModel().setOfferType(ot);
                em.getTransaction().commit();
            }
            finally {
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
            }
            throw e; // accept state transition decision from super
        }
    }


    protected abstract @NotNull Class<K> getOrqClass();


    @Override
    protected final @NotNull Class<AbstractTask> getTaskClass() {
        return AbstractTask.class;
    }


    public void setOrq(final @NotNull K orq) {
        if (getModel().getOrq() != null)
            getModel().setOrq(orq);
        else
            throw new IllegalStateException("Illgeal attempt to overwrite Task ORQ");
    }

    
    public @NotNull K getOrq() {
        final AbstractTask model = getModel();
        if (model == null)
            throw new IllegalStateException("Model missing");
        return getOrqClass().cast(getModel().getOrq());
    }


    public Factory<OfferType, ORQTaskAction<?>> getFactory() {
        return factory;
    }


    public OfferType getKey() {
        return key;
    }


    public void setFactory(@NotNull final Factory<OfferType, ORQTaskAction<?>> factoryParam) {
        factory = factoryParam;
    }


    public void setKey(@NotNull final OfferType keyParam) {
        key = keyParam;
    }
}
