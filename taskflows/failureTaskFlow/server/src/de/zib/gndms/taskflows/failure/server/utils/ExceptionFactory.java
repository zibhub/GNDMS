/*
 * Copyright 2008-2012 Zuse Institute Berlin (ZIB)
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
 *
 */

package de.zib.gndms.taskflows.failure.server.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @date: 23.03.12
 * @time: 11:10
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class ExceptionFactory {
    public static void throwUncheked( Throwable e ) {
        ExceptionFactory.< RuntimeException >throwIt(e);
    }

    @SuppressWarnings("unchecked")
    public static < E extends Throwable > void throwIt( Throwable e ) throws E {
        throw ( E )e;
    }

    public static Throwable createException(final String className, final String message) {
        final Class clazz;
        try {
            clazz = Class.forName( className );
        } catch( ClassNotFoundException e ) {
            throw new IllegalArgumentException( "Could not find class " + className );
        }

        if( ! Throwable.class.isAssignableFrom( clazz ) ) {
            throw new IllegalArgumentException( "Class " + className + " needs be a Throwable derivative!");
        }

        boolean argument = true;
        Constructor constructor;
        try {
            constructor = clazz.getConstructor( new Class<?>[]{ String.class } );
        } catch( NoSuchMethodException e ) {
            try {
                constructor = clazz.getConstructor( new Class<?>[]{ } );
                argument = false;
            } catch (NoSuchMethodException e1) {
                throw new IllegalArgumentException( "Class " + className + " has no constructor with a String argument. THIS IS NOT HAPPENING :/" );
            }
        }

        try {
            if( argument )
                return ( Throwable )constructor.newInstance( message );
            else
                return ( Throwable )constructor.newInstance();
        } catch( InstantiationException e ) {
            throw new IllegalStateException( e );
        } catch( IllegalAccessException e ) {
            throw new IllegalStateException( e );
        } catch( InvocationTargetException e ) {
            throw new IllegalStateException( e );
        }
    }
}
