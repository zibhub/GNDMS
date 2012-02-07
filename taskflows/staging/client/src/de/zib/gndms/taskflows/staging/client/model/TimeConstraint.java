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



import org.joda.time.DateTime;

import java.io.Serializable;


/**
 * A time constrain is a selection criteria for data stage-in.
 * @author  try ma ik jo rr a zib
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 15, 2008 4:11:00 PM
 */
public class TimeConstraint implements Serializable {

    /**
     * The lower bound for the time.
     *
     * The string should be formatted in ISO 8601, but isn't validated. It may also contain some
     * prehistorical time stamps
     */
	private String minTime;

    /**
     * The upper bound for the time span.
     *
     * The string should be formatted in ISO 8601, but isn't validated. It may also contain some
     * prehistorical time stamps
     */
    private String maxTime;

    private static final long serialVersionUID = 2682486067028199165L;


    /**
     * Checks if #minTime is present.
     * @return True if thats the case.
     */
    public boolean hasMinTime() {
        return minTime != null;
    }

    /**
     * Delives the value of #minTime.
     * @return Some string containg time...
     */
    public String getMinTime() {
        return minTime;
    }



    /**
     * Same as above...
     * @deprecated
     */
    public String getMinTimeString() {
        return minTime;
    }


    /**
     * Sets the value of minTime.
     * @param minTime A string contain a vaild time stamp.
     */
    public void setMinTime( String minTime ) {
        this.minTime = minTime;
    }


    /**
     * Sets the value of minTime form the given time object.
     * @param minTime A dateTime object representing the minTime.
     */
    public void setMinTime( DateTime minTime ) {
        this.minTime = minTime.toString();
    }


    /**
     * Checks if #maxTime is present.
     * @return True if thats the case.
     */
    public boolean hasMaxTime() {
        return maxTime != null;
    }


    /**
     * Delives the value of #maxTime.
     * @return Some string containg time...
     */
    public String getMaxTime() {
        return maxTime;
    }


    /**
     * Same as above...
     * @deprecated
     */
    public String getMaxTimeString() {
        return maxTime;
    }


    /**
     * Sets the value of maxTime.
     * @param maxTime A string contain a vaild time stamp.
     */
    public void setMaxTime( String maxTime ) {
        this.maxTime = maxTime;
    }


    /**
     * Sets the value of maxTime form the given time object.
     * @param maxTime A dateTime object representing the maxTime.
     */
    public void setMaxTime( DateTime maxTime ) {
        this.maxTime = maxTime.toString( );
    }
}
