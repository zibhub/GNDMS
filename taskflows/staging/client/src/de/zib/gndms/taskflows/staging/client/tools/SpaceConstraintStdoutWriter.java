package de.zib.gndms.taskflows.staging.client.tools;

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



import de.zib.gndms.common.model.gorfx.types.MinMaxPair;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 23.09.2008, Time: 12:55:38
 */
public class SpaceConstraintStdoutWriter implements SpaceConstraintWriter {

    public void writeLatitude( MinMaxPair lat ) {

        System.out.println( "  Latitude:" );
        showMinMax( lat, "    " );
    }


    public void writeLongitude( MinMaxPair lon ) {
        System.out.println( "  Longitude:" );
        showMinMax( lon, "    " );
    }


    public void writeAltitude( MinMaxPair alt ) {
        System.out.println( "  Altitude:" );
        showMinMax( alt, "    " );
    }
    
    
    public void writeAltitudeRange( String altRange) {
        System.out.println( "  AltitudeRange:" + altRange );
    }


    public void writeAreaCRS( String crs ) {
        System.out.println( "AreaCRS:" + crs );
    }


    public void writeVerticalCRS( String crs ) {
        System.out.println( "VerticalCRS:" + crs );
    }


    public void begin() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void done() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void showMinMax( MinMaxPair mmp, String ind ) {
        if( mmp == null )
            System.out.println( ind + "null" );
        else {
            System.out.println( ind + "Min: " + mmp.getMinValue() );
            System.out.println( ind + "Max: " + mmp.getMaxValue() );
        }
    }
}
