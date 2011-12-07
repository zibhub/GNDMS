/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.zib.gndms.gndmc.dspace;

import de.zib.gndms.common.logic.config.Configuration;
import de.zib.gndms.common.logic.config.SetupMode;
import de.zib.gndms.logic.model.DefaultBatchUpdateAction;
import de.zib.gndms.logic.model.NoWSDontNeedModelUpdateListener;
import de.zib.gndms.logic.model.dspace.SetupSubspaceAction;
import de.zib.gndms.logic.model.dspace.SubspaceConfiguration;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.dspace.Subspace;
import org.testng.Assert;

import javax.persistence.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

import static org.testng.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: explicit
 * Date: 02.12.11
 * Time: 15:31
 * To change this template use File | Settings | File Templates.
 */
public class Test extends JPATest {
    
    @org.testng.annotations.Test( groups = { "jpa" } )
    public void test0() {
        EntityManager em = emf.createEntityManager();

        String subspace = "sub";

        final EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            for( long i = 0; i < 2<<16; ++i )
            {
                TestTable t = new TestTable();
                t.setA( String.valueOf( i ) );
                em.persist(t);
            }

            transaction.commit();
        }
        finally {
            try {
                if( transaction.isActive() )
                    transaction.rollback();
                em.close();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }



    @org.testng.annotations.Test( groups = { "jpa" } )
    public void test1() {
        EntityManager em = emf.createEntityManager();

        String subspace = "sub";
        TestTable t = new TestTable();

        t.setA( "blub A" );

        final EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
          //  em.persist(t);

            Subspace space = new Subspace();
            space.setAvailableSize(100);
            space.setGsiFtpPath("gridftppath");
            space.setPath("/tmp/gndms/sub");
            space.setId( subspace );
            em.persist( space );

            SubspaceConfiguration subspaceConfig = new SubspaceConfiguration( "/var/tmp/gndms/subpath", "gridftppath", true, 100, SetupMode.CREATE, "sub" );
            SetupSubspaceAction action = new SetupSubspaceAction( subspaceConfig );

            StringWriter sw = new StringWriter();

            action.setClosingEntityManagerOnCleanup( false );
            action.setOwnEntityManager(em);
            action.setPrintWriter(new PrintWriter(sw));
            log.info("Calling action for setting up the supspace "
                    + subspace + ".");
            action.setOwnPostponedEntityActions(new DefaultBatchUpdateAction<GridResource>());
            action.getPostponedEntityActions().setListener( new NoWSDontNeedModelUpdateListener() );
            action.call();

            transaction.commit();
        }

        finally {
            try {
                em.close();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }


    @org.testng.annotations.Test( groups = { "jpa" }, dependsOnMethods = { "test1" } )
    public void test2() {
        EntityManager em = emf.createEntityManager();
        final EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Subspace sub = em.find( Subspace.class, "sub" );
            Assert.assertNotNull( sub );
            Assert.assertEquals( sub.getGsiFtpPath(), "gridftppath" );
            transaction.commit();
        }
        finally {
            try{
                em.close();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }
}
