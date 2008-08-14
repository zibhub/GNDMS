package de.zib.gndms.logic.model;

import de.zib.gndms.model.common.GridResource;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 13.08.2008, Time: 14:51:10
 */
public abstract class CreateGridResourceAction<M extends GridResource, R> extends AbstractModelAction<M, R> {

    private String id;


    protected CreateGridResourceAction( ) {
        
    }


    protected CreateGridResourceAction( @NotNull String id ) {

        this.id = id;
    }


    @Override
    public void initialize( ) {

        if( id == null )
            throw new IllegalStateException( "No resource id provided" );;

        super.initialize();
    }


    public String getId() {
        return id;
    }


    public void setId( @NotNull String id ) {
        this.id = id;
    }
}
