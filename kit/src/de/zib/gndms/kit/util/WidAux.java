package de.zib.gndms.kit.util;

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



import org.apache.log4j.MDC;
import org.apache.log4j.NDC;


/**
 * ThingAMagic.
 *
 * @author: try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 22.10.2008 Time: 14:31:50
 */
public class WidAux {
    private WidAux() {}


    public static void removeWid() {
        removeId("dmswid");
    }


    public static String getWid() {
        return (String) MDC.get("dmswid");
    }


    public static void initWid(final String cachedWid) {
        initId("dmswid", cachedWid);
    }


    public static void removeGORFXid( )  {
        removeId( "gorfxid" );
    }

    
    public static void initGORFXid(final String id) {
        initId("gorfxid", id);
    }

    /**
     * Stores {@code val} in the MDC, with {@code id} as its key and pushes 'id+":"val' on the NDC.
     *
     * @see org.apache.log4j.MDC
     * @see org.apache.log4j.NDC
     * @param id key
     * @param val value
     */
    public static void initId( final String id, final String val ) {

        if ( id  == null || val == null )
            return;
        else {
            MDC.put( id, val );
            NDC.push( id + ":" + val);
        }
    }

    /**
     * Removes the entry with the key {@code id} from the MDC.
     * Calls {@code NDC.pop()}.
     *
     * @see org.apache.log4j.MDC
     * @see org.apache.log4j.NDC
     * @param id a key
     */
    public static void removeId( final String id ) {
        MDC.remove( id );
        NDC.pop();
    }

    /**
     * Returns the value for a specific, which has been stored in the MDC
     *
     * @see org.apache.log4j.MDC
     *
     * @param id a key for a value
     * @return the value for a specific, which has been stored in the MDC
     */
    public static String getId( final String id ) {
        return (String) MDC.get( id );
    }
}
