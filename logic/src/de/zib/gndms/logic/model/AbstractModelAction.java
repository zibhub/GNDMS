package de.zib.gndms.logic.model;

import de.zib.gndms.model.common.GridEntity;
import org.jetbrains.annotations.NotNull;


/**
 * An abstract entity action on a given persistence model.
 * 
 * The first template parameter is the model for this action, the second is the return type.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * 
 * User: mjorra, Date: 12.08.2008, Time: 16:36:20
 */
public abstract class AbstractModelAction<M extends GridEntity, R> extends AbstractEntityAction<R>
	  implements ModelAction<M, R> {

    private volatile M model;

    public M getModel() {
        return model;
    }

    public void setModel(final @NotNull M mdl) {
        model = mdl;
    }


    @Override
    public void initialize() {
        preInitialize();
        postInitialize();
    }


    protected void preInitialize() {
         super.initialize();    // Overridden method
     }

    protected void postInitialize() {
        requireParameter("model", getModel());
    }
}
