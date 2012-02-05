package de.zib.gndms.neomodel.common;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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

import de.zib.gndms.neomodel.gorfx.Task;
import org.jetbrains.annotations.NotNull;
import org.neo4j.graphdb.GraphDatabaseService;

import java.util.Map;

/**
 * Dao
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 * User: stepn Date: 05.09.2008 Time: 14:48:36
 */
public class Dao {
    final @NotNull private GraphDatabaseService gdb;
    final @NotNull private String gridName;
    private ClassLoader classLoader = this.getClass().getClassLoader();

    public Dao(@NotNull String gridName, @NotNull GraphDatabaseService gdb) {
        this.gridName = gridName;
        this.gdb      = gdb;
    }

    public Session beginSession() {

        try {

            Session ses = ( (SessionFactory) classLoader.loadClass(
                    SessionFactory.class.getName() ).newInstance() ).invoke(this, gridName, gdb );

            if ( ses.getClass().getClassLoader() != classLoader )
                throw new Error( "This sucks" );
            return ses;
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
        // return new Session(this, gridName, gdb);
    }


    @NotNull
    public String getGridName() {
        return gridName;
    }

    public void createTask(final String id) {
        final Session session = beginSession();
        try {
            final Task task = session.createTask();
            task.setId( id );
            session.success();
        }
        finally {
            session.finish();
        }
    }

    public void removeAltTaskState(final String taskId) {
        final Session session = beginSession();
        try {
            session.findTask(taskId).setAltTaskState(null);
            session.finish();
        }
        finally { session.success(); }
    }

    public Map<String, String> getTaskFlowTypeConfig( String key ) {
        final Session session = beginSession();
        try {
            final Map<String, String> ret = session.findTaskFlowType( key ).getConfigMapData();
            session.finish();
            return ret;
        }
        finally { session.success(); }
    }

    public String getTaskFlowTypeCalculatorFactoryClassName( String key ) {
        final Session session = beginSession();
        try {
            final String ret = session.findTaskFlowType( key ).getCalculatorFactoryClassName();
            session.finish();
            return ret;
        }
        finally { session.success(); }
    }

    public String getTaskFlowTypeTaskActionFactoryClassName( String key ) {
        final Session session = beginSession();
        try {
            final String ret = session.findTaskFlowType( key ).getTaskActionFactoryClassName();
            session.finish();
            return ret;
        }
        finally { session.success(); }
    }


    public ClassLoader getClassLoader() {

        return classLoader;
    }


    public void setClassLoader( final ClassLoader classLoader ) {

        this.classLoader = classLoader;
    }


}
