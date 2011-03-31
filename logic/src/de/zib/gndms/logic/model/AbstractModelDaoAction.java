package de.zib.gndms.logic.model;

import de.zib.gndms.model.ModelEntity;
import de.zib.gndms.model.common.GridResourceItf;
import de.zib.gndms.neomodel.common.Dao;
import org.jetbrains.annotations.NotNull;

/**
 * AbstractModelDaoAction
 *
 * @author try ste fan pla nti kow zib
 *         <p/>
 *         User stepn Date: 18.02.11 TIME: 18:19
 */
public abstract class AbstractModelDaoAction<M extends ModelEntity & GridResourceItf, R>
        extends AbstractModelEntityAction<M, R> implements ModelDaoAction<M, R> {
    private Dao dao;
    
    public Dao getDao() {
        if (dao == null) {
            final DaoAction<?> daoAction = nextParentOfType(DaoAction.class);
            if (daoAction != null)
                return daoAction.getDao();
        }

        return dao;
    }

    
    public Dao getOwnDao() {
        return dao;
    }


    public void setOwnDao(final @NotNull Dao daoParam) {
        dao = daoParam;
    }

    @Override
    protected void postInitialize() {
        requireParameter("dao", getDao());
        super.postInitialize();
    }
}
