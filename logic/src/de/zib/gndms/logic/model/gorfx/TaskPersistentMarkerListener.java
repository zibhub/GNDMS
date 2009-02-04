package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.kit.network.PersistentMarkerListener;
import de.zib.gndms.kit.util.WidAux;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.AbstractTask;
import org.globus.ftp.Marker;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 11.10.2008, Time: 18:47:42
 *
 * A persistent marker listener which updates the progress of a task.
 *
 * The maximum progress is the number of files to transfer hence one progress step is the completed transfer
 * of a single file.
 */
public class TaskPersistentMarkerListener extends PersistentMarkerListener {

    AbstractTask task;


    @Override
    public void markerArrived( final Marker marker ) {
        try{
            WidAux.initWid( task.getWid() );
            super.markerArrived( marker );
        } finally{
            WidAux.removeWid( );
        }
    }


    public void setCurrentFile( String currentFile ) {
        super.setCurrentFile( currentFile );
        task.setProgress( task.getProgress( ) + 1 );
    }


    public AbstractTask getTask() {
        return task;
    }


    public void setTask( AbstractTask task ) {
        this.task = task;
    }
}
