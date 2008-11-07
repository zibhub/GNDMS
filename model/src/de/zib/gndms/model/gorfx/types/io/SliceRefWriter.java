package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.dspace.types.SliceRef;

import javax.xml.namespace.QName;

/**
 * Writes a SliceRef.
 *
 * When implementing this interface one have to choices depending on the
 * target of the write-operation. Either the whole slice can be written at once
 * implementing the writeSliceRef-method, or the parts of the reference, i.e.
 * the id, name and url can be written separately.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 13:27:34
 */
public interface SliceRefWriter extends GORFXWriterBase {

    // writes the complete ref
    void writeSliceRef( SliceRef sf );

    // writes the components of the ref separately
    void writeSliceGridSite( String site );
    void writeSliceId( String id );
    void writeSliceResourceName( QName name );
}
