package de.zib.gndms.stuff.devel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 22.10.2009, Time: 10:04:14
 */
public class StreamConsumer implements Runnable {

    private InputStream source;
    private OutputStream destination;



    public void run() {

        try {
            StreamCopyNIO.copyStream( source, destination, 10 * 1024 );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }


    public InputStream getSource() {
        return source;
    }


    public void setSource( InputStream source ) {
        this.source = source;
    }


    public OutputStream getDestination() {
        return destination;
    }


    public void setDestination( OutputStream destination ) {
        this.destination = destination;
    }
}
