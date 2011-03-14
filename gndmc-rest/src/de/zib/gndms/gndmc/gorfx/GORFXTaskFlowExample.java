package de.zib.gndms.gndmc.gorfx;

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

import de.zib.gndms.kit.action.ActionMeta;
import de.zib.gndms.kit.application.AbstractApplication;
import de.zib.gndms.kit.config.ConfigMeta;
import de.zib.gndms.logic.taskflow.tfmockup.DummyTF;
import de.zib.gndms.model.gorfx.types.AbstractTF;
import de.zib.gndms.model.gorfx.types.TaskFailure;
import de.zib.gndms.model.gorfx.types.TaskFlowStatus;
import de.zib.gndms.model.gorfx.types.TaskResult;
import de.zib.gndms.rest.Facet;
import de.zib.gndms.rest.Facets;
import de.zib.gndms.rest.GNDMSResponseHeader;
import de.zib.gndms.rest.Specifier;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * @author try ma ik jo rr a zib
 * @date 14.03.11 11:46
 * @brief A test run for the gorfx taskflow execution.
 */
public class GORFXTaskFlowExample extends AbstractApplication {

	@Option(name = "-uri", required = true, usage = "URL of GORFX-Endpoint", metaVar = "URI")
	protected String gorfxEpUrl;
	@Option(name = "-dn", required = true, usage = "DN")
	protected String dn;
	protected String wid = UUID.randomUUID().toString();
    private ApplicationContext context;
    private FullGORFXClient gorfxClient;
    private TaskFlowClient tfClient;
    private TaskClient taskClient;
    private AbstractTaskFlowExecClient etfc;


    public static void main(String[] args) throws Exception {

		GORFXTaskFlowExample cnt = new GORFXTaskFlowExample();
		cnt.run(args);
		System.exit(0);
	}

	@Override
	public void run() throws Exception {
        System.out.println( "Running dummy taskFlow test with: " );
        System.out.println("connection to: \"" + gorfxEpUrl + "\"");

        context = new ClassPathXmlApplicationContext(
                "classpath:META-INF/client-context.xml");
        gorfxClient = (FullGORFXClient ) context
                .getAutowireCapableBeanFactory().createBean(
                        FullGORFXClient.class,
                        AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
		gorfxClient.setServiceURL(gorfxEpUrl);

        tfClient = (TaskFlowClient ) context
            .getAutowireCapableBeanFactory().createBean(
                TaskFlowClient.class,
                AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
        tfClient.setServiceURL(gorfxEpUrl);

        taskClient = (TaskClient ) context
            .getAutowireCapableBeanFactory().createBean(
                TaskClient.class,
                AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
        taskClient.setServiceURL(gorfxEpUrl);

        etfc = new ExampleTaskFlowExecClient();
        etfc.setGorfxClient( gorfxClient );
        etfc.setTfClient( tfClient );
        etfc.setTaskClient( taskClient );

        normalRun();
        failingRun();
	}


    private void normalRun( )  {

        System.out.println( "Performing normal run!!" );
        DummyTF dft = new DummyTF();
        dft.setMessage( "Test task flow is flowing" );
        dft.setTimes( 20 );
        dft.setDelay( 1000 );
        dft.setFailIntentionally( false );
        etfc.execTF( dft, dn );
        System.out.println( "DONE\n" );
    }

    private void failingRun( )  {


        System.out.println( "Performing task which will fail!" );
        DummyTF dft = new DummyTF();
        dft.setMessage( "I'm going to fail" );
        dft.setTimes( 30 );
        dft.setDelay( 1000 );
        dft.setFailIntentionally( true );
        etfc.execTF( dft, dn );
        System.out.println( "DONE\n" );
    }
}
