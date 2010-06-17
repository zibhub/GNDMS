package de.zib.gndms.model.gorfx.types.io;

import java.util.Map;

/**
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 01.10.2008, Time: 16:43:40
 */
public interface FileTransferORQWriter extends ORQWriter {

    public void writeSourceURI( String uri );
    public void writeDestinationURI( String uri );

    public void writeFileMap( Map<String,String> fm );
}
