package de.zib.gndms.GORFX.action.dms;

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
public class GORFXClientUtils {


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
     * Executes a single task request and waits for it to complete.
     *
     * @param uri The uri of the GORFX service
     * @param orq The Reques itself.
     * @param ctx The context of the request.
     * @param con The desired contract for the execution.
     * @return An epr to the created task resource.
     *
     * @throws Exception If anything goes wrong.
     */
    public static <M extends DynamicOfferDataSeqT>
        EndpointReferenceType commonTaskPreparation( String uri,
                               DynamicOfferDataSeqT orq,
                               ContextT ctx,
                               OfferExecutionContractT con
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
        return ofcnt.accept( );
    }
    

    public static OfferExecutionContractT newContract( ) {

        final OfferExecutionContractT xsdOfferContract = new OfferExecutionContractT();

        xsdOfferContract.setExecutionLikelyUntil(new DateTime().plusDays(1).toGregorianCalendar());
        xsdOfferContract.setResultValidUntil(new DateTime().plusDays(2).toGregorianCalendar());
        xsdOfferContract.setIfDecisionBefore(new DateTime().plusHours(1).toGregorianCalendar());
        xsdOfferContract.setConstantExecutionTime( false );

        return xsdOfferContract;
    }
}
