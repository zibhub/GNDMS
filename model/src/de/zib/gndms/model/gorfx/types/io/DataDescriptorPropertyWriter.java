package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.TimeConstraint;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Builder to export a data descriptor into a property file.
 *
 * NOTE loading and storing of the Properties must be performed by their provider.
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 17.09.2008, Time: 15:34:01
 */
public class DataDescriptorPropertyWriter extends AbstractPropertyIO implements DataDescriptorWriter {

    public DataDescriptorPropertyWriter() {
        super( );
    }


    public DataDescriptorPropertyWriter( @NotNull Properties properties ) {
        super( properties );
    }


    public void writeObjectList( @NotNull String[] objectList ) {
        PropertyReadWriteAux.writeListMultiLine( getProperties(), SfrProperty.OBJECT_ITEMS.key, Arrays.asList( objectList ) );
    }



    public void writeDataFormat( @NotNull String dataFormat ) {
        getProperties().setProperty( SfrProperty.FILE_FORMAT.key, dataFormat );
    }


    public void writeDataArchiveFormat( String dataArchiveFormat ) {
        getProperties().setProperty( SfrProperty.FILE_ARCHIVE_FORMAT.key, dataArchiveFormat );
    }


    public void writeMetaDataFormat( @NotNull String metaDataFormat ) {
        getProperties().setProperty( SfrProperty.META_FILE_FORMAT.key, metaDataFormat );
    }


    public void writeMetaDataArchiveFormat( String metaDataArchiveFormat ) {
        getProperties().setProperty( SfrProperty.META_FILE_ARCHIVE_FORMAT.key, metaDataArchiveFormat );
    }


    public void writeJustDownload() {
        getProperties().setProperty( SfrProperty.JUST_DOWNLOAD.key, "true" );
    }


    public DataConstraintsWriter getDataConstraintsWriter() {
        return new DataConstraintsPropertyWriter( getProperties() );
    }

    
    public void beginWritingDataConstraints() {
        // Not required here
    }


    public void doneWritingDataConstraints() {
        // Not required here
    }


    public void done() {
        // Not required here
    }
}
