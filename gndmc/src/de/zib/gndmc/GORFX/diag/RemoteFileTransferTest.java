package de.zib.gndmc.GORFX.diag;

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



import de.zib.gndmc.GORFX.constants.OfferConstants;
import de.zib.gndms.GORFX.ORQ.client.ORQClient;
import de.zib.gndms.GORFX.client.GORFXClient;
import de.zib.gndms.GORFX.context.client.TaskClient;
import de.zib.gndms.GORFX.offer.client.OfferClient;
import de.zib.gndms.gritserv.delegation.DelegationAux;
import de.zib.gndms.kit.application.AbstractApplication;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.types.FileTransferORQ;
import de.zib.gndms.model.gorfx.types.io.ContractConverter;
import de.zib.gndms.model.gorfx.types.io.FileTransferORQConverter;
import de.zib.gndms.model.gorfx.types.io.FileTransferORQPropertyReader;
import de.zib.gndms.gritserv.typecon.ContractStdoutWriter;
import de.zib.gndms.gritserv.typecon.types.ContextXSDTypeWriter;
import de.zib.gndms.gritserv.typecon.types.ContractXSDReader;
import de.zib.gndms.gritserv.typecon.types.FileTransferORQXSDTypeWriter;
import org.apache.axis.message.MessageElement;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.gsi.GlobusCredential;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.kohsuke.args4j.Option;
import org.oasis.wsrf.properties.GetResourcePropertyResponse;
import types.*;

import javax.management.RuntimeMBeanException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * WARNING ths class may uses obsolet operation and should be checked befor running it
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 24.10.2008, Time: 09:48:52
 */
public class RemoteFileTransferTest extends AbstractApplication {

    @Option( name="-uri", required=true, usage="URL of GORFX-Endpoint", metaVar="URI" )
    protected String gorfxEpUrl;
    @Option( name="-props", required=true, usage="The property file with the file transfer request properties. - To read from STDIN" )
    protected String props;
    @Option( name="-cancel", required=false, usage="Call cancel on the task resource after N millis" )
    protected Long cancelDelay = null; // cancel delay in ms
    @Option( name="-uid", required=true, usage="The user id, stupid java !$§@!*" )
    protected String uid = null; // cancel delay in ms

    private static final long POLLING_DELAY = 500;
    private boolean cancel = false;


    public static void main( String[] args ) throws Exception {

        RemoteFileTransferTest cnt = new RemoteFileTransferTest();
        cnt.run( args );
    }



    @Override
    public void run() throws Exception {

        String proxyFile = "/tmp/x509up_u" + uid;


        // read property file
        InputStream is;
        if ( props.trim().equals( "-" ) ) {
            System.out.println( "Reading props von stdin" );
            is = System.in;
            System.out.println( "done" );
        } else
            is = new FileInputStream( props );
        Properties prop = new Properties( );
        prop.load( is );
        is.close( );

        // create orq from properties
        FileTransferORQPropertyReader pr = new FileTransferORQPropertyReader( prop );
        pr.performReading();
        FileTransferORQ orq = pr.getProduct();

        // transform orq to axis orq for transfer
        FileTransferORQXSDTypeWriter writer = new FileTransferORQXSDTypeWriter( );
        FileTransferORQConverter conv = new FileTransferORQConverter( writer, orq );
        conv.convert();
        FileTransferORQT orqArgs = writer.getProduct();

        // extract context from orq
        ContextT ctx = ContextXSDTypeWriter.writeContext( orq.getActContext() );

        // Create gorfx client and request offer request.
        GORFXClient gc = new GORFXClient( gorfxEpUrl );

        // with delegation
        String delfac = DelegationAux.createDelegationAddress( gorfxEpUrl );
        GlobusCredential credential = DelegationAux.findCredential( proxyFile );
        EndpointReferenceType epr = DelegationAux.createProxy( delfac, credential );
        DelegationAux.addDelegationEPR( ctx, epr );
        gc.setProxy( credential );

        // Create ORQ via GORFX
        EndpointReferenceType orqepr = gc.createOfferRequest( orqArgs, ctx );

        // Create orq client and request offer
        ORQClient orqc = new ORQClient( orqepr );
        EndpointReferenceType oepr = orqc.getOfferAndDestroyRequest(  null, ctx );

        // create offer client check offer and accept it.
        OfferClient oc = new OfferClient( oepr );
        GetResourcePropertyResponse resp =  oc.getResourceProperty( OfferConstants.OFFEREXECUTIONCONTRACT );
        OfferExecutionContractT con =
            (OfferExecutionContractT) ObjectDeserializer.toObject( resp.get_any( )[0], OfferExecutionContractT.class );
        showContract( con );
        EndpointReferenceType tepr = oc.accept();

        // create Task client and check status;
        TaskClient tc = new TaskClient( tepr );

        TaskStatusT stat;
        boolean finished;
        boolean failed;
        cancel = cancelDelay != null;
        int count = 0;

        do {
            if ( cancel && ( count++ * POLLING_DELAY >  cancelDelay ) ) {
                System.out.println( "destroying client" );
                tc.destroy();
                System.out.println( "done" );
                throw new RuntimeException( "Task destroyed" );
            }

            stat = tc.getTaskState();
            System.out.println( "task state: " + stat );
            failed = stat.equals( TaskStatusT.failed );
            finished = stat.equals( TaskStatusT.finished );
            Thread.sleep( POLLING_DELAY );
        }
        while (! (failed || finished));

        // show result or failure type
        if( finished ) {
            FileTransferResultT ftr = tc.getExecutionResult( FileTransferResultT.class );
         //   String[] fls = (String[]) ObjectDeserializer.toObject( ftr.get_any(), String.class );
            MessageElement[] mes = ftr.get_any( );
            ArrayList<String> al = new ArrayList( mes.length );
            for( int i=0; i < mes.length; ++i )
                al.add( (String) mes[i].getObjectValue( String.class ) );
            String[] fls = al.toArray( new String[al.size()] );
            System.out.println( "Copied files: " );
            for( String s: Arrays.asList( fls ) ) {
                System.out.println( "\t" + s );
            }
            System.exit( 0 );
        }

        if( failed ) {
            TaskExecutionFailure tef  = tc.getExecutionFailure();
            TaskExecutionFailureImplementationFault tefif = tef.getImplementationFault();
            System.out.println( "message:       " + tefif.getMessage( ) );
            System.out.println( "faultClass:    " + tefif.getFaultClass( ) );
            System.out.println( "faultTrace:    " + tefif.getFaultTrace( ) );
            System.out.println( "faultLocation: " + tefif.getFaultLocation( ) );
            System.exit( 1 );
        }

    }


    private static void usage() {

        System.out.println( "usage: " + RemoteFileTransferTest.class.getName() + " <url> <props>"  );
        System.out.println( "With: ");
        System.out.println( "<url>    The url of the GORFX service");
        System.out.println( "<prop>   The property file with the file transfer request properties.");
    }


    private static void showContract( OfferExecutionContractT con ) {

        TransientContract c = ContractXSDReader.readContract( con );
        ContractStdoutWriter writer = new ContractStdoutWriter();
        ContractConverter conv = new ContractConverter( writer, c );
        conv.convert( );
    }


}
