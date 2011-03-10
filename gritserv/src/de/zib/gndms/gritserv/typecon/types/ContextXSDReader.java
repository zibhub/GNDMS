package de.zib.gndms.gritserv.typecon.types;

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



import types.ContextT;
import types.ContextTEntry;

import java.util.HashMap;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 18:22:29
 */
public class ContextXSDReader {

    public static HashMap<String,String> readContext( ContextT ctx ) {

        ContextTEntry[] entries = ctx.getEntry();
        HashMap<String,String> cm = new HashMap<String,String> ( entries.length );

        for( int i=0; i < entries.length; ++i  ) {
            cm.put( entries[i].getKey().toString( ), entries[i].get_value().toString() );
        }

        return cm;
    }

}
