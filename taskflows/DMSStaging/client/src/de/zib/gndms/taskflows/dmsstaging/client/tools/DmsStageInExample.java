/*
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

package de.zib.gndms.taskflows.dmsstaging.client.tools;

import de.zib.gndms.common.model.FileStats;
import de.zib.gndms.common.model.gorfx.types.Order;
import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.common.model.gorfx.types.TaskResult;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.gndmc.dspace.SliceClient;
import de.zib.gndms.gndmc.gorfx.AbstractTaskFlowExecClient;
import de.zib.gndms.gndmc.gorfx.ExampleTaskFlowExecClient;
import de.zib.gndms.gndmc.gorfx.GORFXTaskFlowExample;
import de.zib.gndms.taskflows.dmsstaging.client.model.DmsStageInOrder;
import de.zib.gndms.taskflows.dmsstaging.client.model.DmsStageInResult;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.args4j.Option;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * @date: 21.06.12
 * @time: 10:30
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class DmsStageInExample extends GORFXTaskFlowExample {

    // args4j options:
    /**
     * @deprecated
     */
    @Option( name="-oidprefix", required=false, usage="oidPrefix to be stripped vom Object-Ids")
    protected String oidPrefix = "";

    // members
    private AbstractTaskFlowExecClient etfc;
    private Order normalOrder;


    public static void main( String[] args ) throws Exception {

        GORFXTaskFlowExample cnt = new DmsStageInExample();
        cnt.run(args);
        System.exit(0);
    }


    public DmsStageInExample() {
        super( true );
    }


    @Override
    protected AbstractTaskFlowExecClient provideTaskFlowClient() {

        etfc = new ExampleTaskFlowExecClient() {
            @Override
            public void handleResult( TaskResult res ) {

                DmsStageInResult result = DmsStageInResult.class.cast( res );
                System.out.println("\n\nResult slice url: " + result.getResult().getUrl());
                showResult(result.getResult());
            }


            @Override
            protected GNDMSResponseHeader setupContext( final GNDMSResponseHeader context ) {

                context.addMyProxyToken( "C3GRID", myProxyLogin, myProxyPasswd );
                return context;
            }
        };
        return etfc;
    }


    private void showResult( final Specifier<Void> result ) {

        SliceClient sliceClient = createBean( SliceClient.class );
        sliceClient.setServiceURL( gorfxEpUrl );
        final ResponseEntity<List<FileStats>> listResponseEntity =
                sliceClient.listFiles( result.getUriMap().get( UriFactory.SUBSPACE ),
                        result.getUriMap().get( UriFactory.SLICE_KIND ),
                        result.getUriMap().get( UriFactory.SLICE ), dn );

        if ( HttpStatus.OK.equals( listResponseEntity.getStatusCode() ) ) {
            final List<FileStats> fileStats = listResponseEntity.getBody();
            System.out.println( "Result slice contains " + fileStats.size() + " files" );
            for( FileStats fileStat: fileStats ) {
                System.out.println( fileStat );
                System.out.println( "Execute: " );
                System.out.println( "  curl " +
                        "--cert ~/.globus/usercert.pem --key ~/.globus/userkey.pem " +
                        "--CApath /etc/grid-security/certificates " +
                        "-H \"DN:" + dn + "\" " + result.getUrl() + "/"
                        + fileStat.path.replace( '/',  '_' ) );
                System.out.println( "to download this file" );
                System.out.println();
            }
        }
    }


    protected void normalRun() throws Exception {

        Quote quote = loadAndPrintDesiredQuote();
        System.out.println( "Performing normal run!!" );
        etfc.execTF( getNormalOrder(), dn, true, quote, UUID.randomUUID().toString() );
        System.out.println( "DONE\n" );
    }


    protected DmsStageInOrder loadOrderFromProps( final String orderPropFilename )
            throws IOException
    {

        final @NotNull Properties properties = loadOrderProps( orderPropFilename );

        final DmsStageInOrderPropertyReader reader =
                new DmsStageInOrderPropertyReader( properties );
        reader.begin();
        reader.read();
        return  reader.getProduct();
    }


    protected void printOrder( final DmsStageInOrder order ) throws IOException {
        System.out.println("# Staging request");
        DmsStageInOrderConverter orderConverter =
                new DmsStageInOrderConverter( new DmsStageInOrderStdoutWriter(),
                        order );
        orderConverter.convert();
    }


    protected void failingRun() {

    }


    /**
     * Delviers the order which should be used for #normalRun.
     *
     * @return an order
     */
    public Order getNormalOrder() {

        if( normalOrder == null )
            try {
                normalOrder = loadOrderFromProps( orderPropFile );
            } catch ( IOException e ) {
                throw new RuntimeException( e );
            }

        return normalOrder;
    }
}
