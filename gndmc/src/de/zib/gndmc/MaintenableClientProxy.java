package de.zib.gndmc;

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



import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * @author  try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 27.01.2009, Time: 12:40:12
 */
public class MaintenableClientProxy implements InvocationHandler {

    private Object client;
    private HashMap<String, Method> methodMap;

    public static Object newInstance( Object obj ) {
        return java.lang.reflect.Proxy.newProxyInstance(
            obj.getClass().getClassLoader(),
            new Class[] { MaintenableClient.class },
            new MaintenableClientProxy(obj) );
    }

    
    private MaintenableClientProxy(Object obj) {
        client = obj;
        methodMap = new HashMap<String, Method>( );
    }


    public Object invoke( final Object proxy, final Method meth, final Object[] args ) throws Throwable {

        try {
            Method m;
            String mn = meth.toString();
            if( methodMap.containsKey( mn ) )
                m = methodMap.get( mn );
            else
                m = findMethod( client.getClass(), meth );

            Object o = m.invoke( client, args );
            // method seems to be okay
            methodMap.put( mn, m );

            return o;

        } catch ( NoSuchMethodException e ) {
            throw new IllegalArgumentException( "Given object isn't maintenable.", e );
        } catch ( IllegalAccessException e ) {
            throw new IllegalArgumentException( "getTypeDesc method is inaccessible", e );
        } catch ( InvocationTargetException e ) {
            throw new IllegalArgumentException( "getTypeDesc invocation failed.", e );
        }
    }
    

    private static Method findMethod( Class c, Method m ) throws NoSuchMethodException {

        Method[] ms = c.getMethods( );
        for( Method tm : ms )
            if( m.getName().equals( tm.getName( ) ) )
                return tm;

        throw new NoSuchMethodException( );
    }
}
