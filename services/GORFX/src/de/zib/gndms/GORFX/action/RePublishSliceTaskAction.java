package de.zib.gndms.GORFX.action;

import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.model.gorfx.types.RePublishSliceORQ;
import de.zib.gndms.model.gorfx.types.RePublishSliceResult;
import de.zib.gndms.model.gorfx.AbstractTask;
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


    @Override
    protected void onInProgress( @NotNull AbstractTask model ) {

        try {
            InterSliceTransferTaskAction ista = new InterSliceTransferTaskAction( getEntityManager(), model );
            ista.setClosingEntityManagerOnCleanup( false );
            ista.call( );
        } catch ( TransitException e ) {
            if(! isFinishedTransition( e ) )
                throw e;
        }

        // trigger script execution in target space
        // use gsissh?

        finish( new RePublishSliceResult( getOrq().getDestinationSlice() ) );
    }


    @Override
    protected void onFailed( @NotNull AbstractTask model ) {
        super.onFailed( model );
        try{
            DSpaceBindingUtils.destroySlice( getOrq().getDestinationSlice() );
        } catch( Exception e ) {
            // todo do something usefull with this exception
            e.printStackTrace( );
        }
    }
}
