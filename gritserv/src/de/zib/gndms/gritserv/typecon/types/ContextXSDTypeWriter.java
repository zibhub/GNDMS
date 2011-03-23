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



import org.apache.axis.types.NormalizedString;
import org.apache.axis.types.Token;
import types.ContextT;
import types.ContextTEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 24.10.2008, Time: 13:27:13
 */
public class ContextXSDTypeWriter {

    public static ContextT writeContext( Map<String, String> ctx ) {

        Set<String> keys = ctx.keySet( );

        ArrayList<ContextTEntry> al = new ArrayList<ContextTEntry>( ctx.size( )  );

        for( String s: keys ) {
            String v = ctx.get( s );
            if( v != null ) {
                ContextTEntry ent = new ContextTEntry( new NormalizedString( v ) );
                ent.setKey( new Token( s ) );
                al.add( ent );
            } else
                throw new IllegalArgumentException( "Null value for context key: " + s );
        }

        return new ContextT( al.toArray( new ContextTEntry[al.size()] ) );
    }
}
