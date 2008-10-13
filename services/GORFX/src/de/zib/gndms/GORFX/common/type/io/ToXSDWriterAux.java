package de.zib.gndms.GORFX.common.type.io;

import org.apache.axis.message.MessageElement;

import java.util.ArrayList;
import java.util.Arrays;

import de.zib.gndms.GORFX.common.GORFXClientTools;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 13:33:10
 */
public class ToXSDWriterAux {

    public MessageElement[] fromArray( Object[] objs ) {

        ArrayList<MessageElement> al = new ArrayList();
        for( Object o: Arrays.asList( objs ) ) {
        }
    }

}
