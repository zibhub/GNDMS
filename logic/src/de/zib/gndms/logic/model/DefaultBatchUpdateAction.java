package de.zib.gndms.logic.model;

import de.zib.gndms.logic.action.DefaultCompositeAction;
import de.zib.gndms.logic.action.Action;
import de.zib.gndms.model.common.GridResource;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Collection;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 12.08.2008 Time: 18:54:38
 */
public class DefaultBatchUpdateAction<M extends GridResource> extends DefaultCompositeAction<Void, Void>
	implements BatchUpdateAction<M, Void> {

	private EntityUpdateListener<M> listener;


    @Override
    public void initialize() {
        if (getActions() == null)
            setActions(new LinkedList<Action<Void>>());
        super.initialize();    // Overridden method
    }


    public EntityUpdateListener<M> getListener() {
		return listener;
	}


	public void setListener(final @NotNull EntityUpdateListener<M> listenerParam) {
		listener = listenerParam;
	}


    @Override
    public void addAction(final Action<Void> voidActionParam) {
        voidActionParam.setParent(this);
        super.addAction(voidActionParam);    // Overridden method
    }


    @Override
    public void cleanUp() {
        final Collection<Action<Void>> collection = getActions();
        if (collection != null)
            collection.clear();
        super.cleanUp();    // Overridden method
    }
}
