package de.zib.gndms.gndmc.dspace.test;

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

import de.zib.gndms.gndmc.dspace.SubspaceClient;
import de.zib.gndms.gndmc.gorfx.FullGORFXClient;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.ResponseEntity;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import de.zib.gndms.logic.model.dspace.SubspaceConfiguration;

/**
 * Tests the DSpaceClient.
 * 
 * @author Ulrike Golas
 */

public class SubspaceClientTest {
	/**
	 * Tests the constructors.
	 */
	@Test
    public final void testConstructor() {		
		
		SubspaceClient scl = new SubspaceClient();
       	AssertJUnit.assertNotNull(scl);
		
		String a = "test";
		scl = new SubspaceClient(a);
		
       	AssertJUnit.assertEquals(a, scl.getServiceURL());
   	}

	/**
	 * Tests the request methods.
	 */
	@Test
    public final void testBehavior() {
        final String serviceUrl = "http://localhost:8082/c3grid";

        ApplicationContext context = new ClassPathXmlApplicationContext( "classpath:META-INF/client-context.xml" );
        SubspaceClient subspaceClient = ( SubspaceClient )context.getAutowireCapableBeanFactory().createBean(
                        SubspaceClient.class,
                        AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true );
        subspaceClient.setServiceURL( serviceUrl );

		String dn = "dn";
		String subspace = "sub";

		String path = "/var/tmp/gndms/dspace/";
		String gsiftp = "gsiftp";
		boolean visible = true;
		final long value = 6000;
		String mode = "CREATE";
       	SubspaceConfiguration config = new SubspaceConfiguration(path, gsiftp, visible, value, mode, subspace);
        config.setSubspace( subspace );

        subspaceClient.createSubspace(subspace, config, dn);
       	//AssertJUnit.assertNotNull(res);

		//res = scl.deleteSubspace(subspace, dn);
        //AssertJUnit.assertNotNull(res);

		//res = scl.listSubspaceConfiguration(subspace, dn);
       	//AssertJUnit.assertNotNull(res);

		//res = scl.setSubspaceConfiguration(subspace, config, dn);
       	//AssertJUnit.assertNotNull(res);

		//res = scl.listSliceKinds(subspace, dn);
       	//AssertJUnit.assertNotNull(res);
   	}
}
