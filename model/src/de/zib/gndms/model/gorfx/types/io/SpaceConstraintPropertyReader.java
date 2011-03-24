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



import de.zib.gndms.model.gorfx.types.SpaceConstraint;

import java.util.Properties;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 13:42:12
 */
public class SpaceConstraintPropertyReader extends AbstractPropertyReader<SpaceConstraint> {

    public SpaceConstraintPropertyReader() {
        super( SpaceConstraint.class );
    }


    public SpaceConstraintPropertyReader( Properties properties ) {
        super( SpaceConstraint.class, properties );
    }


    public void read() {

        getProduct().setLatitude(
            PropertyReadWriteAux.readMinMaxPair(
                getProperties(),
                SfrProperty.LAT_MIN.key,
                SfrProperty.LAT_MAX.key
            )
        );

        getProduct().setLongitude(
            PropertyReadWriteAux.readMinMaxPair(
                getProperties(),
                SfrProperty.LON_MIN.key,
                SfrProperty.LON_MAX.key
            )
        );


        if( getProperties().containsKey( SfrProperty.ALT_MIN.key )  ) {

            getProduct( ).setAltitude(
                PropertyReadWriteAux.readMinMaxPair(
                    getProperties(),
                    SfrProperty.ALT_MIN.key,
                    SfrProperty.ALT_MAX.key
                )
            );
        }


        getProduct().setAreaCRS(
            getProperties().getProperty( SfrProperty.AREA_CRS.key )
        );


        getProduct().setVerticalCRS(
            getProperties().getProperty( SfrProperty.ALT_VCRS.key )
        );
    }


    public static SpaceConstraint readSpaceConstraint( Properties prop ) {

        SpaceConstraintPropertyReader sc = new SpaceConstraintPropertyReader( prop );
        sc.begin( );
        sc.read( );
        return sc.getProduct( );
    }

    public void done() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
