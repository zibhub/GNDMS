package de.zib.gndms.GORFX.action;

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



import de.zib.gndms.dspace.client.DSpaceClient;
import de.zib.gndms.dspace.slice.client.SliceClient;
import de.zib.gndms.dspace.subspace.client.SubspaceClient;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.model.common.PersistentContract;
import de.zib.gndms.model.dspace.types.SliceRef;
import de.zib.gndms.model.gorfx.AbstractTask;
import de.zib.gndms.model.gorfx.OfferType;
import de.zib.gndms.model.gorfx.SubTask;
import de.zib.gndms.model.gorfx.types.*;
import de.zib.gndms.model.util.TxFrame;
import de.zib.gndms.gritserv.typecon.types.ProviderStageInResultXSDTypeWriter;
import de.zib.gndms.gritserv.typecon.types.SliceRefXSDReader;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.xml.namespace.QName;
import java.util.List;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 11.11.2008, Time: 13:49:57
 */
public class StagedTransferTaskAction extends ORQTaskAction<SliceStageInORQ> {

    private static GNDMSystem system;
    private SubTask providerStageIn;
    private SubTask interSliceTransfer;

    @NotNull
    protected Class<SliceStageInORQ> getOrqClass() {
        return SliceStageInORQ.class;
    }


    @SuppressWarnings( { "ThrowableInstanceNeverThrown" } )
    @Override
    protected void onInProgress( @NotNull AbstractTask model ) {

        EntityManager em = getEntityManager();
        TxFrame tx = new TxFrame( em );
        try {
            tx.begin( );
            Query q = em.createNamedQuery( "findSubTasks" );
            q.setParameter( "idParm", this );
            List<SubTask> st = ( List<SubTask> ) q.getResultList();

            if( st.size() > 1 )
                restoreSubTask( st.get( 0 ) );
            else if( st.size() > 2 )
                restoreSubTask( st.get( 1 ) );
            else if( st.size( ) > 3 )
                failFrom( new RuntimeException( "to much subtasks" ) );
            tx.commit( );
        } finally{
            tx.finish();
        }

        SliceRef  res_slice = null;
        try {

            ProviderStageInResult psr = doStageIn( model, tx );

            if( interSliceTransfer != null ) {
                checkFailed( interSliceTransfer );
                if( interSliceTransfer.getState().equals( TaskState.FINISHED ) )
                    finish( new SliceStageInResult(
                        ( (InterSliceTransferORQ) interSliceTransfer.getOrq() ).getDestinationSlice() ) );
            }

            // obtain created slice
            SliceRef sr = SliceRefXSDReader.fromEPR( ProviderStageInResultXSDTypeWriter.SliceIdToEPR( psr.getSliceKey() ) );

            // create destination slice
            DSpaceClient dsc = new DSpaceClient( getOrq().getActGridSiteURI() );


            // todo find destination subspace
            SubspaceClient sscnt = new SubspaceClient( dsc.getSubspace( new QName( "" ) ).getEndpointReference() );

            PersistentContract c = model.getContract();
            long size = c.getExpectedSize();
            SliceClient tgt = sscnt.createSlice( GORFXConstantURIs.PUBLISH_SLICE_KIND_URI,
                c.getDeadline(), size == 0 ? 1 : size );

            // create target transfer orq

            InterSliceTransferORQ ist_orq = new InterSliceTransferORQ( );
            ist_orq.setSourceSlice( sr );
            ist_orq.setDestinationSlice( SliceRefXSDReader.fromEPR( tgt.getEndpointReference() ) );

            if( interSliceTransfer == null ) {
                interSliceTransfer = new SubTask( model );

                interSliceTransfer.setContract( c );
                interSliceTransfer.setOrq( ist_orq);
                interSliceTransfer.setDescription( "child task of slice transfer task, just ignore");

                OfferType ist_of = getEntityManager().find( OfferType.class, GORFXConstantURIs.INTER_SLICE_TRANSFER_URI );
                interSliceTransfer.setOfferType( ist_of );

                interSliceTransfer.setTerminationTime( c.getCurrentTerminationTime() );
                interSliceTransfer.setId( getSystem().nextUUID() );
                interSliceTransfer.setWid( model.getWid() );
            }

            // perform transfer
            InterSliceTransferTaskAction ista = new InterSliceTransferTaskAction( );
            ista.initFromModel( getEmf().createEntityManager(), dao, interSliceTransfer );
            ista.setClosingEntityManagerOnCleanup( true );
            ista.call( );

            res_slice = ist_orq.getDestinationSlice();

        } catch ( TransitException e ) {
            if(! isFinishedTransition( e ) )
                throw e;
        } catch ( Exception e ) {
            boxException( e );
        } finally {
           tx.finish( );
        }

        // if we made it this far slice stage should have be successful
        finish( new SliceStageInResult( res_slice ) );
    }


    private ProviderStageInResult doStageIn( AbstractTask model, TxFrame tx ) {

        PersistentContract c = model.getContract( );

        // check if this task is resumed and we aleady have a staged the data

        if( providerStageIn == null ) {
            providerStageIn = new SubTask( model );
            providerStageIn.setId( getSystem( ).nextUUID() );
            providerStageIn.setOfferType( model.getOfferType() );
            providerStageIn.setOrq( getOrq() );
            providerStageIn.setContract( c );
            providerStageIn.setTerminationTime( c.getCurrentTerminationTime() );
            providerStageIn.setWid( model.getWid() );
        } else
            checkFailed( providerStageIn );

        if(! providerStageIn.getState().equals( TaskState.FINISHED ) ) {

            // perform staging
            try {
                /*
                 * TODO overwork this block
                tx.begin( );
                // AbstractProviderStageInAction psa = (AbstractProviderStageInAction)
                //    getSystem().getInstanceDir().getTaskAction( getEntityManager(), GORFXConstantURIs.PROVIDER_STAGE_IN_URI );

<<<<<<< local
                // psa.setClosingEntityManagerOnCleanup( false );
                // psa.initFromModel( getEntityManager(), providerStageIn );
                // psa.call( );
                psa.setClosingEntityManagerOnCleanup( false );
                psa.initFromModel( getEntityManager(), providerStageIn );
                psa.call( );
                */
            } catch ( TransitException e ) {
                tx.commit( );
                if(! isFinishedTransition( e ) )
                    throw e;
            } catch ( Exception e ) {
                tx.finish( );
                boxException( e );
            }
        }

        return (ProviderStageInResult) providerStageIn.getData();
    }


    @SuppressWarnings( { "ThrowableInstanceNeverThrown" } )
    private void boxException( Exception e ) {
        failFrom( e );
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


    @SuppressWarnings( { "ThrowableInstanceNeverThrown" } )
    private  void restoreSubTask( SubTask st ) {

        if( st.getOfferType().getOfferTypeKey().equals( GORFXConstantURIs.PROVIDER_STAGE_IN_URI ) )
            providerStageIn = checkedAssignment( providerStageIn, st );
        else if( st.getOfferType().getOfferTypeKey().equals( GORFXConstantURIs.INTER_SLICE_TRANSFER_URI ) )
            interSliceTransfer = checkedAssignment( interSliceTransfer, st );
        else
            failFrom( new RuntimeException( "Unexpected sub-task occurred") );
    }


    @SuppressWarnings( { "ThrowableInstanceNeverThrown" } )
    private SubTask  checkedAssignment ( SubTask tgt, SubTask src ) {
        if( tgt == null )
            return src;
        else
            failFrom( new RuntimeException( "Sub-task occurres multiple times" ) );

        return null;
    }


    private void checkFailed( SubTask st ) {
        if( st.getState().equals( TaskState.FAILED ) )
            failFrom( ( RuntimeException) st.getData() );
    }
}
