import javax.persistence.EntityManager;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * 
 * User: mjorra, Date: 12.08.2008, Time: 15:33:10
 */
public abstract class AbstractAction<R> implements Action<R> {

    private EntityManager entityManager;

    public void initialize() { }

    public R call ( ) throws RuntimeException, NoEntityManagerException {

        if( entityManager == null )
            throw new NoEntityManagerException( );

        initialize( );
        try{
            return execute( entityManager );
        } finally{
            cleanUp( );
        }
    }

    public abstract R execute( EntityManager em );

    public void cleanUp() { }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


}
