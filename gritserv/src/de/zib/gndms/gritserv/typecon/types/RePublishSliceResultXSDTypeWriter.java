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



import de.zib.gndms.model.gorfx.types.RePublishSliceResult;
import de.zib.gndms.model.gorfx.types.io.RePublishSliceResultConverter;
import de.zib.gndms.gritserv.typecon.GORFXClientTools;
import types.RePublishSliceResultT;

import javax.xml.soap.SOAPException;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 17.11.2008, Time: 10:14:21
 */
public class RePublishSliceResultXSDTypeWriter extends CommonSliceResultXSDTypeWriter<RePublishSliceResultT> {



    public void doneWritingSliceRef() {
        if( getSliceRefWriter( ) == null )
            throw new IllegalStateException( "no slice ref writer present" );

        try {
            getProduct( ).get_any()[0].setObjectValue( ( (SliceRefXSDTypeWriter) getSliceRefWriter() ).getProduct() );
        } catch ( SOAPException e ) {
            throw new RuntimeException( e.getMessage(), e );
        }
    }


    public void begin() {
        try {
            setProduct( GORFXClientTools.createRePublishSliceResultT( )  );
        } catch ( SOAPException e ) {
            e.printStackTrace();
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
        } catch ( InstantiationException e ) {
            e.printStackTrace();
        }
    }


    public static RePublishSliceResultT writeResult( RePublishSliceResult res ) {

        final RePublishSliceResultXSDTypeWriter writer = new RePublishSliceResultXSDTypeWriter();
        final RePublishSliceResultConverter conv = new RePublishSliceResultConverter( writer, res );
        conv.convert();

        return writer.getProduct();
    }
}
