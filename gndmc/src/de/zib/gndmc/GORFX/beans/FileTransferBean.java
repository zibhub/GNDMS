package de.zib.gndmc.GORFX.beans;

import java.util.Properties;

/**
 * @author Maik Jorra <jorra@zib.de>
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
