package de.zib.gndms.gritserv.typecon.types;

import types.FileMappingSeqT;
import types.FileMapEntryT;

import java.util.TreeMap;
import java.util.Map;
import java.util.ArrayList;

import org.apache.axis.types.NormalizedString;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 14.10.2008, Time: 13:35:04
 */
public class XSDReadWriteAux {


    public static TreeMap<String, String> read( FileMappingSeqT fs ) {

        FileMapEntryT[] fsm = fs.getMapFile();
        TreeMap<String, String> out = new TreeMap<String, String>( );

        for( int i=0; i < fsm.length; ++i ) {
            if( fsm[i].getDestinationFile() != null)
                out.put( fsm[i].getSourceFile().toString(), fsm[i].getDestinationFile().toString() );
            else
                out.put( fsm[i].getSourceFile().toString(), null );
        }

        return out;
    }


    public static FileMappingSeqT write( Map<String, String> m ) {

        ArrayList<FileMapEntryT> fme = new ArrayList<FileMapEntryT>( m.size( ) );

        for( String s: m.keySet( ) )
            fme.add( createFileMapEntryT( s, m.get( s ) ) );

        return new FileMappingSeqT( fme.toArray( new FileMapEntryT[fme.size()] ) );
    }


    public static FileMapEntryT createFileMapEntryT( String s, String d ) {

        FileMapEntryT  e = new FileMapEntryT( );
        e.setSourceFile( new NormalizedString( s ) );
        if( d != null )
            e.setDestinationFile( new NormalizedString( d ) );

        return e;
    }
}
