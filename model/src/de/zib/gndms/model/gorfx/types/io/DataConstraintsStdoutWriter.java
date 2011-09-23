package de.zib.gndms.model.gorfx.types.io;

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



import de.zib.gndms.model.gorfx.types.io.DataConstraintsWriter;
import de.zib.gndms.model.gorfx.types.io.SpaceConstraintWriter;
import de.zib.gndms.model.gorfx.types.TimeConstraint;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 08.10.2008, Time: 16:44:31
 */
public class DataConstraintsStdoutWriter implements DataConstraintsWriter {

    public void writeTimeConstraint( TimeConstraint timeConstraint ) {
        System.out.println( "TimeConstraint" );
        System.out.println( "    MinTime: " + timeConstraint.getMinTimeString() );
        System.out.println( "    MaxTime: " + timeConstraint.getMaxTimeString() );
    }


    @Override
    public void writeCFList( List<String> CFList ) {
        System.out.println( "CFList: " );
        showStringList( CFList, "    " );
    }


    public void writeConstraintList( Map<String, String> constraintList ) {
        System.out.println( "Constraint list: " );
        Set<String> ks = constraintList.keySet();
        for( String k : ks )
            System.out.println( "    " + k + " ; " + constraintList.get( k ) );
    }

    
    public SpaceConstraintWriter getSpaceConstraintWriter() {
        return new SpaceConstraintStdoutWriter( );

    }


    public void beginWritingSpaceConstraint() {
        System.out.println( "SpaceConstraint" );
    }


    public void doneWritingSpaceConstraint() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    
    public void begin() {
        System.out.println( "********************* DataConstraints *********************" );
    }


    public void done() {
        System.out.println( "******************** EODataConstraints ********************" );
    }


    private void showStringList( List<String> sl, String ind ) {
        if( sl == null )
            System.out.println( ind + "null" );
        else
            for( String s : sl )
                System.out.println( ind + s );
    }
}
