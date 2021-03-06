/**
 * Copyright 2008-2012 Zuse Institute Berlin (ZIB)
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
 *
 */

package de.zib.gndms.taskflows.esgfStaging.client.tools;

import de.zib.gndms.common.model.gorfx.types.SliceResult;
import de.zib.gndms.common.model.gorfx.types.TaskResult;
import de.zib.gndms.common.rest.CertificatePurpose;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.gndmc.gorfx.AbstractTaskFlowExecClient;
import de.zib.gndms.gndmc.gorfx.ExampleTaskFlowExecClient;
import de.zib.gndms.gndmc.gorfx.GORFXTaskFlowExample;
import de.zib.gndms.taskflows.esgfStaging.client.model.ESGFStagingOrder;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.args4j.Option;

import java.io.IOException;
import java.util.Properties;

/**
 * @date: 12.03.12
 * @time: 14:54
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class ESGFStagingExample extends GORFXTaskFlowExample {

    private AbstractTaskFlowExecClient etfc;

    @Option( name = "-retrieve", usage="forces usage of myproxy retrieve instead of get" )
    boolean retrieve = false;


    public static void main( String[] args ) throws Exception {

        GORFXTaskFlowExample cnt = new ESGFStagingExample();
        cnt.run( args );
        System.exit( 0 );
    }


    public ESGFStagingExample() {
        super( true );
    }


    @Override
    protected AbstractTaskFlowExecClient provideTaskFlowClient() {

        etfc = new ExampleTaskFlowExecClient() {
            @Override
            public void handleResult( TaskResult res ) {

                SliceResult result = SliceResult.class.cast( res );
                System.out.println( "Slice URL: " );
                System.out.println( "\t" + result.getSliceSpecifier().getUrl() );
            }


            @Override
            protected GNDMSResponseHeader setupContext( final GNDMSResponseHeader context ) {

                context.addMyProxyToken( CertificatePurpose.ESGF.toString(), myProxyLogin, myProxyPasswd, retrieve );
                return context;
            }
        };
        return etfc;
    }


    protected void normalRun() throws Exception {

        System.out.println( "Performing normal run!!" );

        requiresMyProxy();

        // create an order instance...
        ESGFStagingOrder esgfStagingOrder = loadOrderFromProps( orderPropFile );

        etfc.execTF( esgfStagingOrder, dn );
        System.out.println( "DONE\n" );
    }


    protected ESGFStagingOrder loadOrderFromProps( final String orderPropFilename )
            throws IOException
    {

        final @NotNull Properties properties = loadOrderProps( orderPropFilename );

        final ESGFStagingOrderPropertyReader reader =
                new ESGFStagingOrderPropertyReader( properties );
        reader.begin();
        reader.read();
        return reader.getProduct();
    }


    protected void failingRun() {

    }
}
