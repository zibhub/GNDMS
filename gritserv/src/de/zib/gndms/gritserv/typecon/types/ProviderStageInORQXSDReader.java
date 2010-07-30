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



import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.types.DataDescriptor;
import de.zib.gndms.gritserv.typecon.GORFXClientTools;
import de.zib.gndms.gritserv.typecon.GORFXTools;
import types.DynamicOfferDataSeqT;
import types.DataDescriptorT;
import types.ContextT;
import org.apache.axis.message.MessageElement;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 14.01.2009, Time: 13:35:41
 */
public class ProviderStageInORQXSDReader {

    // todo implement this using builder form model.gorfx
    public static ProviderStageInORQ read( DynamicOfferDataSeqT orqt, ContextT ctx ) throws Exception, InstantiationException, IllegalAccessException {

        if(! orqt.getOfferType().equals( GORFXClientTools.getProviderStageInURI() ) )
            throw new IllegalArgumentException( );

        return read( ProviderStageInORQ.class, orqt, ctx, 0 );
    }


    // reads the ProviderStageInORQ component from an abritray offerRequest.
    // @param idx is the start index of the component.
    public static <T extends ProviderStageInORQ> T read( Class<T> clazz, DynamicOfferDataSeqT orqt, ContextT ctx, int idx ) {

        T orq = null;
        try {
            orq = AbstractORQXSDReader.read( clazz, ctx );

            DataDescriptor dds = GORFXTools.convertDataDescriptorT( ( DataDescriptorT ) orqt.get_any()[idx].getObjectValue( DataDescriptorT.class ) );
            orq.setDataDescriptor( dds );

            MessageElement[] mes = orqt.get_any();
            for( int i = idx + 1; i < mes.length; ++i ) {
                MessageElement me = mes[i];
                if( me.getElementName().getLocalName().equals( "DataFile" ) )
                    orq.setActDataFile( (String) me.getObjectValue( String.class ) );
                else
                    orq.setActMetadataFile( (String) me.getObjectValue( String.class ) );
            }

            return orq;
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }



}
