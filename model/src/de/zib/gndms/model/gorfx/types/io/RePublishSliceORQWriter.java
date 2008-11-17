package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.dspace.types.SliceRef;

import java.util.TreeMap;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 12.11.2008, Time: 10:31:26
 */
public interface RePublishSliceORQWriter extends ORQWriter {

    void writeSourceSlice( SliceRef sr );
    void writeFileMap( TreeMap<String,String> tm );
}
