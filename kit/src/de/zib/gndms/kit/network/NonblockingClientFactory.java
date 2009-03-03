package de.zib.gndms.kit.network;

import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.Map;
import java.util.HashMap;

import de.zib.gndms.stuff.qexecuter.QueuedExecutor;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 20.02.2009, Time: 17:37:59
 */
public class NonblockingClientFactory extends AbstractGridFTPClientFactory{

    private int timeout = 10;
    private TimeUnit unit = TimeUnit.SECONDS;
    private int delay = 1000; // in ms
    private ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool( 1 );
    private Map<String, QueuedExecutor> hostExecutores = new HashMap<String, QueuedExecutor>( );
    private Logger log = Logger.getLogger( NonblockingClientFactory.class );


    public GridFTPClient createClient( String host, int port ) throws ServerException, IOException {

        QueuedExecutor exec;
        synchronized( hostExecutores ) {
            if( hostExecutores.containsKey( host ) ) {
                log.debug( "Returning executor for host: " + host );
                exec = hostExecutores.get( host ) ;
            } else {
                exec = new QueuedExecutor( scheduledExecutor );
                log.debug( "Creating executor for host: " + host );
                exec.setDefaultDelay( delay );
                hostExecutores.put( host, exec );
            }
        }

        GridFTPClientCreator c = new GridFTPClientCreator( host, port );
        Future<GridFTPClient> f = exec.submit( c );
        try {
            try{
                return f.get( exec.actualTimeout( f, timeout, unit ), unit );
            } catch ( TimeoutException e ) {
                log.info( "GridFTPClient get() create exceeded timeout" );
                f.cancel( true );
            }
         //   System.err.println( "awaiting termination" );
         //   exec.shutdown();
         //   exec.awaitTermination( timeout, TimeUnit.SECONDS );
         //   System.err.println( "done" );
        } catch ( InterruptedException e ) {
            e.printStackTrace(  );
            throw new RuntimeException( "GridFTPClient create exceeded timeout" );
        } catch ( ExecutionException e ) {
            // this mustn't happen here due to the blocked wait op
            e.printStackTrace();
        }
        return null;
    }


    public void shutdown() {

        log.info( "shuting down executors" );
        for( String hn: hostExecutores.keySet() ) {
            hostExecutores.get( hn ).shutdown();
        }

        /*
        log.debug( "awaiting termination" );
        for( String hn: hostExecutores.keySet() ) {
            try {
                hostExecutores.get( hn ).awaitTermination( timeout, TimeUnit.SECONDS );
            } catch ( InterruptedException e ) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        */
    }
}
