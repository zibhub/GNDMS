package de.zib.gndms.logic.model;

import de.zib.gndms.logic.action.Action;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.common.ModelUUIDGen;
import de.zib.gndms.neomodel.common.NeoDao;
import de.zib.gndms.neomodel.common.NodeGridResource;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;

/**
 * DaoAction
 *
 * @author try ste fan pla nti kow zib
 *         <p/>
 *         User stepn Date: 18.02.11 TIME: 17:57
 */
public interface DaoAction<R> extends Action<R>, ModelUUIDGen {
    NeoDao getDao();

    NeoDao getOwnDao();
    void setOwnDao(NeoDao dao);
}