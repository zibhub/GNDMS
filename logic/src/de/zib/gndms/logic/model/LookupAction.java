package de.zib.gndms.logic.model;

import de.zib.gndms.model.common.GridEntity;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;

/**
 * An action to look up items from the data base by their key.
 *
 * Delegates calls to the 
 * 
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 18.08.2008, Time: 14:15:53
 */
public class LookupAction<M extends GridEntity, K> extends AbstractEntityAction<M> {

    private K primaryKey;
    private Class<?> returnClass;


    public LookupAction( Class<? extends GridEntity> returnClass ) {
        this.returnClass = returnClass;
    }

    public LookupAction( Class<? extends GridEntity> returnClass,  K primaryKey ) {
        this.returnClass = returnClass;
        this.primaryKey = primaryKey;
    }

    public void initialize( ) {
        super.initialize( );
        requireParameter( "primaryKey", primaryKey );
        requireParameter( "return value class", returnClass );
    }

    public M execute( @NotNull EntityManager em ) {

        return ( M ) em.find( returnClass, primaryKey );
    }

    public K getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey( K primaryKey ) {
        this.primaryKey = primaryKey;
    }
}
