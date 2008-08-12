import javax.persistence.EntityManager;
import java.util.concurrent.Callable;

/**
 * Interface for an action which requires a EntityManager
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 12.08.2008, Time: 15:25:36
 */

public interface Action<R> extends Callable<R> {

    public void initialize( );

    public R call( ) throws RuntimeException, NoEntityManagerException;

    public void cleanUp( );

    public void setEntityManeger( EntityManager em );

}
