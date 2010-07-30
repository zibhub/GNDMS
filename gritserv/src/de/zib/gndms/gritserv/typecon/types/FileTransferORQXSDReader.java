package de.zib.gndms.gritserv.typecon.types;

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



import de.zib.gndms.model.gorfx.types.FileTransferORQ;
import types.ContextT;
import types.DynamicOfferDataSeqT;
import types.FileMappingSeqT;
import org.apache.axis.message.MessageElement;

/**
 * This class is used to create a FileTransfer instance out of a {@code DynamicOfferDataSeqT} object.
 * To opposite way is done by {@link FileTransferORQXSDTypeWriter}.
 *
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 14.10.2008, Time: 13:14:47
 */
public class FileTransferORQXSDReader {

    /**
     * Creates a {@code FileTransferORQ} instance out of a given {@code DynamicOfferDataSeqT} along with its ContextT parameter
     *
     * @param orq
     * @param ctx
     * @return
     */
    public static FileTransferORQ read( DynamicOfferDataSeqT orq, ContextT ctx )  {

        try {
            FileTransferORQ torq = AbstractORQXSDReader.read( FileTransferORQ.class, ctx );

            MessageElement[] mes = orq.get_any();

            if( mes.length < 2 )
                throw  new IllegalArgumentException( "Source orq has to few arguments" );
            if( mes.length > 3 )
                throw  new IllegalArgumentException( "Source orq has to many arguments" );

            torq.setSourceURI( ( String ) mes[0].getObjectValue( String.class ) );
            torq.setTargetURI( ( String ) mes[1].getObjectValue( String.class ) );

            if( mes.length == 3 ) {
                torq.setFileMap(
                     XSDReadWriteAux.read( (FileMappingSeqT) mes[2].getObjectValue( FileMappingSeqT.class ) )
                );
            }

            return torq;
        } catch ( Exception e ) {
            throw new RuntimeException( e.getMessage(), e );
        }
    }
}
