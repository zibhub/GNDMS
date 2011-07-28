package de.zib.gndms.logic.model.gorfx;

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



import de.zib.gndms.kit.network.GNDMSFileTransfer;
import de.zib.gndms.kit.network.NetworkAuxiliariesProvider;
import de.zib.gndms.model.gorfx.FTPTransferState;
import de.zib.gndms.model.gorfx.types.FileTransferOrder;
import de.zib.gndms.model.gorfx.types.FileTransferResult;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.Taskling;
import org.globus.ftp.GridFTPClient;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 01.10.2008, Time: 17:57:57
 */
public class FileTransferTaskAction extends TaskFlowAction<FileTransferOrder> {

    private FTPTransferState transferState;
    private FileTransferOrder order;


    public FileTransferTaskAction() {
        super();
    }

    public FileTransferTaskAction(@NotNull EntityManager em, @NotNull Dao dao, @NotNull Taskling model) {
        super(em, dao, model);
    }

    @Override
    @NotNull
    public Class<FileTransferOrder> getOrqClass() {
        return FileTransferOrder.class;
    }


    @Override
    protected void onInProgress(@NotNull String wid,
                                @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState)
            throws Exception {
        Map<String, String> files;
        GridFTPClient src;
        GridFTPClient dest;


        Session session = getDao().beginSession();
        try {
            Task task  = getTask(session);
            transferState = (FTPTransferState) task.getPayload();
            order = ((FileTransferOrder )task.getORQ());
            files         = order.getFileMap();
            if( transferState == null )
                newTransfer( task );
            else {
                String s = transferState.getCurrentFile();
                if( s != null ) {
                    int p =  new ArrayList<String>( files.keySet() ).indexOf( s );
                    task.setProgress( p );
                }
            }
            session.success();
        }
        finally { session.finish(); }


        TaskPersistentMarkerListener pml = new TaskPersistentMarkerListener( );
        pml.setDao( getDao() );
        pml.setTransferState( transferState );
        pml.setTaskling(getModel());
        pml.setWid(wid);
        pml.setGORFXId( getOrder().getActId());

        URI suri = new URI ( order.getSourceURI() );
        URI duri = new URI ( order.getDestinationURI() );

        // obtain clients
        src = NetworkAuxiliariesProvider.getGridFTPClientFactory().createClient( suri, getCredentialProvider() );

        try {
            dest = NetworkAuxiliariesProvider.getGridFTPClientFactory().createClient( duri, getCredentialProvider() );

            try {
                // setup transfer handler
                GNDMSFileTransfer transfer = new GNDMSFileTransfer();
                transfer.setSourceClient( src );
                transfer.setSourcePath( suri.getPath() );

                transfer.setDestinationClient( dest );
                transfer.setDestinationPath( duri.getPath() );

                transfer.setFiles( files );

                transfer.prepareTransfer();

                int fc = transfer.getFiles( ).size( );

                session = getDao().beginSession();
                try {
                    getTask(session).setMaxProgress( fc );
                    session.success();
                }
                finally { session.finish(); }

                transfer.performPersistentTransfer( pml );

                FileTransferResult ftr = new FileTransferResult();

                ArrayList<String> al = new ArrayList<String>( transfer.getFiles( ).keySet( ) );
                ftr.setFiles( al );

                transitWithPayload(ftr, TaskState.FINISHED);
                if (altTaskState)
                    getDao().removeAltTaskState(getModel().getId());
            }
            finally { dest.close(); }
        }
        finally { src.close(); }
    }

    private void newTransfer( @NotNull Task task ) {
        transferState = new FTPTransferState();
        transferState.setTransferId( getModel().getId() );
        task.setPayload( transferState );
    }
}
