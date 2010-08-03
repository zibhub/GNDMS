package de.zib.gndms.gritserv.typecon.types;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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



import de.zib.gndms.model.gorfx.types.io.CommonSliceResultWriter;
import de.zib.gndms.model.gorfx.types.io.SliceRefWriter;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 12:45:15
 */
public abstract class CommonSliceResultXSDTypeWriter<M> extends AbstractXSDTypeWriter<M>
    implements CommonSliceResultWriter {

    private SliceRefXSDTypeWriter sliceWriter;


    // use the doneWritingSliceRef-method to obtain the result from the
    // sliceWriter and add it to your result.


    public void beginWritingSliceRef() {
        if( sliceWriter == null )
            throw new IllegalStateException( "SliceRefXSDTypeWriter is not present" );
    }


    public SliceRefWriter getSliceRefWriter() {
        if( sliceWriter == null )
            sliceWriter = new SliceRefXSDTypeWriter( );
        return sliceWriter;
    }


    public void begin() {
        // not required here
    }


    public void done() {
        // not required here
    }
}
