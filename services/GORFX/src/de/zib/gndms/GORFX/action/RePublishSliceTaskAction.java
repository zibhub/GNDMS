package de.zib.gndms.GORFX.action;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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
import de.zib.gndms.model.gorfx.types.RePublishSliceORQ;
import de.zib.gndms.model.gorfx.types.RePublishSliceResult;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.model.gorfx.AbstractTask;
import de.zib.gndms.model.gorfx.SubTask;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 11.11.2008, Time: 13:49:57
 */
public class RePublishSliceTaskAction extends ORQTaskAction<RePublishSliceORQ> {

    @NotNull
    protected Class<RePublishSliceORQ> getOrqClass() {
        return RePublishSliceORQ.class;
    }


    @SuppressWarnings( { "ThrowableInstanceNeverThrown" } )
    @Override
    protected void onInProgress( @NotNull AbstractTask model ) {

        SubTask st = new SubTask( model );
        try {
            st.setId( getUUIDGen().nextUUID() );
	        final EntityManager em = getEmf().createEntityManager();
	        st.fromTask(em, model );
            st.setTerminationTime( model.getTerminationTime() );

            InterSliceTransferTaskAction ista = new InterSliceTransferTaskAction( em, st );
            ista.setClosingEntityManagerOnCleanup( true );
            ista.setLog( getLog() );
            ista.call( );

            // trigger script execution in target space
            // use gsissh?

            if( st.getState().equals( TaskState.FINISHED ) )
                finish( new RePublishSliceResult( getOrq().getDestinationSlice() ) );
            else
                fail( (RuntimeException) st.getData() );

        } catch ( RuntimeException e ) {
            honorOngoingTransit( e );
        } catch ( Exception e ) {
            fail( new RuntimeException( e ) );
        }
    }


    @Override
    public void cleanUpOnFail( @NotNull AbstractTask model ) {
        try{
            DSpaceBindingUtils.destroySlice( getOrq().getDestinationSlice() );
        } catch( Exception e ) {
            // todo do something usefull with this exception
            e.printStackTrace( );
        }
        super.cleanUpOnFail( model );
    }
}
