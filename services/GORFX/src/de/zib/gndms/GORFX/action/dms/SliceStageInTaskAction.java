package de.zib.gndms.GORFX.action.dms;

import de.zib.gndms.GORFX.context.client.TaskClient;
import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.model.dspace.types.SliceRef;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.types.SliceStageInORQ;
import de.zib.gndms.model.gorfx.types.SliceStageInResult;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.typecon.common.type.ContextXSDTypeWriter;
import de.zib.gndms.typecon.common.type.ContractXSDTypeWriter;
import de.zib.gndms.typecon.common.type.ProviderStageInORQXSDTypeWriter;
import de.zib.gndms.typecon.common.type.SliceRefXSDReader;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.jetbrains.annotations.NotNull;
import types.*;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 11.11.2008, Time: 13:49:57
 */
public class SliceStageInTaskAction extends ORQTaskAction<SliceStageInORQ> {

    @NotNull
    protected Class<SliceStageInORQ> getOrqClass() {
        return SliceStageInORQ.class;
    }


    @SuppressWarnings( { "ThrowableInstanceNeverThrown" } )
    @Override
    protected void onInProgress( @NotNull Task model ) {

        try {
            EndpointReferenceType epr;
            if( model.getData( ) == null ) {
                String uri = ( (SliceStageInORQ) model.getOrq()).getGridSiteURI();
                if( uri == null )
                    fail ( new RuntimeException( "GORFX uri is null" ) );
                else {
                    ProviderStageInORQT p_orq = ProviderStageInORQXSDTypeWriter.write( getOrq() );
                    ContextT ctx = ContextXSDTypeWriter.writeContext( getOrq().getContext() );
                    OfferExecutionContractT con = ContractXSDTypeWriter.fromContract( model.getContract() );
                    
                    epr = GORFXClientUtils.commonTaskPreparation( uri, p_orq, ctx, con  );
                    model.setData( epr );
                    transitToState( TaskState.IN_PROGRESS );
                }
            }

            epr = (EndpointReferenceType) model.getData( );

            TaskClient cnt = new TaskClient( epr );
            boolean finished = GORFXClientUtils.waitForFinish( cnt, 1000 );

            if (finished) {
                ProviderStageInResultT res =  cnt.getExecutionResult( ProviderStageInResultT.class );
                SliceReference sk = (SliceReference) res.get_any()[0].getObjectValue( SliceReference.class );
                SliceRef sr = SliceRefXSDReader.read( sk );
                finish( new SliceStageInResult( sr ) );
            } else {
                TaskExecutionFailure f = cnt.getExecutionFailure();
                fail( new RuntimeException( f.toString() ) );
            }
        } catch( RuntimeException e ) {
            honorOngoingTransit( e );
            boxException( e );
        } catch ( Exception e ) {
            boxException( e );
        }
    }


    @SuppressWarnings( { "ThrowableInstanceNeverThrown" } )
    private void boxException( Exception e ) {
        fail( new RuntimeException( e.getMessage(), e ) );
    }
}
