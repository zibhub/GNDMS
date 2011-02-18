package de.zib.gndms.logic.model;

import de.zib.gndms.model.ModelEntity;
import de.zib.gndms.model.common.GridResourceItf;

/**
 * ModelDaoAction
 *
 * @author try ste fan pla nti kow zib
 *         <p/>
 *         User stepn Date: 18.02.11 TIME: 18:37
 */
public interface ModelDaoAction<M extends ModelEntity & GridResourceItf, R> extends DaoAction<R>, ModelAction<M, R> {
    ModelUpdateListener<M> getModelUpdateListener();
}
