package de.zib.gndms.GORFX.action;

import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.logic.model.gorfx.FileTransferTaskAction;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
import de.zib.gndms.model.gorfx.types.InterSliceTransferORQ;
import de.zib.gndms.model.gorfx.Task;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 17:45:47
 */
public class InterSliceTransferTaskAction extends ORQTaskAction<InterSliceTransferORQ> {


    public InterSliceTransferTaskAction() {
    }


    public InterSliceTransferTaskAction( @NotNull EntityManager em, @NotNull Task model ) {
        super( em, model );
    }


    public InterSliceTransferTaskAction( @NotNull EntityManager em, @NotNull String pk ) {
        super( em, pk );
    }


    protected void onInProgress( @NotNull Task model ) {

        try {
            InterSliceTransferORQCalculator.checkURIs( getOrq( ) );
        } catch ( Exception e ) {
            fail( new RuntimeException( e.getMessage( ), e ) );
        }

        FileTransferTaskAction fta = new FileTransferTaskAction( getEntityManager(), model );
        fta.setClosingEntityManagerOnCleanup( false );
        fta.call( );
    }


    @NotNull
    protected Class<InterSliceTransferORQ> getOrqClass() {
        return InterSliceTransferORQ.class;
    }
}
