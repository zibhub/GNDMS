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



import de.zib.gndms.model.gorfx.types.FileTransferOrder;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 16:30:22
 */
public class FileTransferQuoteCalculator extends AbstractTransferQuoteCalculator<FileTransferOrder> {


    public FileTransferQuoteCalculator() {
        super( );
        setScheme( "gsiftp" );
    }


    /**
     * Weak order validation.
     *
     * Just checks if source and destination URI are provided and are valid URI's. but NOT if the resources exist.
     *
     * @return true if the parameters are set.
     */
    @Override
    public boolean validate() {
        FileTransferOrder order = getOrderBean();
        if( order == null )
            throw new IllegalStateException( "No order provided" );

        return uriCheck( order.getSourceURI() ) && uriCheck( order.getDestinationURI() );
    }
}