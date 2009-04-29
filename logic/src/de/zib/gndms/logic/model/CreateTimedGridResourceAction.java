package de.zib.gndms.logic.model;

import de.zib.gndms.model.common.GridResource;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

/**
 * A CreateGridResourceAction with a termination time.
 *
 * A termination time has to be denoted before {@link #initialize()} is called.
 *
 * The first template parameter is the model type. The second parameter is the return type of this action.
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
