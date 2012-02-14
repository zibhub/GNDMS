package de.zib.gndms.taskflows.interslicetransfer.server.logic;

/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.dspace.service.DSpaceBindingUtils;
import de.zib.gndms.taskflows.filetransfer.server.logic.AbstractTransferQuoteCalculator;
import de.zib.gndms.taskflows.interslicetransfer.client.model.InterSliceTransferOrder;
import org.globus.ftp.exception.ClientException;
import org.globus.ftp.exception.ServerException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 15:37:13
 */
public class InterSliceTransferQuoteCalculator extends
    AbstractTransferQuoteCalculator<InterSliceTransferOrder> {

    public InterSliceTransferQuoteCalculator() {
    }


    public List<Quote> createQuotes() throws ServerException, IOException, ClientException, URISyntaxException {

        checkURIs( );
        
        return super.createQuotes();
    }


    @Override
    public boolean validate() {
        checkURIs();
        return super.validate();  // not required here
    }


    protected void checkURIs( ) {

        checkURIs( getOrderBean() );
    }


    public static void checkURIs( InterSliceTransferOrder ist ) {

        if( ist.getSourceURI() == null  )
            ist.setSourceURI(
                DSpaceBindingUtils.getFtpPathForSlice(
                    ist.getSourceSlice() ) );

        if( ist.getDestinationURI() == null )
            ist.setDestinationURI(
                DSpaceBindingUtils.getFtpPathForSlice(
                    ist.getDestinationSlice() ) );
    }
}
