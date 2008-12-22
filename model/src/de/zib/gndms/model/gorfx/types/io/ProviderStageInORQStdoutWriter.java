package de.zib.gndms.model.gorfx.types.io;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 17:36:36
 */
public class ProviderStageInORQStdoutWriter extends ORQStdoutWriter implements ProviderStageInORQWriter {


    public void writeDataFileName( String dfn ) {
        System.out.println ( "DataFileName: " +dfn );
    }


    public void writeMetaDataFileName( String mfn ) {
        System.out.println ( "MetaDataFileName: " +mfn );
    }


    public DataDescriptorWriter getDataDescriptorWriter() {
        return new DataDescriptorStdoutWriter();
    }


    public void beginWritingDataDescriptor() {
        // Not required here
    }


    public void doneWritingDataDescriptor() {
        // Not required here
    }


    public void begin() {
        System.out.println( "******************** ProviderStageInORQ ********************" );
    }


    public void done() {
        System.out.println( "******************* EOProviderStageInORQ *******************" );
    }
}
