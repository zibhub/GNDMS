package de.zib.gndms.gritserv.typecon.types;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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



import de.zib.gndms.model.gorfx.types.io.DataDescriptorWriter;
import de.zib.gndms.model.gorfx.types.io.DataConstraintsWriter;
import types.DataDescriptorT;
import types.ObjectListT;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 16.10.2008, Time: 09:03:14
 */
public class DataDescriptorXSDTypeWriter extends AbstractXSDTypeWriter<DataDescriptorT> implements DataDescriptorWriter {

    private DataConstraintsXSDTypeWriter constraintWriter;

    public void writeObjectList( String[] objectList ) {
        getProduct().setObjectList( new ObjectListT( objectList ) );
    }


    public void writeDataFormat( String dataFormat ) {
        getProduct().setDataFormat( dataFormat );
    }


    public void writeDataArchiveFormat( String dataArchiveFormat ) {
        getProduct().setDataArchiveFormat( dataArchiveFormat );
    }


    public void writeMetaDataFormat( String metaDataFormat ) {
        getProduct().setMetaDataFormat( metaDataFormat );
    }


    public void writeMetaDataArchiveFormat( String metaDataArchiveFormat ) {
        getProduct().setMetaDataArchiveFormat( metaDataArchiveFormat );
    }


    public DataConstraintsWriter getDataConstraintsWriter() {
        if( constraintWriter == null )
            constraintWriter = new DataConstraintsXSDTypeWriter();
        return constraintWriter;
    }


    public void beginWritingDataConstraints() {
        if( constraintWriter == null )
            throw new IllegalStateException( "no constraint writer present" );
    }


    public void doneWritingDataConstraints() {
        if( constraintWriter == null )
            throw new IllegalStateException( "no constraint writer present" );

        getProduct( ).setConstraints( constraintWriter.getProduct( ) );
    }


    public void writeJustDownload() {
        // not required in this context
    }


    public void begin() {
        setProduct( new DataDescriptorT( ) );
    }


    public void done() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
