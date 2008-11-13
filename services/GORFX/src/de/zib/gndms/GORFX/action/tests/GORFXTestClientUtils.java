package de.zib.gndms.GORFX.action.tests;

import types.*;
import org.joda.time.DateTime;
import org.globus.wsrf.encoding.DeserializationException;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.apache.axis.message.addressing.EndpointReferenceType;

import java.rmi.RemoteException;

import de.zib.gndms.GORFX.context.client.TaskClient;
import de.zib.gndms.GORFX.offer.client.OfferClient;
import de.zib.gndms.GORFX.ORQ.client.ORQClient;
import de.zib.gndms.GORFX.client.GORFXClient;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQPropertyReader;
import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.typecon.common.type.ProviderStageInORQXSDTypeWriter;
import de.zib.gndms.dspace.slice.client.SliceClient;

/**
 * Some helper methods for common tasks at testing.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 06.11.2008, Time: 17:08:37
 */
public class GORFXTestClientUtils {


    public static OfferExecutionContractT newContract( ) {

        final OfferExecutionContractT xsdOfferContract = new OfferExecutionContractT();

        xsdOfferContract.setExecutionLikelyUntil(new DateTime().plusDays(1).toGregorianCalendar());
        xsdOfferContract.setResultValidUntil(new DateTime().plusDays(2).toGregorianCalendar());
        xsdOfferContract.setIfDecisionBefore(new DateTime().plusHours(1).toGregorianCalendar());
        xsdOfferContract.setConstantExecutionTime( false );

        return xsdOfferContract;
    }


    /**
     * Waits for a task to finish or fail.
     *
     * @param tcnt The client for the task.
     * @param sleep The duration beetween checks (in msec)
     * @return Flase if the execution fails.
     */
    public static boolean waitForFinish( TaskClient tcnt, int sleep ) throws RemoteException, DeserializationException {

        TaskStatusT state = TaskStatusT.unknown;
        boolean finished;
        boolean failed;

        do {
            state = tcnt.getTaskState();
            failed = state.equals( TaskStatusT.failed );
            finished = state.equals( TaskStatusT.finished );

            System.out.println("Waiting for staging to finish...");
            try {
                Thread.sleep( sleep );
            }
            catch (InterruptedException e) {
                // intentionally
            }
        }
        while (! (failed || finished ) );

        return finished;
    }


    /**
     * Executes a single stagin request and waits for it to complete.
     * @param uri The uri of the GORFX service
     * @param props The name of the staging request property file
     * @param ctx The context of the request.
     * @param con The desired contract for the execution.
     * @param slp The wainting for completion delay.
     * @return The created slice.
     *
     * @throws Exception If anything goes wrong.
     */
    public static SliceReference doStageIn( String uri,
                                      String props,
                                      ContextT ctx,
                                      OfferExecutionContractT con,
                                      int slp ) throws Exception
    {

        // read stagin orq from file
        final ProviderStageInORQ psorq = ProviderStageInORQPropertyReader.readFromFile( props );
        // convert to xsd type
        final ProviderStageInORQT psorqt = ProviderStageInORQXSDTypeWriter.write( psorq );

        final ProviderStageInResultT result =
            commonTaskExecution( uri, psorqt, ctx, con, slp,  ProviderStageInResultT.class );
        SliceReference sr =  ( SliceReference ) ObjectDeserializer.toObject( result.get_any()[0], SliceReference.class ) ;
        if( sr != null ) {
            SliceClient sliceClient = new SliceClient( sr.getEndpointReference() );
            System.out.println( "Created slice at: " );
            System.out.println( sliceClient.getSliceLocation() );
            return sr;
        }

        throw new IllegalStateException( "No slice received." );
    }


    /**
     * Executes a single task request and waits for it to complete.
     *
     * @param uri The uri of the GORFX service
     * @param orq The Reques itself.
     * @param ctx The context of the request.
     * @param con The desired contract for the execution.
     * @param slp The wainting for completion delay.
     * @param resClazz The class of the task result.
     * @return The created slice.
     *
     * @throws Exception If anything goes wrong.
     */
    public static <M extends DynamicOfferDataSeqT>
        M commonTaskExecution( String uri,
                               DynamicOfferDataSeqT orq,
                               ContextT ctx,
                               OfferExecutionContractT con,
                               int slp,
                               Class<M> resClazz
                               ) throws Exception
    {

        // create gorfx client and retrieve orq
        final GORFXClient gcnt = new GORFXClient( uri );
        EndpointReferenceType epr = gcnt.createOfferRequest( orq, ctx );

        // create orq client and request offer
        final ORQClient orqcnt = new ORQClient( epr );
        epr = orqcnt.getOfferAndDestroyRequest( con , ctx );

        // create offer client and accept it
        final OfferClient ofcnt = new OfferClient( epr );
        epr = ofcnt.accept( );

        // create task resource an wait for completion
        final TaskClient tcnt = new TaskClient( epr );

        boolean finished = GORFXTestClientUtils.waitForFinish( tcnt, slp );

        if (finished)
            return tcnt.getExecutionResult( resClazz );
        else {
            final TaskExecutionFailure fail = tcnt.getExecutionFailure();
            TaskExecutionFailureImplementationFault tefif = fail.getImplementationFault();
            System.out.println( "message:       " + tefif.getMessage( ) );
            System.out.println( "faultClass:    " + tefif.getFaultClass( ) );
            System.out.println( "faultTrace:    " + tefif.getFaultTrace( ) );
            System.out.println( "faultLocation: " + tefif.getFaultLocation( ) );
            throw new RuntimeException(fail.toString());
        }
    }
}
