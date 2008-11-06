package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.dspace.types.SliceRef;

import java.util.Map;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 01.10.2008, Time: 16:43:40
 */
public interface InterSliceTransferORQWriter extends ORQWriter {

    public void writeSourceSlice( SliceRef sr );
    public void writeDestinationSlice( SliceRef sr );

    public void writeFileMap( Map<String,String> fm );
}
