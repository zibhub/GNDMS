import de.zib.gndms.model.common.GridEntity;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * 
 * User: mjorra, Date: 12.08.2008, Time: 16:36:20
 */
public abstract class AbstractModelAction<M extends GridEntity, R> extends AbstractAction<R> implements  ModelAction<M, R> {

    private M model;

    public void setModel(M mdl) {
        model = mdl;
    }

    public M getModel() {
        return model;
    }
}
