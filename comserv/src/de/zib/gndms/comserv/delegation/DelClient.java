package de.zib.gndms.comserv.delegation;

import de.zib.gndms.kit.application.AbstractApplication;
import org.kohsuke.args4j.Option;
import org.globus.wsrf.impl.security.descriptor.ClientSecurityDescriptor;

import org.globus.wsrf.impl.security.authentication.Constants;
import org.globus.wsrf.impl.security.authorization.HostAuthorization;
import org.globus.wsrf.impl.security.authorization.NoAuthorization;
import org.globus.wsrf.utils.AddressingUtils;

import org.globus.axis.util.Util;
import org.globus.delegation.DelegationUtil;
import org.apache.axis.message.addressing.EndpointReferenceType;

import java.security.cert.X509Certificate;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 28.01.2009, Time: 12:16:24
 */
public class DelClient extends AbstractApplication {


    @Option( name="-uri", required=true, usage="URL of GORFX-Endpoint", metaVar="URI" )
    protected String uri;


    public static void main(String[] args) throws Exception {
        
        DelClient cnt = new DelClient();
        cnt.run( args );
    }


    public void run() throws Exception {

        ClientSecurityDescriptor desc = new ClientSecurityDescriptor();

//        desc.setGSITransport( (Integer) Constants.ENCRYPTION );
        desc.setGSITransport( (Integer) Constants.SIGNATURE );
        Util.registerTransport();
        desc.setAuthz( NoAuthorization.getInstance() );

        System.out.println( "connecting to service: " + uri );
        EndpointReferenceType delegEpr = AddressingUtils.createEndpointReference( uri, null);
        System.out.println( "epr: " + delegEpr );
        X509Certificate[] certs = DelegationUtil.getCertificateChainRP( delegEpr, desc );

        if( certs == null  )
            throw new Exception( "No Certs received" );

        int len = certs.length;
        System.out.println( "Cert cnt: " + len );
        if( len > 0 )
        System.out.println( certs[0] );
    }
}
