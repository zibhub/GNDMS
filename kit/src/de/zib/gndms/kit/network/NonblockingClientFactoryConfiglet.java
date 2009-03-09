package de.zib.gndms.kit.network;

import de.zib.gndms.kit.configlet.DefaultConfiglet;
import de.zib.gndms.kit.config.ConfigProvider;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.logging.Log;

import java.io.Serializable;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 06.03.2009, Time: 10:54:15
 */
public class NonblockingClientFactoryConfiglet extends DefaultConfiglet {

    private final static String DELAY="delay"; // in ms
    private final static String TIMEOUT="timeout"; // in s


    @Override
    public void init( @NotNull Log loggerParam, @NotNull String aName, Serializable data ) {
        super.init( loggerParam, aName, data );
        refreshFactory();
    }


    @Override
    public void update( @NotNull Serializable data ) {
        super.update( data );    // overriden method implementation
        refreshFactory();
    }


    private void refreshFactory() {
        final MapConfig mapConfig = getMapConfig();
        try{
            NonblockingClientFactory fac =
                NonblockingClientFactory.class.cast( NetworkAuxiliariesProvider.getGridFTPClientFactory() );
            if( mapConfig.hasOption( DELAY ) ) {
                fac.setDelay( mapConfig.getIntOption( DELAY ) );
            }

            if( mapConfig.hasOption( TIMEOUT ) ) {
                fac.setTimeout( mapConfig.getIntOption( TIMEOUT ) );
            }
        } catch ( ClassCastException e ) {
            e.printStackTrace( );
        } catch ( MandatoryOptionMissingException e ) {
            e.printStackTrace();  // may not occur
        }
    }
}
