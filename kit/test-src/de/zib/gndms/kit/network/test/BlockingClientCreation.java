package de.zib.gndms.kit.network.test;

import de.zib.gndms.kit.network.NonblockingClientFactory;
import de.zib.gndms.kit.network.GridFTPClientCreator;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;

import java.io.IOException;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 20.02.2009, Time: 18:15:30
 */
public class BlockingClientCreation {

    public static void main( String[] args ) throws ServerException, IOException, InterruptedException {
        NonblockingClientFactory nbc = new NonblockingClientFactory();
        System.out.println( "precreate" );

        GridFTPClient cln = nbc.createClient( "mardschana2.zib.de", 2811 );
//        GridFTPClient cln = nbc.createClient( "hallo", 123 );
        System.out.println( "postcreate" );
        if( cln != null )
            cln.close();

        nbc.shutdown();
        //Thread.sleep( 600000 );
    }
}
