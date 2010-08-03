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



import de.zib.gndms.model.gorfx.types.InterSliceTransferORQ;
import org.apache.axis.message.MessageElement;
import types.DynamicOfferDataSeqT;
import types.ContextT;
import types.SliceReference;
import types.FileMappingSeqT;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 18:36:24
 */
public class InterSliceTransferORQXSDReader {

    private static final int MIN_ME = 2; // ME = Message Element
    private static final int MAX_ME = 3;

    public static InterSliceTransferORQ read( DynamicOfferDataSeqT orq, ContextT ctx )  {

        try {

            MessageElement[] mes = orq.get_any();

            if( mes.length < MIN_ME )
                throw  new IllegalArgumentException( "Source orq has to few arguments" );
            if( mes.length > MAX_ME )
                throw  new IllegalArgumentException( "Source orq has to many arguments" );

            InterSliceTransferORQ torq = AbstractORQXSDReader.read( InterSliceTransferORQ.class, ctx );

            SliceReference sr =  ( SliceReference ) mes[0].getObjectValue( SliceReference.class );
            torq.setSourceSlice( SliceRefXSDReader.read( sr ) );
            sr = ( SliceReference ) mes[1].getObjectValue( SliceReference.class );
            torq.setDestinationSlice( SliceRefXSDReader.read( sr ) );

            if( mes.length == MAX_ME ) {
                torq.setFileMap(
                     XSDReadWriteAux.read( (FileMappingSeqT) mes[2].getObjectValue( FileMappingSeqT.class ) )
                );
            }

            return torq;
        } catch ( Exception e ) {
            throw new RuntimeException( e.getMessage(), e );  //To change body of catch statement use InterSlice | Settings | InterSlice Templates.
        }
    }
}
