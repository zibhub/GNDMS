package de.zib.gndms.GORFX.common.type.io;

import types.FileMappingSeqT;
import types.FileMapEntryT;

import java.util.TreeMap;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 14.10.2008, Time: 13:35:04
 */
public class FromXSDReaderAux {


    public static TreeMap<String, String> read( FileMappingSeqT fs ) {

        FileMapEntryT[] fsm = fs.getMapFile();
        TreeMap<String, String> out = new TreeMap<String, String>( );

        for( int i=0; i < fsm.length; ++i )
            out.put( fsm[i].getSourceFile().toString(), fsm[i].getDestinationFile().toString() );

        return out;
    }

}
