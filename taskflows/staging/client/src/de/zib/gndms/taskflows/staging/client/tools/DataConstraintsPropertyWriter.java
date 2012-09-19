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



import de.zib.gndms.common.model.gorfx.types.io.AbstractPropertyIO;
import de.zib.gndms.common.model.gorfx.types.io.PropertyReadWriteAux;
import de.zib.gndms.common.model.gorfx.types.io.SfrProperty;
import de.zib.gndms.taskflows.staging.client.model.TimeConstraint;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 08.10.2008, Time: 14:56:59
 */
public class DataConstraintsPropertyWriter extends AbstractPropertyIO implements DataConstraintsWriter {


    public DataConstraintsPropertyWriter() {
    }


    public DataConstraintsPropertyWriter( Properties properties ) {
        super( properties );
    }


    public void writeTimeConstraint( @NotNull TimeConstraint timeConstraint ) {

        if( timeConstraint.hasMinTime( ) )
            TimeConstraintPropertyWriter.writeMinTimeToProperties( getProperties(), timeConstraint.getMinTime( ) );

        if( timeConstraint.hasMaxTime( ) )
            TimeConstraintPropertyWriter.writeMaxTimeToProperties( getProperties(), timeConstraint.getMaxTime( ) );
        
        if( timeConstraint.hasAggregation() )
            TimeConstraintPropertyWriter.writeAggregationToProperties( getProperties(), timeConstraint.getAggregation() );
    }


    public void writeCFList( @NotNull List<String> cflist ) {
        PropertyReadWriteAux.writeListMultiLine( getProperties(), SfrProperty.CFLIST_ITEMS.key, cflist );
        PropertyReadWriteAux.writeList( getProperties(), SfrProperty.CFLIST_OLD.key, ' ', cflist );
    }


    public void writeConstraintList( @NotNull Map<String, String> constraintList ) {
        PropertyReadWriteAux.writeMap( getProperties( ), SfrProperty.CONSTRAINT_LIST.key, constraintList );
    }


    public SpaceConstraintWriter getSpaceConstraintWriter() {
        return new SpaceConstraintPropertyWriter( getProperties( ) );
    }


    public void beginWritingSpaceConstraint() {
        // not required here
    }


    public void doneWritingSpaceConstraint() {
        // not required here
    }


    public void done() {
        // not required here
    }
}
