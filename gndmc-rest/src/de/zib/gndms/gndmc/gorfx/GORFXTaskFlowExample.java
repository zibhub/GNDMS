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

import de.zib.gndms.common.kit.application.AbstractApplication;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

/**
 * @author try ma ik jo rr a zib
 * @date 14.03.11 11:46
 * @brief A test run for the gorfx taskflow execution.
 */
public abstract class GORFXTaskFlowExample extends AbstractApplication {

	@Option(name = "-uri", required = true, usage = "URL of GORFX-Endpoint", metaVar = "URI")
	protected String gorfxEpUrl;
	@Option(name = "-dn", required = true, usage = "DN")
	protected String dn;
    @Option( name="-props", required=true, usage="staging.properties" )
    protected String orderPropFile;
    @Option( name="-con-props", usage="contract.properties" )
    protected String conPropFile;
    @Option( name="-proxyfile", usage="grid-proxy-file to lead", metaVar="proxy-file" )
    protected String proxyFile = null;
    @Option( name="-cancel", required = false, usage = "ms to wait before destroying taskClient.")
    protected Long cancel = null;

	protected String wid = UUID.randomUUID().toString();
    private ApplicationContext context;
    private FullGORFXClient gorfxClient;
    private TaskFlowClient tfClient;
    private TaskClient taskClient;
    private AbstractTaskFlowExecClient etfc;


    @Override
    public void run() throws Exception {
        System.out.println( "Running dummy taskFlow test with: " );
        System.out.println("connection to: \"" + gorfxEpUrl + "\"");

        context = new ClassPathXmlApplicationContext(
                "classpath:META-INF/client-context.xml");
        gorfxClient = (FullGORFXClient ) context
                .getAutowireCapableBeanFactory().createBean(
                FullGORFXClient.class,
                AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true );
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

        etfc = provideTaskFlowClient();
        etfc.setGorfxClient( gorfxClient );
        etfc.setTfClient( tfClient );
        etfc.setTaskClient( taskClient );

        normalRun();
        failingRun();
	}

    protected abstract AbstractTaskFlowExecClient provideTaskFlowClient( );

    protected abstract void normalRun();

    protected abstract void failingRun();


    // Load SFR property file
    protected Properties loadOrderProps( final String orderPropsFilename ) throws IOException {

        InputStream is;
        if ( orderPropsFilename.trim().equals( "-" ) ) {
            System.out.println( "Reading props von stdin" );
            is = System.in;
            System.out.println( "done" );
        } else
            is = new FileInputStream(  orderPropsFilename );

        Properties orderProps = new Properties( );

        try {
            orderProps.load( is );
        } finally {
            is.close();
        }
        return orderProps;
    }
}
