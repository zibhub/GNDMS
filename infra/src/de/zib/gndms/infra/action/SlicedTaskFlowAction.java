/*
 * Copyright 2008-2012 Zuse Institute Berlin (ZIB)
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
 *
 */

package de.zib.gndms.infra.action;

import de.zib.gndms.common.dspace.SliceConfiguration;
import de.zib.gndms.common.dspace.service.SubspaceService;
import de.zib.gndms.common.model.gorfx.types.AbstractOrder;
import de.zib.gndms.common.model.gorfx.types.Order;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.infra.GridConfig;
import de.zib.gndms.kit.config.ConfigProvider;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.logic.action.ProcessBuilderAction;
import de.zib.gndms.logic.model.dspace.ChownSliceConfiglet;
import de.zib.gndms.logic.model.dspace.DeleteSliceTaskAction;
import de.zib.gndms.logic.model.dspace.SliceProvider;
import de.zib.gndms.logic.model.gorfx.AbstractQuoteCalculator;
import de.zib.gndms.logic.model.gorfx.TaskFlowAction;
import de.zib.gndms.model.common.NoSuchResourceException;
import de.zib.gndms.model.common.PersistentContract;
import de.zib.gndms.model.common.types.GNDMSUserDetailsInterface;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.gorfx.types.DelegatingOrder;
import de.zib.gndms.model.util.TxFrame;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.Taskling;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.File;

/**
 * @date: 12.08.12
 * @time: 17:02
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public abstract class SlicedTaskFlowAction< K extends AbstractOrder > extends TaskFlowAction< K > {

    public static final long DEFAULT_SLICE_SIZE = 100*1024*1024; // 100MB


    private GridConfig gridConfig;


    protected SliceProvider sliceProvider;

    private AbstractQuoteCalculator<? extends Order> quoteCalculator;
    private SubspaceService subspaceService;


    public SlicedTaskFlowAction( @NotNull String taskFlowTypeId ) {
        super( taskFlowTypeId );
    }


    public SlicedTaskFlowAction(
            @NotNull String taskFlowTypeId,
            @NotNull EntityManager em,
            @NotNull Dao dao,
            @NotNull Taskling model ) {

        super( taskFlowTypeId, em, dao, model );
    }


    protected Slice findSlice( final String sliceId ) {
        getLogger().info( "findSlice(" + ( sliceId == null ? "null" : '"' + sliceId + '"' ) + ')' );
        if (sliceId == null)
            return null;


        final EntityManager em = getEntityManager();
        final TxFrame txf = new TxFrame(em);
        try {
            final Slice slice = em.find(Slice.class, sliceId);
            txf.commit();
            return slice;
        }
        finally { txf.finish();  }
    }
    
    
    protected void updateSlice( Slice slice ) {
        final EntityManager em = getEntityManager();
        final TxFrame txf = new TxFrame(em);
        try {
            em.persist( slice );
            txf.commit();
        }
        finally { txf.finish();  }
    }


    protected Slice findSlice() {
        return findSlice( getSliceId() );
    }


    protected String getSliceId( ) {
        final Specifier<Void> sliceSpecifier = getSliceSpecifier();

        if( null == sliceSpecifier )
            throw new NoSuchResourceException( "Could not get slice Id." );

        return sliceSpecifier.getUriMap().get( UriFactory.SLICE );
    }


    protected Specifier< Void > getSliceSpecifier( ) {
        // maybe cache the slice id
        final Session session = getDao().beginSession();
        try {
            final Task task = getTask( session );
            Specifier<Void> sliceSpec = ( Specifier<Void> ) task.getPayload();
            session.success();
            return sliceSpec;
        }
        finally {
            session.finish();
        }
    }


    protected void setSliceSpecifier( final Specifier< Void > sliceSpec, final Session session ) {
        final Task task = getTask( session );
        task.setPayload(sliceSpec);
    }


    protected void setSliceSpecifier( Specifier<Void> sliceSpec) {
        final Session session = getDao().beginSession();
        try {
            setSliceSpecifier(sliceSpec, session );
            session.success();
        }
        finally {
            session.finish();
        }
    }

    
    protected void attachSlice( final String sliceId ) throws Exception {
        final ConfigProvider config = getOfferTypeConfig();

        final String subspaceId = config.getOption( "subspace" );
        String sliceKindKey = config.getOption( "sliceKind" );

        Specifier< Void > sliceSpec = UriFactory.createSliceSpecifier(
                getGridConfig().getBaseUrl(),
                subspaceId,
                sliceKindKey,
                sliceId );

        setSliceSpecifier( sliceSpec );
    }
    

    protected void createNewSlice() throws MandatoryOptionMissingException {
        final ConfigProvider config = getOfferTypeConfig();

        final String subspaceId = config.getOption( "subspace" );
        String sliceKindId = config.getOption( "sliceKind" );


        SliceConfiguration sconf = new SliceConfiguration();
        sconf.setTerminationTime( getContract().getResultValidity() );
        sconf.setSize( DEFAULT_SLICE_SIZE );

        ResponseEntity< Specifier< Void > > sliceResponseEntity = subspaceService.createSlice(
                subspaceId,
                sliceKindId,
                sconf.getStringRepresentation(),
                getOrder().getDNFromContext() );

        if (! HttpStatus.CREATED.equals( sliceResponseEntity.getStatusCode() ) )
            throw new IllegalStateException( "Slice creation failed" );

        setSliceSpecifier( sliceResponseEntity.getBody() );

        // to provoke nasty test condition uncomment the following line
        //throw new NullPointerException( );
        getLogger().info("createNewSlice() = " + getSliceId());
    }


    protected void deleteSlice( final String sliceId ) {

        final DeleteSliceTaskAction deleteSliceTaskAction = new DeleteSliceTaskAction();
        getInjector().injectMembers( deleteSliceTaskAction );
        getService().submitTaskAction(deleteSliceTaskAction,
                new de.zib.gndms.model.gorfx.types.ModelIdHoldingOrder(sliceId), getWid());
    }


    protected void killSlice() {
        deleteSlice(getSliceId());
    }


    protected  void changeSliceOwner( Slice slice ) {

        ChownSliceConfiglet csc = getConfigletProvider().getConfiglet( ChownSliceConfiglet.class, "sliceChown" );

        if( csc == null )
            throw new IllegalStateException( "chown configlet is null!");

        final DelegatingOrder<?> order = getOrder();
        String dn = order.getDNFromContext();
       
        	GNDMSUserDetailsInterface userDetails = ( GNDMSUserDetailsInterface )order.getSecurityContextHolder().getSecurityContext().getAuthentication().getPrincipal();
        String localUser = userDetails.getLocalUser();
        
        getLogger().debug( "cso DN: " + dn );
        getLogger().debug( "changing owner of " + slice.getId() + " to " + localUser );
        ProcessBuilderAction chownAct = csc.createChownSliceAction( localUser,
                slice.getSubspace().getPath() + File.separator + slice.getKind().getSliceDirectory(),
                slice.getDirectoryId() );
        getLogger().debug( "calling " + chownAct.getProcessBuilder().command().toString() );
        chownAct.getProcessBuilder().redirectErrorStream(true);
        chownAct.call();
        getLogger().debug("Output for chown:" + chownAct.getOutputReceiver().toString());
    }
    
    
    protected void checkQuotas() throws Exception {
        final Slice sliceModel = findSlice();
        final de.zib.gndms.infra.dspace.Slice slice = new de.zib.gndms.infra.dspace.Slice( sliceModel );
        final long sliceSize = slice.getTotalStorageSize();
        final long sliceUsage = slice.getDiskUsage();
        
        getQuoteCalculator().setOrder( ( DelegatingOrder )getOrder() );
        final long needSize = getQuoteCalculator().createQuotes().get( 0 ).getExpectedSize();

        if( sliceUsage + needSize > sliceSize )
            throw new IllegalStateException(
                    "Staging would exceed slice size: Need "
                            + String.valueOf( needSize )
                            + " Bytes but have only "
                            + String.valueOf( sliceSize - sliceUsage )
                            + " Bytes left." );
    }


    public @NotNull
    MapConfig getOfferTypeConfig() {
        return new MapConfig( getTaskFlowTypeConfigMapData() );
    }


    public PersistentContract getContract() {
        final Session session = getDao().beginSession();
        try {
            final Task task = getTask(session);
            final PersistentContract ret = task.getContract();
            session.finish();
            return ret;
        }
        finally {
            session.success();
        }
    }


    public SubspaceService getSubspaceService() {
        return subspaceService;
    }

    @SuppressWarnings( "SpringJavaAutowiringInspection" )
    @Inject
    public void setSubspaceService( SubspaceService subspaceService ) {
        this.subspaceService = subspaceService;
    }


    private String getWid() {

        Session session = getDao().beginSession();
        try {
            String wid = getTask( session ).getWID();
            session.success();
            return wid;
        } finally {session.finish();}
    }


    public AbstractQuoteCalculator<? extends Order> getQuoteCalculator() {
        return quoteCalculator;
    }


    public void setQuoteCalculator( AbstractQuoteCalculator<? extends Order> quoteCalculator ) {
        this.quoteCalculator = quoteCalculator;
    }


    public GridConfig getGridConfig() {
        return gridConfig;
    }


    @Inject
    public void setGridConfig( GridConfig gridConfig ) {
        this.gridConfig = gridConfig;
    }


    @Inject
    public void setSliceProvider( SliceProvider sliceProvider ) {
        this.sliceProvider = sliceProvider;
    }


    protected SliceProvider getSliceProvider() {
        return sliceProvider;
    }
}
