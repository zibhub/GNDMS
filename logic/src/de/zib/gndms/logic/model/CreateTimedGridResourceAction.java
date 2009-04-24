package de.zib.gndms.logic.model;

import de.zib.gndms.model.common.GridResource;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

/**
 * A CreateGridResourceAction with a termination time
 *
 * @see de.zib.gndms.logic.model.CreateGridResourceAction
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * 
 * User: mjorra, Date: 13.08.2008, Time: 14:57:06
 */
public abstract class CreateTimedGridResourceAction<M extends GridResource, R>
        extends CreateGridResourceAction<M, R> {

    private Calendar terminationTime;

    protected CreateTimedGridResourceAction( ) {

    }

    
    protected CreateTimedGridResourceAction( @NotNull String id,
                                             @NotNull Calendar terminationTimeParam ) {
        super( id );
        terminationTime = terminationTimeParam;
    }

    
    @Override
    public void initialize( ) {
        requireParameter("terminationTime", getTerminationTime());
        super.initialize( );
    }
    

    public Calendar getTerminationTime() {
        return terminationTime;
    }


    public void setTerminationTime( Calendar terminationTimeParam ) {
        terminationTime = terminationTimeParam;
    }
}
