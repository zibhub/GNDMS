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



import org.joda.time.DateTime;

import java.util.Properties;

/**
 *
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 17.09.2008, Time: 14:57:16
 */
public class TimeConstraintPropertyWriter extends AbstractPropertyIO implements TimeConstraintWriter {

    public TimeConstraintPropertyWriter() {
        super( );
    }


    public TimeConstraintPropertyWriter( Properties properties ) {
        super( properties );
    }


    public void writeMinTime( DateTime dt ) {
        writeMinTimeToProperties( getProperties( ), dt );
    }


    public void writeMaxTime( DateTime dt ) {
        writeMaxTimeToProperties( getProperties( ), dt );
    }


    public static void writeMinTimeToProperties( Properties prop, DateTime dt  ) {
       PropertyReadWriteAux.writeISODateTime( prop, SfrProperty.TIME_MIN.key, dt );
    }
    

    public static void writeMinTimeToProperties( Properties prop, String dt  ) {
        prop.setProperty( SfrProperty.TIME_MIN.key, dt );
    }


    public static void writeMaxTimeToProperties( Properties prop, DateTime dt ) {
        PropertyReadWriteAux.writeISODateTime( prop, SfrProperty.TIME_MAX.key, dt );
    }


    public static void writeMaxTimeToProperties( Properties prop, String dt  ) {
        prop.setProperty( SfrProperty.TIME_MAX.key, dt );
    }


    public void done() {
    }
}
