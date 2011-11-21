package de.zib.gndms.GORFX.action;

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



import de.zib.gndms.logic.model.gorfx.AbstractTransferORQCalculator;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.types.InterSliceTransferORQ;
import org.apache.axis.types.URI;
import org.globus.ftp.exception.ClientException;
import org.globus.ftp.exception.ServerException;
import org.globus.gsi.GlobusCredential;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
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

        checkURIs( getORQArguments(), ( GlobusCredential) this.getCredentialProvider().getCredentials().get( 0 ) );
    }


    public static void checkURIs( InterSliceTransferORQ ist, GlobusCredential globusCredential ) throws URI.MalformedURIException, RemoteException {

        if( ist.getSourceURI() == null  )
            ist.setSourceURI(
                DSpaceBindingUtils.getFtpPathForSlice(
                    ist.getSourceSlice(), globusCredential ) );

        if( ist.getTargetURI() == null )
            ist.setTargetURI(
                DSpaceBindingUtils.getFtpPathForSlice(
                    ist.getDestinationSlice(), globusCredential  ) );
    }
}
