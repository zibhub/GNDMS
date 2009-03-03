package de.zib.gndms.kit.network;

import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ServerException;
import org.globus.ftp.exception.ClientException;

import java.util.concurrent.Callable;
import java.io.IOException;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 20.02.2009, Time: 17:40:03
 */
public class GridFTPClientCreator implements Callable<GridFTPClient>  {

    private String host;
    private int port;


    public GridFTPClientCreator() {
    }


    public GridFTPClientCreator( final String host, final int port ) {
        this.host = host;
        this.port = port;
    }


    public GridFTPClient call() throws ServerException, IOException, InterruptedException, ClientException {
        GridFTPClient cnt = new GridFTPClient( host, port );
        validateClient( cnt );
        return cnt;
    }


    private void validateClient( final GridFTPClient cnt ) throws ServerException, IOException, ClientException {
        boolean d = false;
        try {
            System.out.println( "validating client" );
            cnt.authenticate( null );
            //cnt.getFeatureList();
            cnt.list();
            d = true;
            System.out.println( "done" );
        } finally {
            System.out.println( "finally reached with d = " + Boolean.toString( d ) );
            if( d == false ) {
                System.out.println( "closing" );
                cnt.close();
                System.out.println( "done2" ); 
            }
        }
    }


    public String getHost() {
        return host;
    }


    public void setHost( final String host ) {
        this.host = host;
    }


    public int getPort() {
        return port;
    }


    public void setPort( final int port ) {
        this.port = port;
    }
}
