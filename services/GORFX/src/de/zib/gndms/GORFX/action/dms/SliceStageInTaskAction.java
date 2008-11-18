package de.zib.gndms.GORFX.action.dms;

import de.zib.gndms.GORFX.action.InterSliceTransferTaskAction;
import de.zib.gndms.dspace.client.DSpaceClient;
import de.zib.gndms.dspace.slice.client.SliceClient;
import de.zib.gndms.dspace.subspace.client.SubspaceClient;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.logic.model.gorfx.c3grid.AbstractProviderStageInAction;
import de.zib.gndms.model.dspace.types.SliceRef;
import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.model.gorfx.OfferType;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.types.*;
import de.zib.gndms.typecon.common.type.ProviderStageInResultXSDTypeWriter;
import de.zib.gndms.typecon.common.type.SliceRefXSDReader;
import org.jetbrains.annotations.NotNull;

import javax.xml.namespace.QName;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 11.11.2008, Time: 13:49:57
 */
public class SliceStageInTaskAction extends ORQTaskAction<SliceStageInORQ> {

    private static GNDMSystem system;

    @NotNull
    protected Class<SliceStageInORQ> getOrqClass() {
        return SliceStageInORQ.class;
    }


    @Override
    protected void onInProgress( @NotNull Task model ) {

        // perform staging
        try {
            AbstractProviderStageInAction psa = (AbstractProviderStageInAction)
                getSystem().getInstanceDir().getTaskAction( getEntityManager(), GORFXConstantURIs.PROVIDER_STAGE_IN_URI );

            psa.initFromModel( getEntityManager(), getModel( ) );
            psa.call( );
        } catch ( TransitException e ) {
            if(! isFinishedException( e ) )
                throw e;
        } catch ( Exception e ) {
            boxException( e );
        }


        SliceRef res_slice = null;
        try {
            // obtain created slice
            ProviderStageInResult psr = ( ProviderStageInResult ) getModel().getData( );
            SliceRef sr = SliceRefXSDReader.fromEPR( ProviderStageInResultXSDTypeWriter.SliceIdToEPR( psr.getSliceKey() ) );


            // create destination slice
            DSpaceClient dsc = new DSpaceClient( getOrq().getGridSiteURI() );


            // todo find destination subspace
            SubspaceClient sscnt = new SubspaceClient( dsc.getSubspace( new QName( "" ) ).getEndpointReference() );

            Contract c = model.getContract( );
            long size = c.getExpectedSize();
            SliceClient tgt = sscnt.createSlice( GORFXConstantURIs.PUBLISH_SLICE_KIND_URI,
                c.getDeadline(), size == 0 ? 1 : size );


            InterSliceTransferORQ ist_orq = new InterSliceTransferORQ( );
            ist_orq.setSourceSlice( sr );
            ist_orq.setDestinationSlice( SliceRefXSDReader.fromEPR( tgt.getEndpointReference() ) );

            Task tsk = new Task( );

            tsk.setContract( c );
            tsk.setOrq( ist_orq);
            tsk.setDescription( "child task of slice transfer task, just ignore");

            OfferType ist_of = getEntityManager().find( OfferType.class, GORFXConstantURIs.INTER_SLICE_TRANSFER_URI );
            tsk.setOfferType( ist_of );

            tsk.setTerminationTime( c.getCurrentTerminationTime() );
            tsk.setId( getSystem().nextUUID() );
            tsk.setWid( model.getWid() );

            // perform transfer
            InterSliceTransferTaskAction ista = new InterSliceTransferTaskAction( );
            ista.initFromModel( getEntityManager(), tsk );
            ista.setClosingEntityManagerOnCleanup( false );
            ista.call( );
            res_slice = ist_orq.getDestinationSlice();

        } catch ( TransitException e ) {
            if(! isFinishedException( e ) )
                throw e;
        } catch ( Exception e ) {
            boxException( e );
        }

        // if we made it this far slice stage should have be successful
        finish( new SliceStageInResult( res_slice ) );
    }


    @SuppressWarnings( { "ThrowableInstanceNeverThrown" } )
    private void boxException( Exception e ) {
        fail( new RuntimeException( e.getMessage(), e ) );
    }

    
    protected static GNDMSystem getSystem( ) {
        if( system == null )
            throw new IllegalStateException ( "GNDMS not present" );

        return system;
    }


    public static void setSystem( GNDMSystem sys ) {

        if( system != null )
            throw new IllegalStateException ( "GNDMS already present" );

        system = sys;
    }
}
