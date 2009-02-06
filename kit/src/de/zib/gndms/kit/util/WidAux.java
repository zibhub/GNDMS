package de.zib.gndms.kit.util;

import org.apache.log4j.MDC;
import org.apache.log4j.NDC;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 22.10.2008 Time: 14:31:50
 */
public class WidAux {
    private WidAux() {}


    public static void removeWid() {
        removeId("c3wid");
    }


    public static String getWid() {
        return (String) MDC.get("c3wid");
    }


    public static void initWid(final String cachedWid) {
        initId("c3wid", cachedWid);
    }


    public static void removeGORFXid( )  {
        removeId( "gorfxid" );
    }

    
    public static void initGORFXid(final String id) {
        initId("gorfxid", id);
    }


    public static void initId( final String id, final String val ) {

        if ( id  == null || val == null )
            return;
        else {
            MDC.put( id, val );
            NDC.push( id + ":" + val);
        }
    }

    public static void removeId( final String id ) {
        MDC.remove( id );
        NDC.pop();
    }


    public static String getId( final String id ) {
        return (String) MDC.get( id );
    }
}
