package de.zib.gndms.neomodel.common;
/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import org.neo4j.graphdb.GraphDatabaseService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
* @author Maik Jorra
* @email jorra@zib.de
* @date 05.02.12  13:52
* @brief
*/
public class SessionFactory {

    public Session invoke( Dao dao, String gridName, GraphDatabaseService gdb )
            throws NoSuchMethodException, ClassNotFoundException, InstantiationException,
            IllegalAccessException, InvocationTargetException
    {

        Constructor ctor = getClass().getClassLoader().loadClass( Session.class.getName() )
                .getConstructor(
                        new Class[]{dao.getClass(), String.class, GraphDatabaseService.class}
                );
        return ( Session ) ctor.newInstance( dao, gridName, gdb );
    }
}
