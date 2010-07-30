package de.zib.gndms.model.gorfx.types.io;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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
