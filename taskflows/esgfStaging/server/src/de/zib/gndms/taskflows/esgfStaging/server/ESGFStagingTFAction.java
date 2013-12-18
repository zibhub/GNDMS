package de.zib.gndms.taskflows.esgfStaging.server;


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


import de.zib.gndms.common.kit.security.SetupSSL;
import de.zib.gndms.common.model.gorfx.types.SliceResultImpl;
import de.zib.gndms.common.rest.MyProxyToken;
import de.zib.gndms.gndmc.utils.DownloadResponseExtractor;
import de.zib.gndms.gndmc.utils.HTTPGetter;
import de.zib.gndms.infra.action.SlicedTaskFlowAction;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.kit.security.CredentialProvider;
import de.zib.gndms.kit.security.GetCredentialProviderFor;
import de.zib.gndms.kit.security.SSLCredentialInstaller;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.gorfx.types.DelegatingOrder;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.Taskling;
import de.zib.gndms.taskflows.esgfStaging.client.ESGFStagingTaskFlowMeta;
import de.zib.gndms.taskflows.esgfStaging.client.model.ESGFStagingOrder;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;

/**
 * @author bachmann@zib.de
 * @date 14.02.11  17:53
 * @brief The action of the dummy task flow.
 *
 * @see ESGFStagingOrder
 */
public class ESGFStagingTFAction extends SlicedTaskFlowAction< ESGFStagingOrder > {

    private static final String PROXY_FILE_NAME = File.separator + "x509_proxy.pem";

    @Override
    public Class< ESGFStagingOrder > getOrderBeanClass( ) {
        return ESGFStagingOrder.class;
    }


    public ESGFStagingTFAction() {
        super( ESGFStagingTaskFlowMeta.TASK_FLOW_TYPE_KEY );
    }


    public ESGFStagingTFAction(@NotNull EntityManager em, @NotNull Dao dao, @NotNull Taskling model) {

        super( ESGFStagingTaskFlowMeta.TASK_FLOW_TYPE_KEY, em, dao, model );
    }


    @Override
    protected synchronized void onCreated(
            @NotNull String wid,
            @NotNull TaskState state,
            boolean isRestartedTask,
            boolean altTaskState ) throws Exception
    {

        ensureOrder();

        // set max progress to number of files to download
        if( !isRestartedTask ) {
            ESGFStagingOrder order = getOrderBean();

            final Session session = getDao().beginSession();
            try {
                Task task = getTask( session );
                task.setProgress(0);
                task.setMaxProgress(order.getUrls().size());
                session.success();
            }
            finally { session.finish(); }

            super.onCreated( wid, state, isRestartedTask, altTaskState );    // overridden method implementation
        }

        if( getOrderBean().hasSliceId() ) {
            attachSlice( getOrderBean().getSliceId() );
        }
        else {
        	try{
            createNewSlice();
        	}catch (Exception e){
        		throw new RuntimeException("Could not create slice "+e);
        	}

			if (getOrderBean().hasSliceConfiguration() && null != findSlice()) {
                findSlice().setConfiguration( getOrderBean().getSliceConfiguration() );
            }
        }

        super.onCreated(wid, state, isRestartedTask, altTaskState);
    }


    @Override
    protected synchronized void onInProgress( @NotNull String wid, @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState ) throws Exception {
        final ESGFStagingOrder order = getOrderBean();

        // check for quotas
        if( !isRestartedTask ) {
            checkQuotas();
        }

        final Map< String, String > urls = order.getUrls();
        final Slice slice = findSlice();
		if (null == slice) {
			throw new IllegalStateException("Could not find slice id");
		}
        final String slicePath = slice.getSubspace().getPathForSlice( slice );

        // get certificate and private key by credentials
        final SetupSSL setupSSL;
        try {
            setupSSL = prepareProxy();
        }
        catch( Throwable e ) {
			logger.error("Could not authenticate against ESGF Provider." + e);
            transit( TaskState.FAILED );
            throw new Exception( "Could not authenticate against ESGF Provider.", e );
        }
        
        final HTTPGetter httpGetter = new HTTPGetter();
        httpGetter.setupSSL( setupSSL, getKeyPassword() );

        final Task detachedTask = getDetachedTask();

        int progress = detachedTask.getProgress();
        int i = 0;
        for( String url: urls.keySet() ) {
            // skip already processed entries
            if( i++ < progress )
                continue;

            File urlFile = new File( url );
            File outFile = new File( slicePath + File.separatorChar + urlFile.getName() );

            // download file
            {
                httpGetter.setExtractor( 200, new DownloadResponseExtractor( outFile.getCanonicalPath() ) );
                final int statusCode = httpGetter.get( url );
                        
                if( 200 != statusCode )
                {
                    transit(TaskState.FAILED);
                    throw new Exception( "Could not download all files. HTTP GET returned status code " + statusCode );
                }
            }

            // check for correct checksum
			{
				String checksum = urls.get(url);
				String compareMD5 = makeChecksum(outFile, "MD5");
				logger.info("expected checksum "+checksum);
				logger.info("checksum md5 "+compareMD5);
				

				if (!checksum.equals(compareMD5)) {
					String compareSHA256 = makeChecksum(outFile, "SHA-256");
					logger.info("checksum compareSHA256 "+compareSHA256);

					if (!checksum.equals(compareSHA256)) {
						transit(TaskState.FAILED);
						throw new Exception("Downloaded file "
								+ outFile.getName() + " has wrong checksum.");
					}
				}
			}

            setProgress( ++progress );
        }

        transitWithPayload(
                new SliceResultImpl(
                        ESGFStagingTaskFlowMeta.TASK_FLOW_TYPE_KEY,
                        getSliceSpecifier() ),
                TaskState.FINISHED );

        super.onInProgress(wid, state, isRestartedTask, altTaskState);    // overridden method implementation
    }


    public CredentialProvider getCredentialProvider() {
        CredentialProvider credentialProvider = new GetCredentialProviderFor(
                getOrder(),
                ESGFStagingTaskFlowMeta.REQUIRED_AUTHORIZATION.get( 0 ),
                getMyProxyFactoryProvider()
        ).invoke();
        credentialProvider.setInstaller( new SSLCredentialInstaller() );
        return  credentialProvider;
    }


    protected String getKeyPassword( ) {
        final MyProxyToken token = getOrder().getMyProxyToken().get( ESGFStagingTaskFlowMeta.REQUIRED_AUTHORIZATION.get( 0 ) );
        if( null == token )
            throw new IllegalArgumentException( "No " + ESGFStagingTaskFlowMeta.REQUIRED_AUTHORIZATION.get( 0 ) + " token provided in Order." );

        return token.getPassword();
    }


    protected SetupSSL prepareProxy( )
            throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException,
            UnrecoverableKeyException, MandatoryOptionMissingException {
        final SetupSSL setupSSL = new SetupSSL();
        
        final MyProxyToken token = getOrder().getMyProxyToken().get( ESGFStagingTaskFlowMeta.REQUIRED_AUTHORIZATION.get( 0 ) );
        if( null == token ){
        	logger.error("cannot get MyProxyToken");
			logger.error("No for purpose "	+ ESGFStagingTaskFlowMeta.REQUIRED_AUTHORIZATION.get(0)+ " token provided in Order.");           
            throw new IllegalArgumentException( "No for purpose " + ESGFStagingTaskFlowMeta.REQUIRED_AUTHORIZATION.get( 0 ) + " token provided in Order." );
    }

        final String password = token.getPassword();
        final String trustStoreLocation = getOfferTypeConfig().getOption( "trustStoreLocation" );
        final String trustStorePassword = getOfferTypeConfig().getOption( "trustStorePassword" );

        setupSSL.setTrustStoreLocation( trustStoreLocation );
        setupSSL.prepareTrustStore( trustStorePassword, "JKS" );
        setupSSL.prepareKeyStore( password, "JKS" );

        try {
            SSLCredentialInstaller.InstallerParams params = new SSLCredentialInstaller.InstallerParams();

            params.setAlias( "privateKey" );
            params.setPassword( password );
            params.setSetupSSL( setupSSL );

            getCredentialProvider().installCredentials( params );
        }
        catch ( Exception e ) {
            logger.debug( "Couldn't deploy credentials " +e );
            throw new IllegalStateException( "Couldn't deploy credentials: ", e );
        }

        return setupSSL;
    }
    
    
    public static String makeChecksum( File file, String type ) throws Exception {
        final MessageDigest md = MessageDigest.getInstance( type );
        final FileInputStream fis = new FileInputStream( file );
        final byte[] dataBytes = new byte[1024];
 
        int nread = 0;
 
        while( ( nread = fis.read( dataBytes ) ) != -1 ) {
            md.update( dataBytes, 0, nread );
        }
 
        final byte[] mdbytes = md.digest();
 
        //convert the byte to hex format
        final StringBuffer sb = new StringBuffer( "" );
        for( byte mdbyte : mdbytes ) {
            sb.append( Integer.toString( ( mdbyte & 0xff ) + 0x100, 16 ).substring( 1 ) );
        }

        return sb.toString();
    }


    private void setProgress( int progress ) {
        final Session session = getDao().beginSession();
        try {
            Task task = getTask( session );
            task.setProgress( progress );
            session.success();
        }
        finally { session.finish(); }
    }


    private Task getDetachedTask() {
        final Session session = getDao().beginSession();
        try {
            Task task = getTask( session );
            session.success();
            return task;
        }
        finally { session.finish(); }
    }


    @Override
    public DelegatingOrder<ESGFStagingOrder> getOrder() {

        DelegatingOrder< ESGFStagingOrder > order = null;

        final Session session = getDao().beginSession();
        try {
            order = ( DelegatingOrder< ESGFStagingOrder > ) getTask( session ).getOrder( );
            session.success();
        }
        finally { session.finish(); }

        return order;
    }
}
