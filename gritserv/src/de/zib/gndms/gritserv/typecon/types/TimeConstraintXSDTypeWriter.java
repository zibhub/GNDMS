package de.zib.gndms.gritserv.typecon.types;

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



import types.TimeConstraintT;
import de.zib.gndms.model.gorfx.types.io.TimeConstraintWriter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 16.10.2008, Time: 10:28:07
 */
public class TimeConstraintXSDTypeWriter extends AbstractXSDTypeWriter<TimeConstraintT> implements TimeConstraintWriter {

    private DateTimeFormatter ISOFormatter;

    public TimeConstraintXSDTypeWriter( ) {
        ISOFormatter = ISODateTimeFormat.dateTime( );
    }

    
    public void writeMinTime( DateTime dt ) {
        getProduct().setMinTime( ISOFormatter.print( dt ) );
    }


    public void writeMinTime( String dt ) {
        getProduct().setMinTime( dt );
    }


    public void writeMaxTime( DateTime dt ) {
        getProduct().setMaxTime( ISOFormatter.print( dt ) );
    }


    public void writeMaxTime( String dt ) {
        getProduct().setMaxTime( dt );
    }
    

    public void begin() {
        setProduct( new TimeConstraintT( ) );
    }


    public void done() {
        // Not required here
    }
}
