package de.zib.gndms.typecon.common.type;

import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;

import types.ContextT;
import types.ContextTEntry;
import org.apache.axis.types.Token;
import org.apache.axis.types.NormalizedString;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 24.10.2008, Time: 13:27:13
 */
public class ContextXSDTypeWriter {

    public static ContextT writeContext( HashMap<String, String> ctx ) {

        Set<String> keys = ctx.keySet( );

        ArrayList<ContextTEntry> al = new ArrayList<ContextTEntry>( ctx.size( )  );

        for( String s: keys ) {
            ContextTEntry ent = new ContextTEntry( new NormalizedString( ctx.get( s ) ) );
            ent.setKey( new Token( s ) );
            al.add( ent );
        }

        return new ContextT( al.toArray( new ContextTEntry[al.size()] ) );
    }
}
