package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.DataDescriptor;

/**
 * Interface for a ProviderStageInORQ builder
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 10:06:00
 */
public interface ProviderStageInORQWriter extends ORQWriter {

    public void writeDataFileName( String dfn );
    public void writeMetaDataFileName( String mfn );

    // see DataDescriptorWriter for an explanation of the following methods
    public DataDescriptorWriter getDataDescriptorWriter( );
    public void beginWritingDataDescriptor( );
    public void doneWritingDataDescriptor( );
}
