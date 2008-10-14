package de.zib.gndms.GORFX.common.type.io;

import types.ContextT;
import types.ContextTEntry;

import java.util.HashMap;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 18:22:29
 */
public class ContextXSDReader {

    public static HashMap<String,String> readContext( ContextT ctx ) {

        // todo: replace with getEntries call
        ContextTEntry[] entries = new ContextTEntry[12];
        HashMap<String,String> cm = new HashMap<String,String> ( entries.length );

        for( int i=0; i < entries.length; ++i  ) {
            cm.put( entries[i].getKey().toString( ), entries[i].get_value().toString() );
        }

        return cm;
    }

}
