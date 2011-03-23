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



import types.SpaceConstraintT;
import types.MinMaxT;
import de.zib.gndms.model.gorfx.types.io.SpaceConstraintWriter;
import de.zib.gndms.model.gorfx.types.MinMaxPair;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 16.10.2008, Time: 10:51:48
 */
public class SpaceConstrantXSDTypeWriter extends AbstractXSDTypeWriter<SpaceConstraintT> implements SpaceConstraintWriter {

    public void writeLatitude( MinMaxPair lat ) {
        getProduct().setLatitude( createMinMaxT( lat ) );
    }


    public void writeLongitude( MinMaxPair lon ) {
        getProduct().setLongitude( createMinMaxT( lon ) );
    }


    public void writeAltitude( MinMaxPair alt ) {
        getProduct().setAltitude( createMinMaxT( alt ) );
    }


    public void writeVerticalCRS( String verticalCRS ) {
        getProduct( ).setVerticalCRS( verticalCRS );
    }


    public void writeAreaCRS( String areaCRS ) {
        getProduct( ).setAreaCRS( areaCRS );
    }


    public void begin() {
        setProduct( new SpaceConstraintT( ) );
    }


    public void done() {
        // Not required here
    }
    

    public static MinMaxT createMinMaxT( MinMaxPair mmp ) {
        MinMaxT mmt = new MinMaxT( );
        mmt.setMin( mmp.getMinValue() );
        mmt.setMax( mmp.getMaxValue() );
        
        return mmt;
    }
}
