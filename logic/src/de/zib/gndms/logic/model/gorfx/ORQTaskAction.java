package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.model.gorfx.OfferType;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
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
public abstract class ORQTaskAction<K extends AbstractORQ> extends TaskAction<Task> {

    public ORQTaskAction(final @NotNull EntityManager em, final @NotNull Task model) {
        super(em, model);
    }


    public ORQTaskAction(final @NotNull EntityManager em, final @NotNull String pk) {
        super(em, pk);
    }


    @SuppressWarnings({ "ThrowableInstanceNeverThrown" })
    @Override
    protected final void onCreated(final Task model) {
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
    protected final @NotNull Class<Task> getTaskClass() {
        return Task.class;
    }



    public void setOrq(final @NotNull K orq) {
        if (getModel().getOrq() != null)
            getModel().setOrq(orq);
        else
            throw new IllegalStateException("Illgeal attempt to overwrite Task ORQ");
    }

    public @NotNull K getOrq() {
        final Task model = getModel();
        if (model == null)
            throw new IllegalStateException("Model missing");
        return getOrqClass().cast(getModel().getOrq());
    }
}
