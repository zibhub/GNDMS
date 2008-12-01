package de.zib.gndms.typecon.common.type;

import org.apache.axis.types.NormalizedString;
import org.apache.axis.types.Token;
import types.ContextT;
import types.ContextTEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 24.10.2008, Time: 13:27:13
 */
public class ContextXSDTypeWriter {

    public static ContextT writeContext( Map<String, String> ctx ) {

        Set<String> keys = ctx.keySet( );

        ArrayList<ContextTEntry> al = new ArrayList<ContextTEntry>( ctx.size( )  );

        for( String s: keys ) {
            String v = ctx.get( s );
            ContextTEntry ent = new ContextTEntry( new NormalizedString( v ) );
            if( v != null ) {
                ent.setKey( new Token( s ) );
                al.add( ent );
            } else
                throw new IllegalArgumentException( "Null value for context key: " + s );
        }

        return new ContextT( al.toArray( new ContextTEntry[al.size()] ) );
    }
}
