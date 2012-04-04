package de.zib.gndms.model.test;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.util.TxFrame;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 20.10.2008, Time: 13:11:43
 */
public class QueryTest extends ModelEntityTestBase {

    private static final String getSubspaceQuery =
        //"SELECT x FROM SliceKinds x WHERE x.creatableSliceKinds = :uriParam";
        "SELECT x FROM Subspaces x WHERE x.path LIKE :path";

    @Parameters( { "dbPath", "dbName"} )
    public QueryTest( String dbPath, @Optional( "c3grid" )String dbName ) {
        super( dbPath, dbName );
    }

    @Test( groups={ "db" } )
    public void testCreateSubspaceEntity( ) {
        EntityManager em = getEntityManager();

        Subspace s = new Subspace();
        s.setPath( "/tmp/testsubspace" );
        s.setId( "TESTSUB" );

        TxFrame txFrame = new TxFrame( em );
        em.persist( s );
        txFrame.commit();
        txFrame.finish();
    }

    @Test( groups={ "db" }, dependsOnMethods = "testCreateSubspaceEntity" )
    public void testSubspaceQuery( ) {

        EntityManager em = getEntityManager();
        Query q = em.createQuery( getSubspaceQuery );
        q.setParameter( "path", "/tmp/%" );
        List< Subspace > res = q.getResultList();

        assert res.size() > 0;
    }

}
