package de.zib.gndms.model.gorfx.types.io;

/**
 * Created by IntelliJ IDEA.
 * User: roberto
 * Date: 13.10.2008
 * Time: 15:38:45
 * To change this template use File | Settings | File Templates.
 */
public interface LofiSetStageInORQWriter extends ORQWriter {

    public void writeGridSiteName ( String gsn );
    public void writeDataFileName( String dfn );
    public void writeMetaDataFileName( String mfn );

    // see DataDescriptorWriter for an explanation of the following methods
    public DataDescriptorWriter getDataDescriptorWriter( );
    public void beginWritingDataDescriptor( );
    public void doneWritingDataDescriptor( );
    
}
