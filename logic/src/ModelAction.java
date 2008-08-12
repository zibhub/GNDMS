import de.zib.gndms.model.common.GridEntity;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 12.08.2008, Time: 16:24:56
 */
public interface ModelAction<M extends GridEntity, R> extends Action <R> {

    public void setModel( M mdl );

    public M getModel( );
}
