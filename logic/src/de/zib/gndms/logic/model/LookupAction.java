package de.zib.gndms.logic.model;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import de.zib.gndms.model.common.GridEntity;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;

/**
 * An action to look up items from the data base by their key.
 *
 * Delegates calls to the 
 * 
 * @author  try ma ik jo rr a zib
 * @version  $Id$
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
