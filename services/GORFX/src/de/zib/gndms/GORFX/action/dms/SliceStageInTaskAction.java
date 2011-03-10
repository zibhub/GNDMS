package de.zib.gndms.GORFX.action.dms;

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



import de.zib.gndms.GORFX.context.client.TaskClient;
import de.zib.gndms.gritserv.util.GlobusCredentialProvider;

import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.infra.system.SystemHolder;
import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.model.dspace.types.SliceRef;
import de.zib.gndms.model.gorfx.AbstractTask;
import de.zib.gndms.model.gorfx.types.SliceStageInORQ;
import de.zib.gndms.model.gorfx.types.SliceStageInResult;
import de.zib.gndms.gritserv.typecon.types.ProviderStageInORQXSDTypeWriter;
import de.zib.gndms.gritserv.typecon.types.SliceRefXSDReader;
import de.zib.gndms.gritserv.typecon.types.ContextXSDTypeWriter;
import de.zib.gndms.gritserv.typecon.types.ContractXSDTypeWriter;
import de.zib.gndms.stuff.threading.Forkable;
import org.apache.axis.AxisFault;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.NeoSession;
import de.zib.gndms.neomodel.gorfx.NeoTask;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.gsi.GlobusCredential;
import org.jetbrains.annotations.NotNull;
import types.*;

import java.rmi.RemoteException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 11.11.2008, Time: 13:49:57
 */
public class SliceStageInTaskAction extends ORQTaskAction<SliceStageInORQ>
	implements SystemHolder {

    private GNDMSystem system;
    private TaskClient taskClient = null;


    @NotNull
	public GNDMSystem getSystem() {
		return system;
	}


	public void setSystem(@NotNull final GNDMSystem systemParam) {
		system = systemParam;
	}


    @Override
    @NotNull
    public Class<SliceStageInORQ> getOrqClass() {
        return SliceStageInORQ.class;
    }


    @Override
    protected void onInProgress(@NotNull String wid, @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState) throws Exception {

        EndpointReferenceType taskEPR = null;
        final NeoSession session = getDao().beginSession();
        try {
            GlobusCredential gc = GlobusCredentialProvider.class.cast( getCredentialProvider() ).getCredential();
            final NeoTask model = getTask(session);

            if( model.getPayload( ) == null ) {
                SliceStageInORQ orq = (SliceStageInORQ) getORQ();
                String uri = orq.getActGridSiteURI();
                if( uri == null )
                    throw new RuntimeException( "GORFX uri is null" );
                else {
                    ProviderStageInORQT p_orq = ProviderStageInORQXSDTypeWriter.write( orq );
                    ContextT ctx = ContextXSDTypeWriter.writeContext( orq.getActContext() );
                    OfferExecutionContractT con = ContractXSDTypeWriter.write( model.getContract().toTransientContract() );

                    try {
                        taskEPR = GORFXClientUtils.commonTaskPreparation( uri, p_orq, ctx, con, gc );
                    } catch ( AxisFault e ) {
                        String msg = "AxisFault on task preparation\n" ;
                        try {
                            msg += e.dumpToString();
                        } catch ( Exception e2 ) {
                            msg += "No info available";
                        }
                        getLog( ).debug( msg );
						// todo check transitToFail
                        fail( new IllegalStateException( msg ) );
                    }
                    model.setPayload( taskEPR );
 					// transitWithPayload( epr, TaskState.IN_PROGRESS );
                }
            }

            taskEPR = (EndpointReferenceType) model.getData( );

            if( taskEPR == null )
                fail( new IllegalStateException( "commonTaskPrep return ed null epr" ) );

            taskClient = new TaskClient( taskEPR );
            taskClient.setProxy( gc );

            trace( "Starting remote staging.", null );
            // poll every 15 seconds

            Forkable<Boolean> forkable = new Forkable<Boolean>(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return GORFXClientUtils.waitForFinish( taskClient, 15000 );
                }
            });
            forkable.setShouldStop( true );

            boolean finished = false;
            ExecutorService executorService = Executors.newFixedThreadPool( 1 );
            try {
                Future<Boolean> future = executorService.submit(forkable);
                finished = future.get();
            } catch( InterruptedException e ) {
                Thread.interrupted();
                fail( new RuntimeException( e ) );
            }

            if (finished) {
                trace( "Remote staging finished.", null );
                ProviderStageInResultT res =  taskClient.getExecutionResult( ProviderStageInResultT.class );
                SliceReference sk = (SliceReference) res.get_any()[0].getObjectValue( SliceReference.class );
                SliceRef sr = SliceRefXSDReader.read( sk );
                trace( "Remote staging finished. SliceId: " + sr.getResourceKeyValue() + "@"  + sr.getGridSiteId() , null );
                taskClient.destroy( );
                taskClient = null;
                transitWithPayload(new SliceStageInResult(sr), TaskState.FINISHED);
				return;
            } else {
                TaskExecutionFailure f = taskClient.getExecutionFailure();
                try{
                    taskClient.destroy( );
                } catch ( Exception e ) {
                    log.warn( "taskClient destroy throw exception", e );
                }
                taskClient = null;
                String failure = GORFXClientUtils.taskExecutionFailureToString( f );
                trace( "Remote staging failed with: \n"
                    +  GORFXClientUtils.taskExecutionFailureToString( f ), null );
                throw new RuntimeException( f.toString() );
            }
        } 
		finally { session.success(); }
    	super.onInProgress(wid, state, isRestartedTask, altTaskState);
    }


	// todo check if this is still called
    @Override
    public void cleanUpOnFail( @NotNull AbstractTask model ) {
        super.cleanUpOnFail( model );
        if( taskClient != null )
            try {
                getLog( ).warn( "Destroying client" );
                taskClient.destroy( );
            } catch ( RemoteException e ) {
                getLog( ).warn( "Exception on cleanup", e );
            }
    }
}
