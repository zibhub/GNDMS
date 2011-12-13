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

package de.zib.adis.abi;

import java.util.Collection;
import java.util.Map;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ABIi
{
        // use reflect to check for correct method names
        {
                Class<?> c = this.getClass();
                Method[] methods = c.getDeclaredMethods( );

                // search the right method
                for( Method m: methods )
                {
                        // search the right method
                        if( Modifier.PUBLIC != m.getModifiers() )
                                continue;

                        Class<?>[] paramtypes = m.getParameterTypes();

                        // check for parametertypes
                        for( int i = 0; i < paramtypes.length; ++i )
                        {
                                if( paramtypes[i].isAssignableFrom( String.class ) )
                                        continue;
                                else if( paramtypes[i].isAssignableFrom( Collection.class ) )
                                {
                                        if( i != paramtypes.length-1 )
                                        {
                                                throw new IllegalStateException( "Method " + m.toGenericString() + " has invalid parameter format: At most the last parameter may be a Collection." );
                                        }
                                }
                                else
                                {
                                        throw new IllegalStateException( "Method " + m.toGenericString() + " has invalid parameter format: parameter " + String.valueOf( i+1 ) + " is not supported." );
                                }
                        }

                        Class<?> returntype = m.getReturnType();

                        // check returntype
                        {
                                if( String.class.isAssignableFrom( returntype ) )
                                        continue;
                                else if( Collection.class.isAssignableFrom( returntype ) )
                                        continue;
                                else if( Map.class.isAssignableFrom( returntype ) )
                                        continue;
                                else if( Boolean.class.isAssignableFrom( returntype ) )
                                        continue;
                                else if( Character.class.isAssignableFrom( returntype ) )
                                        continue;
                                else if( Byte.class.isAssignableFrom( returntype ) )
                                        continue;
                                else if( Short.class.isAssignableFrom( returntype ) )
                                        continue;
                                else if( Integer.class.isAssignableFrom( returntype ) )
                                        continue;
                                else if( Long.class.isAssignableFrom( returntype ) )
                                        continue;
                                else if( Float.class.isAssignableFrom( returntype ) )
                                        continue;
                                else if( Double.class.isAssignableFrom( returntype ) )
                                        continue;
                                else if( Void.class.isAssignableFrom( returntype ) )
                                        continue;
                        }
                }
        }

        protected void printABICommands( )
        {
                Class<?> c = this.getClass();
                Method[] methods = c.getDeclaredMethods( );

                for( Method m: methods )
                {
                        // print the public methods only
                        if( Modifier.PUBLIC != m.getModifiers() )
                                continue;

                        System.out.println( m.getName() + " |--> " + m.toGenericString() );
                }
        }
}
