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



import de.zib.gndms.model.gorfx.types.MinMaxPair;

import java.util.Properties;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 17.09.2008, Time: 15:16:08
 */
public class SpaceConstraintPropertyWriter extends AbstractPropertyIO implements SpaceConstraintWriter {


    public SpaceConstraintPropertyWriter( Properties properties ) {
        super( properties );
    }


    public SpaceConstraintPropertyWriter() {
        super( );
    }


    public void writeLatitude( MinMaxPair lat ) {
        getProperties( ).setProperty( SfrProperty.LAT_MIN.key, Double.toString( lat.getMinValue( ) ) );
        getProperties( ).setProperty( SfrProperty.LAT_MAX.key, Double.toString( lat.getMaxValue( ) ) );
    }


    public void writeLongitude( MinMaxPair lon ) {
        getProperties( ).setProperty( SfrProperty.LON_MIN.key, Double.toString( lon.getMinValue( ) ) );
        getProperties( ).setProperty( SfrProperty.LON_MAX.key, Double.toString( lon.getMaxValue( ) ) );
    }
    

    public void writeAltitude( MinMaxPair alt ) {
        getProperties( ).setProperty( SfrProperty.ALT_MIN.key, Double.toString( alt.getMinValue( ) ) );
        getProperties( ).setProperty( SfrProperty.ALT_MAX.key, Double.toString( alt.getMaxValue( ) ) );
    }


    public void writeAreaCRS( String acrs ) {
        getProperties().setProperty( SfrProperty.AREA_CRS.key, acrs );
    }


    public void writeVerticalCRS( String vcrs ) {
        getProperties().setProperty( SfrProperty.ALT_VCRS.key, vcrs );
    }


    public void done() {
    }
}
