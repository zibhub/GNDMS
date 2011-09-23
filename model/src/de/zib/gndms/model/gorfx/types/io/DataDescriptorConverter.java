package de.zib.gndms.model.gorfx.types.io;

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



import de.zib.gndms.model.gorfx.types.DataDescriptor;

/**
 * Worker for converting data descriptor instances.
 *
 * The converter itself doesn't any real conversion, it just calls the
 * appropriate methods of the provided DataDescriptorWriter.
 * SEE DataDescriptorWriter for details.
 * 
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 17.09.2008, Time: 17:32:18
 */
public class DataDescriptorConverter extends GORFXConverterBase<DataDescriptorWriter, DataDescriptor> {


    public DataDescriptorConverter( ) {
        super( );
    }


    public DataDescriptorConverter( DataDescriptorWriter writer, DataDescriptor dataDescriptor ) {
        super( writer, dataDescriptor );
    }


    public void convert( ) {

        if( getWriter( ) == null || getModel() == null )
            throw new IllegalStateException( );

        getWriter().begin();
        getWriter().writeObjectList( NotNullStringList( getModel().getObjectList() ) );

        
        if( getModel().hasConstraints() ) {
            DataConstraintsWriter dcw = getWriter( ).getDataConstraintsWriter( );
            getWriter().beginWritingDataConstraints( );
            DataConstraintsConverter dcc = new DataConstraintsConverter( dcw, getModel().getConstrains() );
            dcc.convert();
            getWriter().doneWritingDataConstraints( );
        } else
            getWriter().writeJustDownload( );


        if( getModel().hasDataFormat() )
            getWriter().writeDataFormat( NotNullString( getModel().getDataFormat() ) );

        if( getModel().hasDataArchiveFormat() )
            getWriter().writeDataArchiveFormat( getModel().getDataArchiveFormat() );

        getWriter().writeMetaDataFormat( NotNullString( getModel().getMetaDataFormat() ) );

        if( getModel().hasMetaDataArchiveFormat() )
            getWriter().writeMetaDataArchiveFormat( getModel().getMetaDataArchiveFormat() );

        getWriter().done();
    }
}
