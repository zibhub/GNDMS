package de.zib.gndms.GORFX.action;

import de.zib.gndms.logic.model.gorfx.AbstractTransferORQCalculator;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.types.InterSliceTransferORQ;
import org.apache.axis.types.URI;
import org.globus.ftp.exception.ClientException;
import org.globus.ftp.exception.ServerException;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 15:37:13
 */
public class InterSliceTransferORQCalculator extends 
    AbstractTransferORQCalculator<InterSliceTransferORQ, InterSliceTransferORQCalculator  > {

    public InterSliceTransferORQCalculator() {
        super( InterSliceTransferORQ.class );
    }


    public TransientContract createOffer() throws ServerException, IOException, ClientException {

        checkURIs( );
        
        return super.createOffer();
    }


    protected void checkURIs( ) throws URI.MalformedURIException, RemoteException {

        checkURIs( getORQArguments() );
    }


    public static void checkURIs( InterSliceTransferORQ ist ) throws URI.MalformedURIException, RemoteException {

        if( ist.getSourceURI() == null  )
            ist.setSourceURI(
                DSpaceBindingUtils.getFtpPathForSlice(
                    ist.getSourceSlice() ) );

        if( ist.getTargetURI() == null )
            ist.setTargetURI(
                DSpaceBindingUtils.getFtpPathForSlice(
                    ist.getDestinationSlice() ) );
    }
}
