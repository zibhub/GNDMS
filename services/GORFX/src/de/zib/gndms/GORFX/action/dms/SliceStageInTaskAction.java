package de.zib.gndms.GORFX.action.dms;

import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.logic.model.gorfx.c3grid.AbstractProviderStageInAction;
import de.zib.gndms.model.gorfx.types.SliceStageInORQ;
import de.zib.gndms.model.gorfx.types.SliceStageInResult;
import de.zib.gndms.model.gorfx.types.GORFXConstantURIs;
import de.zib.gndms.model.gorfx.types.ProviderStageInResult;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.dspace.types.SliceRef;
import de.zib.gndms.GORFX.action.InterSliceTransferTaskAction;
import de.zib.gndms.GORFX.action.DSpaceBindingUtils;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.typecon.common.type.ProviderStageInResultXSDTypeWriter;
import de.zib.gndms.typecon.common.type.SliceRefXSDReader;
import de.zib.gndms.dspace.slice.client.SliceClient;
import de.zib.gndms.dspace.client.DSpaceClient;
import de.zib.gndms.dspace.subspace.client.SubspaceClient;
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


        try {
            // obtain created slice
            ProviderStageInResult psr = ( ProviderStageInResult ) getModel().getData( );
            SliceRef sr = SliceRefXSDReader.fromEPR( ProviderStageInResultXSDTypeWriter.SliceIdToEPR( psr.getSliceKey() ) );


            // create destination slice

            DSpaceClient dsc;
            if(! getOrq().hasGridSiteURI() )
                getOrq().setGridSiteURI( SliceStageInORQCalculator.destinationURI( getOrq().getGridSite() ).toString( ) );

            dsc = new DSpaceClient( getOrq().getGridSiteURI() );

            // todo find destination subspace
            SubspaceClient subc = new SubspaceClient( dsc.getSubspace( new QName( "" ) ).getEndpointReference() );
            

            // perform transfer
            InterSliceTransferTaskAction ista = new InterSliceTransferTaskAction( getEntityManager(), model );
            ista.setClosingEntityManagerOnCleanup( false );
            ista.call( );
        } catch ( TransitException e ) {
            if(! isFinishedException( e ) )
                throw e;
        } catch ( Exception e ) {
            boxException( e );
        }

        // trigger script execution in target space
        // use gsissh?

        //finish( new SliceStageInResult( getOrq().getDestinationSlice() ) );
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
