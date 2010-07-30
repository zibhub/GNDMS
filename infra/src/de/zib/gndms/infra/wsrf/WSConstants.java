package de.zib.gndms.infra.wsrf;

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



import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 01.12.2008, Time: 12:47:58
 */
public final class WSConstants {

    /** Timestamp for resource lifetime "forever" (i'm afraid of 2027) */
    public final static Calendar FOREVER = new GregorianCalendar( 2026, 12, 31 );

    /** Lifetime in seconds for newly created resources */
    public final static int DEFAULT_LIFETIME_OFFSET = 120;

    /** Default deadline in seconds for tasks */
    public final static int DEFAULT_DEADLINE_OFFSET = 120;

    /** Name of gorfx permission configlet */
    public final static String GORFX_PERMISSION_CONFIGLET = "PermissionConfiglet";



    /** @return Delivers a valid termination-time starting from now */
    public static Calendar getDefaultTerminationTime( ) {
        return offsetFromNow( DEFAULT_LIFETIME_OFFSET );
    }


    public static Calendar getDefaultDeadline() {
        return offsetFromNow( DEFAULT_DEADLINE_OFFSET );
    }


    private static Calendar offsetFromNow( int off ) {
        return new DateTime( ).plusSeconds( off ).toGregorianCalendar();
    }


    private WSConstants( ) { }
}
