package de.zib.gndms.stuff.devel;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 29.10.2009, Time: 12:37:48
 */
public class StreamCopyNIO {

    static final int BUFFER_SIZE = 4096;



    /**
     * Copies the content of is to os using a buffer of size bs.
     *
     * Copying is performed using non-blocking io channels. The source and destionation streams are NOT closed
     * automatically.
     *
     * @param is The source stream.
     * @param os The destination stream.
     * @param bs The buffer size
     * @return The count of copied bytes.
     * @throws IOException
     */
    public static int copyStream ( InputStream is, OutputStream os, int bs ) throws IOException {

        ReadableByteChannel sc = Channels.newChannel( is );
        WritableByteChannel oc = Channels.newChannel( os );
        ByteBuffer buf = ByteBuffer.allocate( bs );
        int bc;
        int tbc = 0;

        // fill the buffer
        while( ( bc = sc.read( buf ) ) != -1 ) {

            buf.flip(); // set into read mode

            // depending on the destination this might be necessary
            // but normally the channel will write the entire buffer.
            while ( buf.hasRemaining() )
                // write buffer to channel output
                oc.write( buf );
            
            // flush it
            buf.clear();
            tbc += bc;
        }

        return tbc;
    }


    /**
     * Same as above but using a default buffer size.
     * @param is
     * @param os
     * @return
     * @throws IOException
     */
    public static int copyStream ( InputStream is, OutputStream os  ) throws IOException {
        return copyStream( is, os, BUFFER_SIZE );
    }
}
