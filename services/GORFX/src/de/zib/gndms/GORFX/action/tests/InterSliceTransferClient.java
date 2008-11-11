package de.zib.gndms.GORFX.action.tests;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.joda.time.DateTime;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.oasis.wsrf.properties.GetResourcePropertyResponse;
import de.zib.gndms.kit.application.AbstractApplication;
import de.zib.gndms.typecon.common.GORFXTools;
import de.zib.gndms.typecon.common.type.ProviderStageInORQXSDTypeWriter;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQPropertyReader;
import de.zib.gndms.GORFX.client.GORFXClient;
import de.zib.gndms.GORFX.ORQ.client.ORQClient;
import de.zib.gndms.GORFX.offer.client.OfferClient;
import de.zib.gndms.GORFX.context.client.TaskClient;
import de.zib.gndms.GORFX.context.common.TaskConstants;
import de.zib.gndms.dspace.slice.client.SliceClient;
import types.*;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 18:28:22
 */
public class InterSliceTransferClient extends AbstractApplication {

    public static int MILLIS = 500;
    
    // do provider stagin
    // create new slice on different host
    // transfer staged data to created slice

    @Option( name="-uri", required=true, usage="URI of the gorfx service" )
    public String uri;
    @Option( name="-duri", required=true, usage="Destination subspace uri" )
    public String duri;
    @Option( name="-props", required=true, usage="properties for the staging request" )
    public String props;

    final private ContextT xsdContext = new ContextT();

    public void run() throws Exception {

        // Create reusable context with pseudo DN
        final ContextT xsdContext = new ContextT();
        final ContextTEntry entry =
            GORFXTools.createContextEntry("Auth.DN", "any_dn" );
        xsdContext.setEntry(new ContextTEntry[] { entry });

    }


    private SliceReference doStageIn( ) throws Exception{

        // read stagin orq from file
        final ProviderStageInORQ psorq = ProviderStageInORQPropertyReader.readFromFile( props );
        // convert to xsd type
        final ProviderStageInORQT psorqt = ProviderStageInORQXSDTypeWriter.write( psorq );

        // create gorfx client and retrieve orq
        final GORFXClient gcnt = new GORFXClient( uri );
        EndpointReferenceType epr = gcnt.createOfferRequest( psorqt, xsdContext );

        // create orq client and request offer
        final ORQClient orqcnt = new ORQClient( epr );
        epr = orqcnt.getOfferAndDestroyRequest( newContract(), xsdContext );

        // create offer client and accept it
        final OfferClient ofcnt = new OfferClient( epr );
        epr = ofcnt.accept( );

        // create task resource an wait for completion
        final TaskClient tcnt = new TaskClient( epr );

        boolean finished;
        boolean failed;

        do {
            TaskStatusT state = tcnt.getTaskState();
            failed = state.equals( TaskStatusT.failed );
            finished = state.equals( TaskStatusT.finished );

            System.out.println("Waiting for staging to finish...");
            try {
                Thread.sleep(MILLIS);
            }
            catch (InterruptedException e) {
                // intentionally
            }
        }
        while (! (failed || finished ) );


        // Write results to console
        if (finished) {
            final ProviderStageInResultT result = tcnt.getExecutionResult( ProviderStageInResultT.class );
            System.out.println(result);
            SliceReference sr =  ( SliceReference ) ObjectDeserializer.toObject( result.get_any()[0], SliceReference.class ) ;
            if( sr != null ) {
                SliceClient sliceClient = new SliceClient( sr.getEndpointReference() );
                System.out.println( "Created slice at: " );
                System.out.println( sliceClient.getSliceLocation() );
                return sr;
            }
        } else {
            final TaskExecutionFailure fail = tcnt.getExecutionFailure();
            throw new RuntimeException(fail.toString());
        }

        throw new IllegalStateException( "No sliece received." );
    }

    public static OfferExecutionContractT newContract( ) {

        final OfferExecutionContractT xsdOfferContract = new OfferExecutionContractT();
        
        xsdOfferContract.setExecutionLikelyUntil(new DateTime().plusDays(1).toGregorianCalendar());
        xsdOfferContract.setResultValidUntil(new DateTime().plusDays(2).toGregorianCalendar());
        xsdOfferContract.setIfDecisionBefore(new DateTime().plusHours(1).toGregorianCalendar());
        xsdOfferContract.setConstantExecutionTime( false );

        return xsdOfferContract;
    }


    public static void main( String[] args ) throws Exception {

        InterSliceTransferClient cnt = new InterSliceTransferClient();
        cnt.run( args );
    }
}