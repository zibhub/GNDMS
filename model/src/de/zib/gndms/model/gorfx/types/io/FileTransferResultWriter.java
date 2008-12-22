package de.zib.gndms.model.gorfx.types.io;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 13:06:29
 */
public interface FileTransferResultWriter extends TaskResultWriter {

    public void writeFiles( String[] files );
}
