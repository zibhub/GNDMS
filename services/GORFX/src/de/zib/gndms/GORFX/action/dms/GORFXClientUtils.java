package de.zib.gndms.GORFX.action.dms;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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



import de.zib.gndms.GORFX.ORQ.client.ORQClient;
import de.zib.gndms.GORFX.client.GORFXClient;
import de.zib.gndms.GORFX.context.client.TaskClient;
import de.zib.gndms.GORFX.offer.client.OfferClient;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.gsi.GlobusCredential;
import org.apache.log4j.Logger;
import org.globus.wsrf.encoding.DeserializationException;
import org.joda.time.DateTime;
import types.*;

import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.rmi.RemoteException;
import java.util.Calendar;

/**
 * Some helper methods for common tasks at testing.
 *
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 06.11.2008, Time: 17:08:37
 */
public class GORFXClientUtils {

    private static Logger log = Logger.getLogger( GORFXClientUtils.class );

    /**
     * Waits for a task to finish or fail.
     *
     * @param tcnt The client for the task.
     * @param sleep The duration beetween checks (in msec)
     * @return Flase if the execution fails.
     */
    public static boolean waitForFinish( TaskClient tcnt, int sleep ) throws RemoteException, DeserializationException {

        TaskStatusT state = TaskStatusT.unknown;
        boolean finished=false;
        boolean failed=false;
        int exceptions=0;
        final int min = 15;
        final int delay = min * 60 * 1000;

        long lastException = Calendar.getInstance().getTimeInMillis() - 2* delay;

        do {
            try {
                state = checkState( tcnt );
                failed = state.equals( TaskStatusT.failed );
                finished = state.equals( TaskStatusT.finished );
                if( failed || finished )
                    break;
                else
                    Thread.sleep( sleep );

            } catch( SocketTimeoutException e ) {

                long now = Calendar.getInstance().getTimeInMillis();
                if( now - lastException > delay ) {
                    log.debug( "Resetting exception count from: " + exceptions );
                    exceptions = 1;
                    lastException = now;
                }


                if( exceptions < 6 )
                    log.debug( "Cought: "+e.toString()+" No. " + ++exceptions, e  );
                else
                    throw new RuntimeException( "Cought " + exceptions + " exceptions. ABORTING", e );

            } catch (InterruptedException e) {
                // intentionally
            }
        }
        while ( true );

        return finished;
    }


    private static TaskStatusT checkState( TaskClient tc ) throws SocketTimeoutException, RemoteException, DeserializationException {
        return tc.getTaskState();
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
                               OfferExecutionContractT con,
                               GlobusCredential proxy
                               ) throws Exception
    {

        // create gorfx client and retrieve orq
        final GORFXClient gcnt = new GORFXClient( uri );
        if( proxy != null ) gcnt.setProxy( proxy );
        EndpointReferenceType epr = gcnt.createOfferRequest( orq, ctx );

        // create orq client and request offer
        final ORQClient orqcnt = new ORQClient( epr );
        if( proxy != null ) orqcnt.setProxy( proxy );
        epr = orqcnt.getOfferAndDestroyRequest( con , ctx );

        // create offer client and accept it
        final OfferClient ofcnt = new OfferClient( epr );
        if( proxy != null ) ofcnt.setProxy( proxy );
        return ofcnt.accept( );
    }
    

    public static OfferExecutionContractT newContract( ) {

	    final OfferExecutionContractT xsdOfferContract = new OfferExecutionContractT();

	    xsdOfferContract.setIfDecisionBefore( new DateTime().plusHours(1).toGregorianCalendar() );
		final FutureTimeT execLikelyUntil = new FutureTimeT();
		execLikelyUntil.setTime(new DateTime().plusDays(1).toGregorianCalendar());
	    xsdOfferContract.setExecutionLikelyUntil( execLikelyUntil );
		final FutureTimeT resultValidity = new FutureTimeT();
		resultValidity.setTime(new DateTime().plusDays(2).toGregorianCalendar());
	    xsdOfferContract.setResultValidUntil( resultValidity );

        return xsdOfferContract;
    }


    public static String taskExecutionFailureToString( TaskExecutionFailure tef ) {

        if( tef == null )
            return "NULL";


        StringWriter sw = new StringWriter( );
        TaskExecutionFailureImplementationFault tefif = tef.getImplementationFault();
        sw.write( "message:       " + tefif.getMessage( ) + "\n");
        sw.write( "faultClass:    " + tefif.getFaultClass( ) + "\n");
        sw.write( "faultTrace:    " + tefif.getFaultTrace( ) + "\n");
        sw.write( "faultLocation: " + tefif.getFaultLocation( ) );

        return sw.toString();
    }
}
