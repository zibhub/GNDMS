package de.zib.gndms.kit.network;

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


import de.zib.gndms.kit.access.CredentialProvider;
import de.zib.gndms.stuff.threading.Forkable;
import de.zib.gndms.stuff.threading.QueuedExecutor;
import org.apache.log4j.Logger;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author try ma ik jo rr a zib
 * 
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 20.02.2009, Time: 17:37:59
 */
public class StrictNonblockingClientFactory extends AbstractGridFTPClientFactory{
    private static final Logger log = Logger.getLogger( StrictNonblockingClientFactory.class );

    private int timeout = 20;
    private final TimeUnit unit = TimeUnit.SECONDS;
    private long delay = 500; // in ms
   //private final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool( 1 );
   //private final Map<String, QueuedExecutor> hostExecutors = new HashMap<String, QueuedExecutor>( );
    private ExecutorService exec = Executors.newFixedThreadPool( 1 );
    private int count=0;


    public GridFTPClient createClient( String host, int port, CredentialProvider cp ) throws ServerException, IOException {

        final GridFTPClientCreator creator = new GridFTPClientCreator( host, port, cp, inc() );
        final Forkable<GridFTPClient> fork = new Forkable<GridFTPClient>( creator );


        final Future<GridFTPClient> f;
        log.info( "submitting creator " +creator.getHost() +" " + creator.getSeq() );
        synchronized ( exec ) {
            f = exec.submit( fork );
        }

        try {
            try{
                return f.get( timeout, unit );
            } catch ( TimeoutException e ) {
                creator.getLog().info( "GridFTPClient get() create exceeded timeout", e );
                fork.setShouldStop( true );
                f.cancel( true );
            }
        } catch ( InterruptedException e ) {
            creator.getLog().debug( e );
            throw new RuntimeException( "GridFTPClient create interrupted", e );
        } catch ( ExecutionException e ) {
            // this mustn't happen here due to the blocked wait op
            creator.getLog().debug( e );
            if( e.getCause() instanceof ServerException )
                throw ServerException.class.cast( e.getCause() );

            throw new RuntimeException( e );
        }
        return null;
    }


    private synchronized int inc() {
        return ++count;
    }


    public void shutdown() {

      //  log.info( "shuting down executors" );
      //  for( String hn: hostExecutors.keySet() ) {
      //      hostExecutors.get( hn ).shutdown();
      //  }

        /*
        log.debug( "awaiting termination" );
        for( String hn: hostExecutors.keySet() ) {
            try {
                hostExecutors.get( hn ).awaitTermination( timeout, TimeUnit.SECONDS );
            } catch ( InterruptedException e ) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        */
    }


    public int getTimeout() {
        return timeout;
    }


    public void setTimeout( int timeout ) {
        
        if( timeout < 0 )
            throw new IllegalArgumentException( "Timeout must be greater or equal 0" );
        
        this.timeout = timeout;
    }


    public long getDelay() {
        return delay;
    }


    public void setDelay( int delay ) {

        if( delay < 0 )
            throw new IllegalArgumentException( "Delay must be greater or equal 0" );

        this.delay = delay;

       // for( String hn: hostExecutors.keySet() ) {
       //     hostExecutors.get( hn ).setDefaultDelay( delay );
       // }
    }
}