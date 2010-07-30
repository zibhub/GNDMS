package de.zib.gndms.gritserv.typecon.types;

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
