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



import de.zib.gndms.model.gorfx.types.io.DataConstraintsWriter;
import de.zib.gndms.model.gorfx.types.io.DataDescriptorWriter;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 17:22:28
 */
public class DataDescriptorStdoutWriter implements DataDescriptorWriter {

    public void writeObjectList( String[] objectList ) {
        System.out.println( "ObjectList: " );
        showStringList( objectList, "    " );
    }


    public void writeDataFormat( String dataFormat ) {
        System.out.println( "dataFormat: " + dataFormat );
    }


    public void writeDataArchiveFormat( String dataArchiveFormat ) {
        System.out.println( "dataArchiveFormat: " + dataArchiveFormat );
    }


    public void writeMetaDataFormat( String metaDataFormat ) {
        System.out.println( "metaDataFormat: " + metaDataFormat );
    }


    public void writeMetaDataArchiveFormat( String metaDataArchiveFormat ) {
        System.out.println( "metaDataArchiveFormat: " + metaDataArchiveFormat );
    }


    public DataConstraintsWriter getDataConstraintsWriter() {
        return new DataConstraintsStdoutWriter();
    }


    public void beginWritingDataConstraints() {
        System.out.println( "Data Constraints: " );
    }


    public void doneWritingDataConstraints() {
        // Not required here
    }


    public void writeJustDownload() {
        System.out.println( "Just Download TRUE" );
    }


    public void begin() {
        System.out.println( "********************* DataDescriptor *********************" );
    }

    public void done() {
        System.out.println( "******************** EODataDescriptor ********************" );
    }

    private void showStringList( String[] sl, String ind ) {
        if( sl == null )
            System.out.println( ind + "null" );
        for( int i=0; i < sl.length; ++i )
            System.out.println( ind + sl[i] );
    }
}
