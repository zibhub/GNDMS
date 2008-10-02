package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.logic.model.TaskAction;
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
public abstract class ORQTaskAction<M extends Task, K extends AbstractORQ> extends TaskAction<M> {

    public ORQTaskAction() {
        super();
    }


    public ORQTaskAction(final @NotNull EntityManager em, final @NotNull M model) {
        super(em, model);
    }


    public ORQTaskAction(final @NotNull EntityManager em, final @NotNull String pk) {
        super(em, pk);
    }


    public void setOrq(final @NotNull K orq) {
        if (getModel().getOrq() != null)
            getModel().setOrq(orq);
        else
            throw new IllegalStateException("Illgeal attempt to overwrite Task ORQ");
    }

    public @NotNull K getOrq(final @NotNull Class<K> orqClass) {
        final M model = getModel();
        if (model == null)
            throw new IllegalStateException("Model missing");
        return orqClass.cast(getModel().getOrq());
    }
}
