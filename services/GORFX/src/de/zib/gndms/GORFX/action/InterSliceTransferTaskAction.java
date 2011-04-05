package de.zib.gndms.GORFX.action;

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



import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.logic.model.gorfx.FileTransferTaskAction;
import de.zib.gndms.model.gorfx.types.InterSliceTransferORQ;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.model.gorfx.AbstractTask;
import de.zib.gndms.model.gorfx.SubTask;
import org.globus.gsi.GlobusCredential;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 17:45:47
 */
public class InterSliceTransferTaskAction extends ORQTaskAction<InterSliceTransferORQ> {


    public InterSliceTransferTaskAction() {
    }


    public InterSliceTransferTaskAction( @NotNull EntityManager em, @NotNull AbstractTask model ) {
        super( em, model );
    }


    public InterSliceTransferTaskAction( @NotNull EntityManager em, @NotNull String pk ) {
        super( em, pk );
    }


    @SuppressWarnings( { "ThrowableInstanceNeverThrown" } )
    protected void onInProgress( @NotNull AbstractTask model ) {

        try {
            InterSliceTransferORQCalculator.checkURIs( getOrq( ), (GlobusCredential)
                getCredentialProvider().getCredential() );
        } catch ( Exception e ) {
            fail( new RuntimeException( e.getMessage( ), e ) );
        }

        SubTask st = new SubTask( model );
        try {
	        final EntityManager em = getEntityManager();

            st.setId( getUUIDGen().nextUUID() );
            st.fromTask( em, model );
            st.setTerminationTime( model.getTerminationTime() );

	        FileTransferTaskAction fta = new FileTransferTaskAction(em, st );
            fta.setCredentialProvider( getCredentialProvider() );
            fta.setClosingEntityManagerOnCleanup( false );

            fta.setLog( getLog() );
            fta.call( );
            if( st.getState().equals( TaskState.FINISHED ) )
                finish( st.getData( ) );
            else
                failFrom( (RuntimeException) st.getData() );

        } catch ( TransitException e ) {
            honorOngoingTransit( e );
        } catch ( Exception e ) {
            /*
            if( isFinishedException( e ) )
                finish( st.getData( ) );
            else if( isFailedTransition( e ) )
                fail( (RuntimeException) st.getData() );
            else
            */
            failFrom( e );
        }
    }


    @NotNull
    protected Class<InterSliceTransferORQ> getOrqClass() {
        return InterSliceTransferORQ.class;
    }
}
