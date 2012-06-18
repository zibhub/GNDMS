/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.zib.gndms.voldmodel.abi;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;

public class ABI {
    private ABIi iface;
    private JCommander jc;

    @Parameter( names = "--usage", hidden = true )
    private boolean usage;

    @Parameter( description = "Commands" )
    private List<String> commands = new ArrayList<String>();

    public ABI( ABIi iface, String[] args ) {
        this.iface = iface;
        this.jc = new JCommander( this, args );
    }

    public void invoke() {
        // we need at least a methods name
        if( commands.size() < 1 )
            return;

        if( usage )
            jc.usage();

        List<String> commands = new ArrayList<String>( this.commands );
        String cmd = commands.remove( 0 );

        // use reflect to call adis method
        {
            Class<?> c = iface.getClass();
            Method[] methods = c.getDeclaredMethods();

            // search the right method
            for( Method m : methods ) {
                // search the right method
                if( Modifier.PUBLIC != m.getModifiers() )
                    continue;
                if( !m.getName().equals( cmd ) )
                    continue;

                Class<?>[] paramtypes = m.getParameterTypes();

                // get parameters
                Object[] params = new Object[paramtypes.length];
                for( int i = 0; i < paramtypes.length; ++i ) {
                    //if( paramtypes[i].isInstance( String.class ) )
                    if( paramtypes[i].isAssignableFrom( String.class ) ) {
                        if( 0 == commands.size() ) {
                            throw new IllegalArgumentException( "The command " + cmd + " excepts at least one more argument on method " + m.toGenericString() + "." );
                        }

                        params[i] = commands.remove( 0 );
                    }
                    //else if( paramtypes[i].isInstance( Collection.class ) )
                    else if( paramtypes[i].isAssignableFrom( Collection.class ) ) {
                        Set<String> s = new HashSet<String>();

                        while( 0 != commands.size() ) {
                            s.add( commands.remove( 0 ) );
                        }

                        params[i] = s;
                    }
                    else {
                        throw new IllegalStateException( "Internal Error: don't understand the parameter type of the API." );
                    }
                }

                // call and handle return value
                Object result;
                try {
                    result = m.invoke( iface, params );
                }
                catch( Exception e ) {
                    e.printStackTrace();
                    return;
                }

                // handle results
                {
                    if( result instanceof String ) {
                        String r = ( String ) result;
                        System.out.println( r );
                    }
                    else if( result instanceof Collection<?> ) {
                        Collection<String> r = ( Collection<String> ) result;
                        printCollection( r );
                    }
                    else if( result instanceof Map<?, ?> ) {
                        Map<String, String> r = ( Map<String, String> ) result;
                        printMap( r );
                    }
                    else if( Boolean.TYPE.isInstance( result ) ) {
                    }
                    else if( Character.TYPE.isInstance( result ) ) {
                    }
                    else if( Byte.TYPE.isInstance( result ) ) {
                    }
                    else if( Short.TYPE.isInstance( result ) ) {
                    }
                    else if( Integer.TYPE.isInstance( result ) ) {
                    }
                    else if( Long.TYPE.isInstance( result ) ) {
                    }
                    else if( Float.TYPE.isInstance( result ) ) {
                    }
                    else if( Double.TYPE.isInstance( result ) ) {
                    }
                    else if( Void.TYPE.isInstance( result ) ) {
                    }
                }

                return;
            }
        }

        // did not find it - put list of possible methods
        {
            iface.printABICommands();
        }
    }

    private static void printCollection( Collection<String> col ) {
        for( String s : col ) {
            System.out.println( s );
        }
    }

    private static void printMap( Map<String, String> map ) {
        for( Map.Entry<String, String> entry : map.entrySet() ) {
            System.out.println( entry.getKey() + ": " + entry.getValue() );
        }
    }
}
