package de.zib.gndms.comserv.delegation;

import sun.misc.UUDecoder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 11.02.2009, Time: 13:00:32
 */
public class MyUUDecoder extends UUDecoder {


    @Override
    protected void decodeBufferSuffix( final PushbackInputStream is, final OutputStream os ) throws IOException {

        int  c;
        byte bb[] = new byte[4];
        c = is.read( bb );
        String end = new String( bb );
        System.out.println( c );
        System.out.println( end );
   //     if ((bb[0] != 'e') || (bb[1] != 'n') ||
   //         (bb[2] != 'd')) {
   //         throw new CEFormatException("UUDecoder: Missing 'end' line.");
   //     }
    }
}
