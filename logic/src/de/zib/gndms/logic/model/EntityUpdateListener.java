package de.zib.gndms.logic.model;

import de.zib.gndms.model.common.GridResource;


/**
 * An EntityUpdateListener will be informed by an <tt>BatchUpdateAction</tt> on a model.
 *
 * @see BatchUpdateAction
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 12.08.2008 Time: 18:33:47
 */
public interface EntityUpdateListener<M extends GridResource> {

    /**
     * A class waiting for changes on the model must implement this method.
     * It will be notified about a change, using this method with the new model.
     *
     * @param model the new model
     */
    void onModelChange( M model );
}
