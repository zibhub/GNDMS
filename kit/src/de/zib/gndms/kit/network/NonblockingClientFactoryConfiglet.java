package de.zib.gndms.kit.network;

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



import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.kit.configlet.DefaultConfiglet;
import org.slf4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 06.03.2009, Time: 10:54:15
 */
public class NonblockingClientFactoryConfiglet extends DefaultConfiglet {

    private final static String DELAY="delay"; // in ms
    private final static String TIMEOUT="timeout"; // in s
    private final static String BUFFERSIZE="buffersize"; // in byte

    @Override
    public void init( @NotNull Logger loggerParam, @NotNull String aName, Serializable data ) {
        super.init( loggerParam, aName, data );
        refreshFactory();
    }


    @Override
    public void update( @NotNull Serializable data ) {
        super.update( data );    // overriden method implementation
        refreshFactory();
    }


    private void refreshFactory() {
        getLog().debug( "refresh called" );
        final MapConfig mapConfig = getMapConfig();
        try{
            Schedulable sed =
                Schedulable.class.cast( NetworkAuxiliariesProvider.getGridFTPClientFactory() );
            if( mapConfig.hasOption( DELAY ) ) {
                sed.setDelay( mapConfig.getIntOption( DELAY ) );
            }

            if( mapConfig.hasOption( TIMEOUT ) ) {
                sed.setTimeout( mapConfig.getIntOption( TIMEOUT ) );
            }

            if (mapConfig.hasOption( BUFFERSIZE )) {
                if (mapConfig.getOption( BUFFERSIZE ).trim().equals( "null" )) {
                       NetworkAuxiliariesProvider.setBufferSize( null );
                } else {
                 NetworkAuxiliariesProvider.setBufferSize( mapConfig.getIntOption( BUFFERSIZE ) );
                }
            }
        } catch ( ClassCastException e ) {
            getLog( ).warn( e );
        } catch ( MandatoryOptionMissingException e ) {
            getLog( ).warn( e );
        }
    }
}
