package de.zib.gndms.common.model.gorfx.types.io;

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



import de.zib.gndms.common.model.gorfx.types.AbstractOrder;

import java.util.Properties;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 18:02:08
 */
public abstract class OrderPropertyReader<M extends AbstractOrder> extends AbstractPropertyReader<M> {

    protected OrderPropertyReader( Class productClass ) {
        super( productClass );
    }


    protected OrderPropertyReader( Class productClass, Properties properties ) {
        super( productClass, properties );
    }


    public void read() {
        String s = getProperties( ).getProperty( SfrProperty.JUST_ASK.key );
        if( s != null  )
            getProduct().setJustEstimate( Boolean.parseBoolean( s ));

        if( getProperties().containsKey( SfrProperty.CONTEXT.key ) )
            getProduct().setActContext(
                PropertyReadWriteAux.readMap( getProperties(), SfrProperty.CONTEXT.key ) );

        getProduct().setActId( getProperties().getProperty( SfrProperty.GORFX_ID.key ) );
    }
}
