package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.kit.network.GNDMSFileTransfer;
import de.zib.gndms.kit.network.NetworkAuxiliariesProvider;
import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.model.gorfx.types.FileTransferORQ;
import org.apache.axis.types.URI;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;
import org.globus.ftp.exception.ClientException;
import org.joda.time.DateTime;

import java.io.IOException;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 30.09.2008, Time: 10:51:38
 */
public abstract class AbstractTransferORQCalculator<M extends FileTransferORQ, C extends AbstractORQCalculator<M, C>>
    extends AbstractORQCalculator<M,C> {


    protected AbstractTransferORQCalculator( Class<M> cls ) {
        super();
        super.setORQModelClass( cls );
    }


    public Contract createOffer() throws ServerException, IOException, ClientException {

        GridFTPClient clnt = null;
        try {
            URI suri =  new URI( getORQArguments().getSourceURI() );
            clnt =  getNetAux().getGridFTPClientFactory().createClient( suri );
            GNDMSFileTransfer ft = new GNDMSFileTransfer();
            ft.setSourceClient( clnt );
            ft.setSourcePath( suri.getPath( ) );
            ft.setFiles( getORQArguments().getFileMap() );
            long ets = ft.estimateTransferSize(  );

            DateTime dat = new DateTime( );
            Float tt = getNetAux().getBandWidthEstimater().estimateBandWidthFromTo(
                getORQArguments( ).getSourceURI(), getORQArguments( ).getTargetURI() );

            if( tt == null )
                throw new IOException( "Couldn't estimate bandwidth between " );

            dat = dat.plusSeconds( NetworkAuxiliariesProvider.calculateTransferTime( ets, tt.floatValue( ) ) );

            Contract ct = new Contract( );

            ct.setDeadline( dat.toGregorianCalendar( )  );
            ct.setDeadlineIsOffset( false );
            ct.setResultValidity( dat.plusHours( ContractConstants.FILE_TRANSFER_RESULT_VALIDITY ).toGregorianCalendar() );

            return ct;
        } finally {
            if ( clnt != null )
                clnt.close( true ); // none blocking close op
        }
    }
}
