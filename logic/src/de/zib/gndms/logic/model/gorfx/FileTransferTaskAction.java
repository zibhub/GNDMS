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
import de.zib.gndms.model.gorfx.AbstractTask;
import de.zib.gndms.model.gorfx.FTPTransferState;
import de.zib.gndms.model.gorfx.types.FileTransferORQ;
import de.zib.gndms.model.gorfx.types.FileTransferResult;
import de.zib.gndms.model.util.TxFrame;
import org.apache.axis.types.URI;
import org.globus.ftp.GridFTPClient;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 01.10.2008, Time: 17:57:57
 */
public class FileTransferTaskAction extends ORQTaskAction<FileTransferORQ> {

    private FTPTransferState transferState;


    public FileTransferTaskAction() {
        super();
    }


    public FileTransferTaskAction( final @NotNull EntityManager em, final @NotNull AbstractTask model ) {
        super( em, model );
    }


    public FileTransferTaskAction( final @NotNull EntityManager em, final @NotNull String pk ) {
        super( em, pk );
    }


    @Override
    @NotNull
    protected Class<FileTransferORQ> getOrqClass() {
        return FileTransferORQ.class;
    }


    @Override
    protected void onInProgress( @NotNull AbstractTask model ) {

        GridFTPClient src = null;
        GridFTPClient dest = null;

        EntityManager em = getEmf().createEntityManager(  );
        // EntityManager em = getEntityManager(  );
        TxFrame tx = new TxFrame( em );
        try {
            transferState = em.find( FTPTransferState.class, getModel( ).getId() );

            TreeMap<String,String> files =  getOrq().getFileMap();
            if( transferState == null )
                newTransfer( );
            else {
                String s = transferState.getCurrentFile();
                if( s != null ) {
                    int p =  new ArrayList<String>( files.keySet() ).indexOf( s );
                    getModel().setProgress( p );
                }
            }

            tx.commit();


            TaskPersistentMarkerListener pml = new TaskPersistentMarkerListener( );
            pml.setEntityManager( em );
            pml.setTransferState( transferState );
            pml.setTask( getModel() );

            URI suri = new URI ( getOrq().getSourceURI() );
            URI duri = new URI ( getOrq().getTargetURI() );

            // obtain clients
            src = NetworkAuxiliariesProvider.getGridFTPClientFactory().createClient( suri, getCredentialProvider() );
            dest = NetworkAuxiliariesProvider.getGridFTPClientFactory().createClient( duri, getCredentialProvider() );

            // setup transfer handler
            GNDMSFileTransfer transfer = new GNDMSFileTransfer();
            transfer.setSourceClient( src );
            transfer.setSourcePath( suri.getPath() );

            transfer.setDestinationClient( dest );
            transfer.setDestinationPath( duri.getPath() );

            transfer.setFiles( files );

            transfer.prepareTransfer();

            int fc = transfer.getFiles( ).size( );
            getModel( ).setMaxProgress( fc );

            transfer.performPersistentTransfer( pml );

            tx.begin();
            em.remove( transferState );
            tx.commit();

            FileTransferResult ftr = new FileTransferResult();

            ArrayList<String> al = new ArrayList<String>( transfer.getFiles( ).keySet( ) );
            ftr.setFiles( al.toArray( new String[al.size( )] ));

            finish( ftr );

        } catch ( TransitException e ) {
            throw e;
        } catch ( Exception e ) {
                failFrom( e );
        } finally {

            try {
                tx.finish();
                em.close();
            } catch ( Exception e ) {
                trace( "Exception while closing entityManager client.", e );
            }

            try {
                if( src != null )
                    src.close();
            } catch ( Exception e ) {
                trace( "Exception while closing src client.", e );
            }

            try {
                if( dest != null )
                    dest.close();
            } catch ( Exception e ) {
                trace( "Exception while closing dest client.", e );
            }
        }

    }

    private void newTransfer( ) {

        transferState = new FTPTransferState();
        transferState.setTransferId( getModel().getId() );
    }

    
}
