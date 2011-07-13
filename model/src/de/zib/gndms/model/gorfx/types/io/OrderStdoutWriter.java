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



import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Writes an ORQ to Stdout.
 * It should be used in conjunction with an OrderConverter.
 *
 *
 * @see OrderConverter
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 17:57:12
 */
public abstract class OrderStdoutWriter implements OrderWriter {

    public void writeJustEstimate( boolean je ) {
        System.out.println( "Just estimate: " + Boolean.toString( je ) );
    }


    public void writeContext( HashMap<String, String> ctx ) {
        System.out.println( "Context" );
        showMap( ctx );
    }


    public static void showMap( Map<String, String> map ) {
        Set<String> ks = map.keySet();
        for( String k : ks )
            System.out.println( "    " + k + " ; " + map.get( k ) );
    }


    public void writeId( String id ) {
        System.out.println( "GORFXId: " + id);
    }
}
