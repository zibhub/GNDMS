package de.zib.gndms.infra.tests;

import de.zib.gndms.infra.system.SysTestBase;
import de.zib.gndms.model.gorfx.types.TaskState;
import org.testng.annotations.Test;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.BeforeClass;
import static org.testng.Assert.assertNotNull;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 27.10.2008, Time: 14:04:11
 */
public class CommonQueryTest extends SysTestBase {

    @Parameters( { "gridName" } )
    public CommonQueryTest( @Optional( "c3grid" )String gridName ) {
        super( gridName );
    }


    @BeforeClass( groups={"db", "tasks"} )
    public void blah( ) {

    }


    @Test( groups={"db", "tasks"} )
    public void testIt( ) throws Exception{

        runDatabase();

        EntityManager em = null;
        List<Object> rs = null;
        try{
            em = getSys().getEntityManagerFactory().createEntityManager(  );
            // Query q = em.createNamedQuery( "unfinishedTaskIds" );
            // Query q = em.createQuery( "SELECT t.id FROM Tasks t WHERE t.state NOT LIKE 'FAILED'" );
            // doesn't work:  exception "string"  WETM
            // Query q = em.createQuery( "SELECT t.id FROM Tasks t WHERE t.state != 'FAILED'" );
            // doesn't work: ! encountered near ""
            Query q = em.createQuery( "SELECT t.id FROM Tasks t WHERE t.state <> de.zib.gndms.model.gorfx.types.TaskState.FAILED"
                + " AND t.state <> de.zib.gndms.model.gorfx.types.TaskState.FINISHED"  );
            rs = q.getResultList();
            assertNotNull( rs, "No matches found" );
            System.out.println( "Results: " + rs.size( ) );
            for( Object o : rs )
                System.out.println( o.toString( ) );

        } finally {
            if( em != null && em.isOpen( ) )
                em.close( );
        }
    }

}
