package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
import de.zib.gndms.model.gorfx.types.FileTransferORQ;
import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.infra.network.GNDMSFileTransfer;
import de.zib.gndms.infra.network.NetworkAuxiliariesProvider;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;
import org.apache.axis.types.URI;
import org.joda.time.DateTime;

import java.io.IOException;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 30.09.2008, Time: 10:51:38
 */
public class FileTransferORQCalculator extends AbstractORQCalculator<FileTransferORQ> {


    public FileTransferORQCalculator( ) {
        super();
        super.setORQModelClass( FileTransferORQ.class );
    }


    public Contract createOffer() throws ServerException, IOException {

        GridFTPClient clnt = null;
        try {
            URI suri =  new URI( getORQArguments().getSourceURI() );
            clnt =  getNetworkAuxiliariesProvider().getGridFTPClientFactory().createClient( suri );
            GNDMSFileTransfer ft = new GNDMSFileTransfer();
            ft.setSourceClient( clnt );
            ft.setSourcePath( suri.getPath( ) );
            ft.setFiles( getORQArguments().getFileMap() );
            long ets = ft.estimateTransferSize(  );

            DateTime dat = new DateTime( );
            Float tt = getNetworkAuxiliariesProvider().getBandWidthEstimater().estimateBandWidthFromTo(
                getORQArguments( ).getSourceURI(), getORQArguments( ).getTargetURI() );

            if( tt == null )
                throw new IOException( "Couldn't estimate bandwidth between " );

            dat = dat.plusSeconds( NetworkAuxiliariesProvider.calculateTransferTime( ets, tt.floatValue( ) ) );

            Contract ct = new Contract( );

            ct.setDeadline( dat.toGregorianCalendar( )  );
            ct.setDeadlineIsOffset( true );

            return ct;
        } finally {
            if ( clnt != null )
                clnt.close( true ); // none blocking close op
        }
    }
}
