package de.zib.adis;

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

                // did not find it - put list of possible methods
                {
                        System.out.println( "List of possible commands: " );
                        for( Method m: methods )
                        {
                                System.out.println( m.getName() + " |--> " + m.toGenericString() );
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
