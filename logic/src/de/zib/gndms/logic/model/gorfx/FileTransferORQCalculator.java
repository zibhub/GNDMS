package de.zib.gndms.logic.model.gorfx;

import org.jetbrains.annotations.NotNull;
import de.zib.gndms.kit.factory.Factory;
import de.zib.gndms.model.gorfx.types.FileTransferORQ;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 16:30:22
 */
public class FileTransferORQCalculator extends AbstractTransferORQCalculator<FileTransferORQ, FileTransferORQCalculator> {

    public FileTransferORQCalculator( ) {
        super( FileTransferORQ.class );
    }
}
