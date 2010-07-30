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



import de.zib.gndms.model.gorfx.types.TimeConstraint;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
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
    }


    public void writeCFList( @NotNull String[] cflist ) {
        List<String> cfl = Arrays.asList( cflist );
        PropertyReadWriteAux.writeListMultiLine( getProperties(), SfrProperty.CFLIST_ITEMS.key, cfl );
        PropertyReadWriteAux.writeList( getProperties(), SfrProperty.CFLIST_OLD.key, ' ', cfl );
    }


    public void writeConstraintList( @NotNull HashMap<String, String> constraintList ) {
        PropertyReadWriteAux.writeMap( getProperties(), SfrProperty.CONSTRAINT_LIST.key, constraintList );
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
