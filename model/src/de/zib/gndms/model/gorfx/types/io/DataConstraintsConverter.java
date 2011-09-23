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



import de.zib.gndms.model.gorfx.types.DataConstraints;
import de.zib.gndms.model.gorfx.types.SpaceConstraint;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 08.10.2008, Time: 14:27:25
 */
public class DataConstraintsConverter extends GORFXConverterBase<DataConstraintsWriter, DataConstraints> {

    public DataConstraintsConverter() {
    }


    public DataConstraintsConverter( DataConstraintsWriter writer, DataConstraints model ) {
        super( writer, model );
    }


    public void convert() {

        if( getWriter( ) == null || getModel() == null )
            throw new IllegalStateException( );

        getWriter().begin();
        
        SpaceConstraint sc = getModel().getSpaceConstraint();
        if( sc == null )
            sc = new SpaceConstraint();
        SpaceConstraintWriter scw = getWriter().getSpaceConstraintWriter();
        getWriter( ).beginWritingSpaceConstraint();
        SpaceConstraintConverter scc = new SpaceConstraintConverter( scw, sc );
        scc.convert();
        getWriter( ).doneWritingSpaceConstraint();


        if( getModel().hasTimeConstraint() )
            getWriter().writeTimeConstraint( getModel().getTimeConstraint() );

        getWriter().writeCFList( NotNullStringList( getModel().getCFList() ) );

        if( getModel().hasConstraintList() )
            getWriter().writeConstraintList( getModel().getConstraintList() );
        
        getWriter().done( );
    }
}
