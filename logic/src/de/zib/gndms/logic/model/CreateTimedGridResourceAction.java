package de.zib.gndms.logic.model;

import de.zib.gndms.model.common.GridResource;

import java.util.Calendar;

import org.jetbrains.annotations.NotNull;

/**
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

    
    protected CreateTimedGridResourceAction( @NotNull String id, @NotNull Calendar terminationTime ) {
        super( id );
        this.terminationTime = terminationTime;
    }

    
    @Override
    public void initialize( ) {

        if( terminationTime == null )
            throw new IllegalStateException( "No termination time provided" );

        super.initialize( );
    }
    

    public Calendar getTerminationTime() {
        return terminationTime;
    }


    public void setTerminationTime( Calendar terminationTime ) {
        this.terminationTime = terminationTime;
    }
}
