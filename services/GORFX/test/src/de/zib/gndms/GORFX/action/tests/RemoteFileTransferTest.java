package de.zib.gndms.GORFX.action.tests;

import de.zib.gndms.GORFX.ORQ.client.ORQClient;
import de.zib.gndms.GORFX.client.GORFXClient;
import de.zib.gndms.GORFX.context.client.TaskClient;
import de.zib.gndms.GORFX.offer.client.OfferClient;
import de.zib.gndms.GORFX.offer.common.OfferConstants;
import de.zib.gndms.comserv.delegation.DelegationAux;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.types.FileTransferORQ;
import de.zib.gndms.model.gorfx.types.io.ContractConverter;
import de.zib.gndms.model.gorfx.types.io.FileTransferORQConverter;
import de.zib.gndms.model.gorfx.types.io.FileTransferORQPropertyReader;
import de.zib.gndms.typecon.common.ContractStdoutWriter;
import de.zib.gndms.typecon.common.type.ContextXSDTypeWriter;
import de.zib.gndms.typecon.common.type.ContractXSDReader;
import de.zib.gndms.typecon.common.type.FileTransferORQXSDTypeWriter;
import org.apache.axis.message.MessageElement;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.gsi.GlobusCredential;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.oasis.wsrf.properties.GetResourcePropertyResponse;
import types.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * WARNING ths class may uses obsolet operation and should be checked befor running it
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 24.10.2008, Time: 09:48:52
 */
public class RemoteFileTransferTest {

    public static void main( String[] args ) throws Exception {

        String proxyFile = "/tmp/x509up_u1000";
        if( args.length != 2 ) {
            usage( );
            System.exit( 1 );
        }


        // read property file
        InputStream f = new FileInputStream( args[1] );
        Properties prop = new Properties( );
        prop.load( f );
        f.close( );

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
        String gorfxEpUrlParam  = args[0];
        GORFXClient gc = new GORFXClient( gorfxEpUrlParam );

        // with delegation
        String delfac = DelegationAux.createDelationAddress( gorfxEpUrlParam );
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

        do {
            stat = tc.getTaskState();
            System.out.println( "task state: " + stat );
            failed = stat.equals( TaskStatusT.failed );
            finished = stat.equals( TaskStatusT.finished );
            Thread.sleep( 500 );
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
