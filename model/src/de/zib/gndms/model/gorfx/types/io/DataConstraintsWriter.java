package de.zib.gndms.model.gorfx.types.io;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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

import java.util.HashMap;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 08.10.2008, Time: 14:28:23
 */
public interface DataConstraintsWriter extends GORFXWriterBase {

    //no writeSpaceConstraint see below
    public void writeTimeConstraint ( TimeConstraint timeConstraint );
    public void writeCFList( String[] CFList );
    public void writeConstraintList( HashMap<String,String> constraintList );

    
    // Used by the converter to write a space constraint
    public SpaceConstraintWriter getSpaceConstraintWriter( );

    // The following methods are called by the DataDescriptorConverter immediatly before and
    // after the write of the space constraint is triggered.
    //
    // Uses the following methods to perform context switches on your writer
    // if required
    public void beginWritingSpaceConstraint( );
    public void doneWritingSpaceConstraint( );
}
