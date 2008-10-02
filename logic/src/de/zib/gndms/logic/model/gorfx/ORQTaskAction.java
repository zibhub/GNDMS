package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
import org.jetbrains.annotations.NotNull;


/**
 * Adds some type-safety to TaskActions
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 02.10.2008 Time: 13:00:56
 */
public abstract class ORQTaskAction<M extends Task, K extends AbstractORQ> extends TaskAction<M> {
    public void setOrq(final @NotNull K orq) {
        if (getModel().getOrq() != null)
            getModel().setOrq(orq);
        else
            throw new IllegalStateException("Illgeal attempt to overwrite Task ORQ");
    }

    public K getOrq(final @NotNull Class<K> orqClass) {
        return orqClass.cast(getModel().getOrq());
    }
}
