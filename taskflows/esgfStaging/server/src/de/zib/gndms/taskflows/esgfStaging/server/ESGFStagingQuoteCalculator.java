package de.zib.gndms.taskflows.esgfStaging.server;

import de.zib.gndms.common.kit.security.SetupSSL;
import de.zib.gndms.common.model.gorfx.types.FutureTime;
import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.common.rest.MyProxyToken;
import de.zib.gndms.gndmc.utils.DefaultResponseExtractor;
import de.zib.gndms.gndmc.utils.HTTPGetter;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.kit.security.CredentialProvider;
import de.zib.gndms.kit.security.GetCredentialProviderFor;
import de.zib.gndms.kit.security.SSLCredentialInstaller;
import de.zib.gndms.logic.model.gorfx.AbstractQuoteCalculator;
import de.zib.gndms.logic.model.gorfx.taskflow.UnsatisfiableOrderException;
import de.zib.gndms.taskflows.esgfStaging.client.ESGFStagingTaskFlowMeta;
import de.zib.gndms.taskflows.esgfStaging.client.model.ESGFStagingOrder;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author bachmann@zib.de
 * @date 14.02.11  16:56
 * @brief Dummy quote calculator.
 */
public class ESGFStagingQuoteCalculator extends AbstractQuoteCalculator<ESGFStagingOrder> {

    protected final Logger logger = LoggerFactory.getLogger( this.getClass() );

    private final String trustStoreLocation;
    private final String trustStorePassword;

    ESGFStagingQuoteCalculator( final String trustStoreLocation, final String trustStorePassword ) {
        this.trustStoreLocation = trustStoreLocation;
        this.trustStorePassword = trustStorePassword;
    }


    @Override
    public boolean validate() {
        ESGFStagingOrder order = getOrderBean();

        if( null == order )
            return false;

        if( order.getUrls().size() == 0 )
            return false;

        return true;
    }


    @Override
    public List<Quote> createQuotes() throws
            UnsatisfiableOrderException,
            IOException,
            NoSuchAlgorithmException,
            KeyManagementException,
            KeyStoreException,
            CertificateException,
            UnrecoverableKeyException
    {

        if(! validate() )
            throw new UnsatisfiableOrderException( );

        final ESGFStagingOrder order = getOrderBean();

        final Map< String, String > urls = order.getUrls();

        // get certificate and private key by credentials
        final SetupSSL setupSSL;
        try {
            setupSSL = prepareProxy();
        }
        catch( Throwable e ) {
            logger.error( "Could not authenticate against ESGF Provider." );
            throw new UnsatisfiableOrderException( "Could not authenticate against ESGF Provider.", e );
        }

        final HTTPGetter httpGetter = new HTTPGetter();
        httpGetter.setupSSL( setupSSL, getKeyPassword() );

        DefaultResponseExtractor responseExtractor = new DefaultResponseExtractor();

        long sumFileSizes = 0;

        for( String url: urls.keySet() ) {

            // download file
            {
                httpGetter.setExtractor( 200, responseExtractor );
                final int statusCode = httpGetter.head(url);

                if( 200 != statusCode )
                {
                    throw new UnsatisfiableOrderException(
                            "Could not download all files. HTTP GET returned status code " + statusCode );
                }
            }

            // sum size of file from header
            {
                final long fileSize = responseExtractor.getHeaders().getContentLength();
                sumFileSizes += fileSize;
            }
        }

        // TODO: estimate bandwidth to get better estimations for transfertime
        // atm: 1 MB/s
        final Quote q = new Quote();
        Duration d = new Duration( sumFileSizes / ( 1024 * 1024 ) );
        q.setDeadline( FutureTime.atOffset(d) );
        FutureTime rv = FutureTime.atOffset( d.plus( 10 * 1000 * 60 ) );
        q.setResultValidity( rv );
        q.setExpectedSize( sumFileSizes );

        return Collections.singletonList(q);
    }


    @Override
    public List<Quote> createQuotes( Quote preferred ) throws
            UnsatisfiableOrderException,
            IOException,
            NoSuchAlgorithmException,
            KeyManagementException,
            KeyStoreException,
            CertificateException,
            UnrecoverableKeyException
    {

        List<Quote> rl = createQuotes();
        rl.add( preferred );

        return rl;
    }


    protected SetupSSL prepareProxy( )
            throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException,
            UnrecoverableKeyException, MandatoryOptionMissingException {
        final SetupSSL setupSSL = new SetupSSL();

        final MyProxyToken token = getOrder().getMyProxyToken().get( ESGFStagingTaskFlowMeta.REQUIRED_AUTHORIZATION.get( 0 ) );
        if( null == token )
            throw new IllegalArgumentException( "No for purpose " + ESGFStagingTaskFlowMeta.REQUIRED_AUTHORIZATION.get( 0 ) + " token provided in Order." );

        final String password = token.getPassword();

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
            logger.debug( "Couldn't deploy credentials " );
            throw new IllegalStateException( "Couldn't deploy credentials: ", e );
        }

        return setupSSL;
    }


    protected String getKeyPassword( ) {
        final MyProxyToken token = getOrder().getMyProxyToken().get( ESGFStagingTaskFlowMeta.REQUIRED_AUTHORIZATION.get( 0 ) );
        if( null == token )
            throw new IllegalArgumentException( "No " + ESGFStagingTaskFlowMeta.REQUIRED_AUTHORIZATION.get( 0 ) + " token provided in Order." );

        return token.getPassword();
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

}
