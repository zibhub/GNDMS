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



import de.zib.gndms.model.gorfx.types.SpaceConstraint;

/**
 *
 * @see SpaceConstraintWriter
 * @author: try ma ik jo rr a zib
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 23.09.2008, Time: 10:40:12
 */
public class SpaceConstraintConverter extends GORFXConverterBase<SpaceConstraintWriter, SpaceConstraint> {


    public SpaceConstraintConverter() {
    }


    public SpaceConstraintConverter( SpaceConstraintWriter writer, SpaceConstraint model ) {
        super( writer, model );
    }


    public void convert() {

        if( getWriter( ) == null || getModel() == null )
            throw new IllegalStateException( );

        getWriter().begin();
        getWriter().writeLatitude( getModel().getLatitude() );

        getWriter().writeLongitude( getModel().getLongitude() );

        if( getModel().hasAreaCRS() )
            getWriter().writeAreaCRS( getModel().getAreaCRS() );

        if( getModel().hasAltitude() )
            getWriter().writeAltitude( getModel().getAltitude() );

        if( getModel().hasVerticalCRS() )
            getWriter().writeVerticalCRS( getModel().getVerticalCRS() );
            
        getWriter().done();
    }
}
