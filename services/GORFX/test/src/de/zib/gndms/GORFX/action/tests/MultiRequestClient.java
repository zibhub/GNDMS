package de.zib.gndms.GORFX.action.tests;

import de.zib.gndms.kit.application.AbstractApplication;
import de.zib.gndms.stuff.propertytree.PropertyTreeFactory;
import de.zib.gndms.stuff.propertytree.PropertyTree;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This client reads multiple staging orqs from a file and sends them to a given site using mulitple threads
 *
 * @author Maik Jorra <jorra@zib.de>
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

        List<Properties> pl = new ArrayList<Properties>( );

        PropertyTree st;
        for ( int i=1; ( st = pt.subTree( "ORQ" + i ) ) != null; ++i )
            pl.add( st.asProperties( true ) );

        if( pl.size() < 1 )
            throw new IllegalStateException( "Couldn't read orq information" );

        System.out.println( "Creating " + pl.size() + " staging threads" );

        List<StagingRunner> runner = createStagingRunners( pl );

        ExecutorService exec = Executors.newFixedThreadPool( runner.size() );

        for( StagingRunner run : runner )  {
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


    private List<StagingRunner> createStagingRunners(  List<Properties> pts ) {

        List<StagingRunner> res = new ArrayList<StagingRunner>( );
        for( Properties prop : pts ) {
            StagingRunner sr =  new StagingRunner( prop, gorfxEpUrl );
            sr.prepare();
            res.add( sr );
        }

        return res;
    }

}

