package de.zib.gndms.model.test;

import org.testng.annotations.Parameters;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;import static org.testng.Assert.assertNull;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.dspace.Subspace;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 20.10.2008, Time: 13:11:43
 */
public class QueryTest extends ModelEntityTestBase {

    private static final String getSubspaceQuery =
        "SELECT x FROM subspaces x WHERE x.metaSubspace.scopedName = :uriParam";

    @Parameters( { "dbPath", "dbName"} )
    public QueryTest( String dbPath, @Optional( "c3grid" )String dbName ) {
        super( dbPath, dbName );
    }



    @Test( groups={ "db" } )
    public void testSubspaceQuery( ) {

        EntityManager em = getEntityManager();
        Query q = em.createQuery( getSubspaceQuery );
        q.setParameter( "uriParam", new ImmutableScopedName( "http://www.c3grid.de/G2/Subspace/", "ProviderStageIn" ) );
        Subspace s = ( Subspace ) q.getSingleResult();
        assertNull( s, "subspace not found" );
    }

}
