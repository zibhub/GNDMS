/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
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

package de.zib.gndms.infra.system;

import de.zib.gndms.logic.model.gorfx.taskflow.TaskFlowFactory;
import de.zib.gndms.logic.model.gorfx.taskflow.TaskFlowProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author try ma ik jo rr a zib
 * @date 27.06.11  18:38
 * @brief
 */
public class PlugableTaskFlowProvider extends TaskFlowProviderImpl {

    boolean hasFactories = false;
    protected Logger logger = LoggerFactory.getLogger( this.getClass() );

    public static void register( TaskFlowFactory tff ) {

        System.out.println( "register called" );

        Map<String, TaskFlowFactory> plugins = new HashMap<String, TaskFlowFactory>( 10 );

        if( plugins.containsKey( tff.getTaskFlowKey() ) )
            throw new IllegalStateException( "plugin " + tff.getTaskFlowKey() +" already exists" );

        plugins.put( tff.getTaskFlowKey(), tff );
    }


    public void listPlugins( ) {
        Map<String, TaskFlowFactory> plugins = getFactories();
        for ( String k : plugins.keySet() ){
            logger.debug( k + ": " + plugins.get( k ).getVersion() + " class: " + plugins.get( k ).getClass().getName() );
        }
    }


    public int pluginCount( ) {
        return getFactories().size();
    }


    public void loadPlugins( ) {
        loadPlugins( true );
    }


    public void loadPlugins( boolean checkDeps ) {

        ServiceLoader<TaskFlowFactory> sl = ServiceLoader.load( TaskFlowFactory.class );
        for( TaskFlowFactory bp : sl ) {
            register( bp );
        }

        if( checkDeps )
            checkDeps();
    }


    public void checkDeps( ) {

        Map<String, TaskFlowFactory> plugins = getFactories();
        List<String> missing = new ArrayList<String>();

        for( String k : plugins.keySet() ) {
            TaskFlowFactory tff = plugins.get( k );
            for( String dep : tff.depends( ) ) {
                if(! plugins.containsKey( dep ) )
                    missing.add( dep );
            }
        }


        if( missing.size() != 0 ) {
            StringBuffer mes = new StringBuffer( );
            mes.append( "Missing plugin dependencies detected:" );
            for ( String k : missing ) {
                mes.append( '\n' );
                mes.append( k );
            }
            logger.warn( mes.toString() );
        }
    }
    @Override
    public void setFactories( Map<String, TaskFlowFactory> factories ) {

        if ( hasFactories )
            throw new IllegalStateException( "factories are allready set"  );

        hasFactories = true;
        super.setFactories( factories );    // overriden method implementation
    }
}
