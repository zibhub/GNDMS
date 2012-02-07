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



import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * Container for data constrains in staging requests.
 *
 * @author  try ma ik jo rr a zib
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 15, 2008 4:08:44 PM
 */
public class DataConstraints implements Serializable {

    /**
     * A space constraint.
     *
     * Like longitude or height-level
     *
     * This is required.
     */
    private SpaceConstraint spaceConstraint; // required

    /**
     * A time span for the data.
     *
     * This is optional.
     */
    private TimeConstraint timeConstraint; // not required

    /**
     * Some data flags.
     *
     * This is required.
     */
    @SuppressWarnings({ "InstanceVariableNamingConvention" })
    private List<String> CFList; // required

    /**
     * Additional constraints.
     *
     * This is required.
     */
    private Map<String, String> constraintList; // required

    
    private static final long serialVersionUID = 3332239957438316526L;


    public SpaceConstraint getSpaceConstraint() {
        return spaceConstraint;
    }


    public void setSpaceConstraint( SpaceConstraint spaceConstraint ) {
        this.spaceConstraint = spaceConstraint;
    }


    public boolean hasTimeConstraint() {
        return timeConstraint != null;
    }


    public TimeConstraint getTimeConstraint() {
        return timeConstraint;
    }


    public void setTimeConstraint( TimeConstraint timeConstraint ) {
        this.timeConstraint = timeConstraint;
    }



    public List<String> getCFList() {
        return CFList;
    }


    public void setCFList( List<String> CFList ) {
        this.CFList = CFList;
    }


    public boolean hasConstraintList( ) {
        return constraintList != null && constraintList.size() > 0;
    }


    public Map<String, String> getConstraintList() {
        return constraintList;
    }


    public void setConstraintList( Map<String, String> constraintList ) {
        this.constraintList = constraintList;
    }
}
