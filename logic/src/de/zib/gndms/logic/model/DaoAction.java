package de.zib.gndms.logic.model;

import de.zib.gndms.logic.action.Action;
import de.zib.gndms.model.common.ModelUUIDGen;
import de.zib.gndms.neomodel.common.Dao;

/**
 * DaoAction
 *
 * @author try ste fan pla nti kow zib
 *         <p/>
 *         User stepn Date: 18.02.11 TIME: 17:57
 */
public interface DaoAction<R> extends Action<R>, ModelUUIDGen {
    Dao getDao();

    Dao getOwnDao();
    void setOwnDao(Dao dao);
}