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



import de.zib.gndms.model.gorfx.types.DataConstraints;

import java.util.Properties;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 08.10.2008, Time: 16:09:55
 */
public class DataConstraintsPropertyReader extends AbstractPropertyReader<DataConstraints> {

    public DataConstraintsPropertyReader( ) {
        super( DataConstraints.class );
    }


    public DataConstraintsPropertyReader( Properties properties ) {
        super( DataConstraints.class, properties );
    }


    @Override
    public void read() {

        getProduct( ).setSpaceConstraint( SpaceConstraintPropertyReader.readSpaceConstraint( getProperties() ) );

        getProduct( ).setTimeConstraint( TimeConstraintPropertyReader.readTimeConstraint( getProperties() ) );

        // read cfl
        String [] cfl =  PropertyReadWriteAux.readListMultiLine( getProperties(), SfrProperty.CFLIST_ITEMS.key );
        if( cfl == null )
            cfl = PropertyReadWriteAux.readList( getProperties(), SfrProperty.CFLIST_OLD.key, ' ' );
        getProduct( ).setCFList( cfl );

        if( getProperties().containsKey( SfrProperty.CONSTRAINT_LIST.key ) )
            getProduct( ).setConstraintList(
                PropertyReadWriteAux.readMap( getProperties(), SfrProperty.CONSTRAINT_LIST.key ) );
    }


    public void done() {
        // Not required here
    }


    public static DataConstraints readDataConstraints( Properties prop ) {

        DataConstraintsPropertyReader dc = new DataConstraintsPropertyReader( prop );
        dc.performReading();
        return dc.getProduct( );
    }
}
