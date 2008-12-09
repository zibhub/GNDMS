package de.zib.gndms.GORFX.action;

import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.model.gorfx.types.RePublishSliceORQ;
import de.zib.gndms.model.gorfx.types.RePublishSliceResult;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.model.gorfx.AbstractTask;
import de.zib.gndms.model.gorfx.SubTask;
import org.jetbrains.annotations.NotNull;

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
            st.fromTask( getEntityManager(), model );
            st.setTerminationTime( model.getTerminationTime() );

            InterSliceTransferTaskAction ista = new InterSliceTransferTaskAction( getEntityManager(), st );
            ista.setClosingEntityManagerOnCleanup( false );
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
