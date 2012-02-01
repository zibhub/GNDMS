package de.zib.gndms.taskflows.staging.client.model;

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

import java.io.Serializable;


/**
 * A space constrain is a selection criteria for data stagin.
 * 
 * @author  try ma ik jo rr a zib
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 15, 2008 4:10:44 PM
 *
 */
public class SpaceConstraint implements Serializable {

    /**
     * Pair for the latitude.
     *
     * This is required
     */
    private MinMaxPair latitude; // required

    /**
     * Pair for the longitude.
     *
     * This is required
     */
    private MinMaxPair longitude; // required

    /**
     * Pair for the altitude.
     *
     * This is optional
     */
    private MinMaxPair altitude; // optional

    /**
     * Some area constraints.
     *
     * This are optional.
     */
    private String areaCRS; // optional

    /**
     * Some vertical constraints.
     *
     * This are optional.
     */
    private String verticalCRS; // optional

    private static final long serialVersionUID = -6224923031075051128L;


    /**
     * Checks if the altitude constraint is present in this instance.
     * @return True if thats the case.
     */
    public boolean hasAltitude( ) {
        return altitude != null;
    }


    /**
     * Checks if the area constraints are present in this instance.
     * @return True if thats the case.
     */
    public boolean hasAreaCRS( ) {
        return areaCRS != null && areaCRS.length() != 0;
    }


    /**
     * Checks if the vertical constraints are present in this instance.
     * @return True if thats the case.
     */
    public boolean hasVerticalCRS( ) {
        return verticalCRS != null && verticalCRS.length() != 0;
    }


    public MinMaxPair getLatitude() {
        return latitude;
    }


    public void setLatitude( MinMaxPair latitude ) {
        this.latitude = latitude;
    }


    public MinMaxPair getLongitude() {
        return longitude;
    }


    public void setLongitude( MinMaxPair longitude ) {
        this.longitude = longitude;
    }


    public MinMaxPair getAltitude() {
        return altitude;
    }


    public void setAltitude( MinMaxPair altitude ) {
        this.altitude = altitude;
    }


    public String getAreaCRS() {
        return areaCRS;
    }


    public void setAreaCRS( String areaCRS ) {
        this.areaCRS = areaCRS;
    }


    public String getVerticalCRS() {
        return verticalCRS;
    }


    public void setVerticalCRS( String verticalCRS ) {
        this.verticalCRS = verticalCRS;
    }
}
