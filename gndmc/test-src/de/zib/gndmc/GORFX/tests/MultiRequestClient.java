package de.zib.gndmc.GORFX.tests;

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



import de.zib.gndms.kit.application.AbstractApplication;
import de.zib.gndms.stuff.propertytree.PropertyTree;
import de.zib.gndms.stuff.propertytree.PropertyTreeFactory;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This client reads multiple staging orqs from a file and sends them to a given site using mulitple threads
 *
 * @author  try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 05.02.2009, Time: 15:47:02
 */
public class MultiRequestClient extends AbstractApplication {

    @Option( name="-uri", required=true, usage="URL of GORFX-Endpoint", metaVar="URI" )
    protected String gorfxEpUrl;
    @Option( name="-props", required=true, usage="staging.properties" )
    protected String propFile;
    @Option( name="-dn", required=true, usage="DN" )
    protected String dn;
    @Option( name="-w", usage="Delay between thread starts in ms" )
    protected int dl=0;

    
    public static void main(String[] args) throws Exception {
        MultiRequestClient cnt = new MultiRequestClient();
        cnt.run( args );

    }


    public void run() throws Exception {

        PropertyTree pt = PropertyTreeFactory.createPropertyTree( new File( propFile ) );
        String rc = pt.getProperty( "RunnerClass" );

        Class<? extends RequestRunner> runner;
        try {
            runner = (Class<? extends RequestRunner>) Class.forName( rc );
        } catch ( Exception e ) {
            System.err.println( "Failed to load class " + e );
            throw e;
        }

        List<Properties> pl = new ArrayList<Properties>( );

        PropertyTree st;
        for ( int i=1; ( st = pt.subTree( "ORQ" + i ) ) != null; ++i )
            pl.add( st.asProperties( true ) );

        if( pl.size() < 1 )
            throw new IllegalStateException( "Couldn't read orq information" );

        System.out.println( "Creating " + pl.size() + " staging threads" );

        List<RequestRunner> runners = createRequestRunners( runner, pl );

        ExecutorService exec = Executors.newFixedThreadPool( runners.size() );

        for( RequestRunner run : runners )  {
            System.out.println( "Starting thread: " );
            
            run.show( );
            exec.submit( run );
            Thread.sleep( dl );
        }

        exec.shutdown();
        while( exec.isTerminated() ) {
            System.out.println( "Master: waiting for theads to finish");
            Thread.sleep( 10000 );
        }
    }


    private <M extends RequestRunner> List<RequestRunner> createRequestRunners( Class<M> r,  List<Properties> pts ) throws IllegalAccessException, InstantiationException {

        List<RequestRunner> res = new ArrayList<RequestRunner>( );
        for( Properties prop : pts ) {
            RequestRunner sr =  r.newInstance( );
            sr.setGorfxUri( gorfxEpUrl );
            sr.fromProps( prop );
            sr.prepare();
            res.add( sr );
        }
        return res;
    }

}

