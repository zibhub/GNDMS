package de.zib.gndms.taskflows.filetransfer.server.logic;

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



import de.zib.gndms.kit.security.CredentialProvider;
import de.zib.gndms.kit.security.GetCredentialProviderForGridFTP;
import de.zib.gndms.logic.model.gorfx.TaskFlowAction;
import de.zib.gndms.taskflows.filetransfer.client.FileTransferMeta;
import de.zib.gndms.taskflows.filetransfer.client.model.FileTransferOrder;
import de.zib.gndms.taskflows.filetransfer.client.model.FileTransferResult;
import de.zib.gndms.taskflows.filetransfer.server.network.GNDMSFileTransfer;
import de.zib.gndms.taskflows.filetransfer.server.network.NetworkAuxiliariesProvider;
import de.zib.gndms.model.gorfx.FTPTransferState;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.Taskling;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ClientException;
import org.globus.ftp.exception.ServerException;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author  Maik Jorra
 * @version  $Id$
 *
 * User: mjorra, Date: 01.10.2008, Time: 17:57:57
 */
public class FileTransferTaskAction extends TaskFlowAction<FileTransferOrder> {


    public FileTransferTaskAction() {
        super( FileTransferMeta.FILE_TRANSFER_TYPE_KEY );
    }

    public FileTransferTaskAction(@NotNull EntityManager em, @NotNull Dao dao, @NotNull Taskling model) {
        super(em, dao, model);
    }

    @Override
    @NotNull
    public Class<FileTransferOrder> getOrderBeanClass( ) {
        return FileTransferOrder.class;
    }

    @Override
    protected void onCreated( @NotNull String wid, @NotNull TaskState state,
                              boolean isRestartedTask, boolean altTaskState ) throws Exception
    {

        if ( !isRestartedTask ) {
            super.onCreated( wid, state, isRestartedTask, altTaskState );    // overridden method implementation
            final Session session = getDao().beginSession();
            try {
                Task task = getTask( session );
                task.setProgress( 0 );
                final FileTransferOrder order = getOrderBean( );
                if( order.hasFileMap() )
                    task.setMaxProgress( order.getFileMap().size() );
                else
                    task.setMaxProgress( 1 ); // well we don't now it yet...
                session.success();
            } finally {
                session.finish();
            }
        }
    }

    
    @Override
    protected void onInProgress(@NotNull String wid,
                                @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState)
            throws Exception {
        
        ensureOrder();

        Map<String, String> files = getOrderBean().getFileMap();
        FTPTransferState transferState = resumeOrInitTransferState( files );

        URI suri = new URI ( getOrderBean().getSourceURI() );
        URI duri = new URI ( getOrderBean().getDestinationURI( ) );

        GridFTPClient src = createGridFTPClient( suri );
        try {

            GridFTPClient dest = createGridFTPClient( duri );
            try {
                // setup transfer handler
                GNDMSFileTransfer transfer = setupFileTransfer( src, suri.getPath(), dest,
                        duri.getPath(), files );
                
                // transfer holds the up-to-date file listing
                updateMaxProgress( transfer.getFiles().size() );

                transfer.performPersistentTransfer(
                        initPersistentMarkerListener( transferState, wid ) );

                FileTransferResult result = new FileTransferResult();

                result.setFiles( new ArrayList<String>( transfer.getFiles().keySet() ) );

                transitWithPayload( result, TaskState.FINISHED );
                if (altTaskState)
                    getDao().removeAltTaskState(getModel().getId());
            }
            finally { dest.close(); }
        }
        finally { src.close(); }
    }


    private GridFTPClient createGridFTPClient( final URI suri ) throws ServerException, IOException,
            TimeoutException
    {
        return NetworkAuxiliariesProvider.getGridFTPClientFactory().createClient(
                suri, getCredentialProvider() );
    }


    private GNDMSFileTransfer setupFileTransfer( final GridFTPClient srcClient, final String srcDir,
                                                 final GridFTPClient destClient,
                                                 final String destDir,
                                                 final Map<String, String> files ) throws
            ServerException, IOException, ClientException
    {

        GNDMSFileTransfer transfer = new GNDMSFileTransfer();
        transfer.setSourceClient( srcClient );
        transfer.setSourcePath( srcDir );

        transfer.setDestinationClient( destClient );
        transfer.setDestinationPath( destDir );

        transfer.setFiles( files );

        transfer.prepareTransfer();
        return transfer;
    }


    protected TaskPersistentMarkerListener initPersistentMarkerListener(
            final FTPTransferState transferState, final String wid ) {

        TaskPersistentMarkerListener pml = new TaskPersistentMarkerListener( );
        pml.setDao( getDao() );
        pml.setTaskling( getModel() );
        pml.setTransferState( transferState );
        pml.setWid( wid );
        pml.setGORFXId( getOrder().getActId() );
        return pml;
    }


    @Override
    public CredentialProvider getCredentialProvider() {

        // todo string based factory and credential names are error prone

        return new GetCredentialProviderForGridFTP( getOrder(),
                FileTransferMeta.REQUIRED_AUTHORIZATION.get( 0 ),
                getMyProxyFactoryProvider() ).invoke();
    }


    public FTPTransferState resumeOrInitTransferState( final Map<String, String> files ) {

        Session session = getDao().beginSession();
        FTPTransferState transferState;
        try {
            Task task = getTask( session );
            transferState = ( FTPTransferState ) task.getPayload();
            if ( transferState == null ) { // transfer is fresh
                transferState = initTransferState( task );
            } else {            // transfer is resuming
                String s = transferState.getCurrentFile();
                if ( s != null ) { // at least one file was already transferred
                    int p = new ArrayList<String>( files.keySet() ).indexOf( s );
                    task.setProgress( p );
                }
            }
            session.success();
        } finally {
            session.finish();
        }
        return transferState;
    }


    private FTPTransferState initTransferState( @NotNull Task task ) {

        FTPTransferState transferState = new FTPTransferState();
        transferState.setTransferId( getModel().getId() );
        task.setPayload( transferState );
        return transferState;
    }
}
