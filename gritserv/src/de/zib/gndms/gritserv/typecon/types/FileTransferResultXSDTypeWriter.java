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



import de.zib.gndms.model.gorfx.types.io.FileTransferResultWriter;
import de.zib.gndms.gritserv.typecon.GORFXClientTools;
import org.apache.axis.description.FieldDesc;
import org.apache.axis.message.MessageElement;
import types.FileTransferResultT;

import javax.xml.soap.SOAPException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 13:18:40
 */
public class FileTransferResultXSDTypeWriter extends AbstractXSDTypeWriter<FileTransferResultT> implements FileTransferResultWriter {


    public void writeFiles( String[] files ) {

        FieldDesc fd = FileTransferResultT.getTypeDesc().getFieldByName( "file" );
        ArrayList<MessageElement> al = new ArrayList<MessageElement>( Arrays.asList( getProduct().get_any() ) );
        for( int i=0; i < files.length; ++i ) {
            try {
                al.add( GORFXClientTools.createElementForField( fd, files[i] ) );
            } catch ( SOAPException e ) {
                throw new IllegalStateException( "SOAPException occurred: " + e.getMessage( ) );
            }
        }

        getProduct().set_any( al.toArray( new MessageElement[al.size()] ) );
    }


    public void begin() {
        try {
            setProduct( GORFXClientTools.createFileTransferResultT( )  );
        } catch ( SOAPException e ) {
            e.printStackTrace();
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
        } catch ( InstantiationException e ) {
            e.printStackTrace();
        }
    }


    public void done() {
        // Not required here
    }
}
