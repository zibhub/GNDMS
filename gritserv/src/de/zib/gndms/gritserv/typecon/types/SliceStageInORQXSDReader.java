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



import de.zib.gndms.model.gorfx.types.SliceStageInORQ;
import types.ContextT;
import types.DynamicOfferDataSeqT;
import org.apache.axis.message.MessageElement;

/**
 * @author  try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 14.01.2009, Time: 12:41:15
 */
public class SliceStageInORQXSDReader {

    public static SliceStageInORQ read(  DynamicOfferDataSeqT orqt, ContextT ctx ) {

        try {
            MessageElement[] mes = orqt.get_any();

            int idx = 0;
            MessageElement me = mes[idx];
            String gs = null;
            if( me.getElementName().getLocalName().equals( "GridSite" ) ) {
                gs = (String) me.getObjectValue( String.class );
                ++idx;
            }
            SliceStageInORQ orq = ProviderStageInORQXSDReader.read( SliceStageInORQ.class, orqt, ctx, idx );
            orq.setGridSite( gs );
            return orq;
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }
    
}
