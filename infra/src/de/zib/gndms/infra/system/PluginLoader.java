package de.zib.gndms.infra.system;
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

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 03.02.12  11:01
 * @brief
 */
public class PluginLoader {
    
    private String pluginPath;


    public PluginLoader( final String pluginPath ) {
        this.pluginPath = pluginPath;
    }


    public URLClassLoader loadPlugins( ) throws IOException {

        final ArrayList<URL> jars;

        final File pluginDir = new File( pluginPath );

        final String[] list = pluginDir.list( new FilenameFilter() {
            @Override
            public boolean accept( final File dir, final String name ) {

                return name.matches( ".*\\.jar" );
            }
        } );


        if ( list != null  ) {
            jars = new ArrayList<URL>( list.length );
            for ( int i=0; i < list.length; ++i )
                jars.add( new URL( "file://" + pluginPath + File.separator + list[i] ) );

            return new URLClassLoader(
                    jars.toArray( new URL[jars.size()] ),
                    this.getClass().getClassLoader()
            );
        }
        
        return new URLClassLoader( new URL[] {}, this.getClass().getClassLoader() );
    }


    public static void main ( String[] args ) throws  Exception {
        
        if ( args.length != 1 ) {
            System.out.println( "java " + PluginLoader.class.getName() + " <plugin-dir>" );
            System.exit( 1 );
        }

        ApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:META-INF/00_system.xml");

        PluginLoader pl = new PluginLoader( args[0] );
        ClassLoader cl = pl.loadPlugins();
        PlugableTaskFlowProvider provider = ( PlugableTaskFlowProvider ) context.getAutowireCapableBeanFactory()
                .getBean( "taskFlowProvider" );

        provider.setCl( cl );
        provider.loadPlugins();
        provider.listPlugins();
        
        System.exit( 0 );
    }
}
