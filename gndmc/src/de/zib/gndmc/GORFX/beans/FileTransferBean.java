package de.zib.gndmc.GORFX.beans;

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



import java.util.Properties;

/**
 * @author  try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 26.07.2010, Time: 15:12:14
 */
public class FileTransferBean extends GorfxBean {


    public final static String SOURCE_ADDRESS_KEY = "GORFX.fileTransfer.sourceAddress";
    public final static String DESTINATION_ADDRESS_KEY = "GORFX.fileTransfer.destinationAddress";

    private String sourceAddress;
    private String destinationAddress;

    // todo add optional FileTransfer ORQ field

    @Override
    public void setProperties( Properties prop ) {
        super.setProperties( prop );

        sourceAddress = prop.getProperty( SOURCE_ADDRESS_KEY );
        destinationAddress = prop.getProperty( DESTINATION_ADDRESS_KEY );
    }


    public String getSourceAddress() {
        return sourceAddress;
    }


    public void setSourceAddress( String sourceAddress ) {
        this.sourceAddress = sourceAddress;
    }


    public String getDestinationAddress() {
        return destinationAddress;
    }


    public void setDestinationAddress( String destinationAddress ) {
        this.destinationAddress = destinationAddress;
    }


    @Override
    public void createExampleProperties( Properties prop ) {
        super.createExampleProperties( prop );
        prop.setProperty( SOURCE_ADDRESS_KEY, "<the-gsiftp-address-of-the-source-file>" );
        prop.setProperty( DESTINATION_ADDRESS_KEY, "<the-gsiftp-address-of-the-destination-file>" );
    }
}
