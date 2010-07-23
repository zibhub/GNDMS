package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.io.ORQStdoutWriter;
import de.zib.gndms.model.gorfx.types.io.SliceStageInORQWriter;
import de.zib.gndms.model.gorfx.types.io.DataDescriptorWriter;
import de.zib.gndms.model.gorfx.types.io.DataDescriptorStdoutWriter;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 17:36:36
 */
public class SliceStageInORQStdoutWriter extends ORQStdoutWriter implements
	  SliceStageInORQWriter {


	public void writeGridSiteName(final String gsn) {
		System.out.println ( "GridSite: " + gsn );
	}


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
        System.out.println( "******************** SliceStageInORQ ********************" );
    }


    public void done() {
        System.out.println( "******************* EOSliceStageInORQ *******************" );
    }
}