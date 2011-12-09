package de.zib.gndms.logic.action;
/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import de.zib.gndms.logic.model.*;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.common.ModelUUIDGen;
import de.zib.gndms.neomodel.common.Dao;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.UUID;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 09.12.11  15:44
 * @brief
 */
public class ActionConfigurer implements ModelUUIDGen {

    private EntityManagerFactory entityManagerFactory;
    private final ModelUpdateListener<GridResource> entityUpdateListener = new NoWSDontNeedModelUpdateListener();
    private Dao dao;


    public ActionConfigurer() {

        super();
    }


    public <R> EntityAction<R> configureAction( final EntityAction<R> action ) {

        final EntityManager ownEm = action.getOwnEntityManager();
        if (ownEm != null)
            return configure_( action );
        else {
            final @NotNull EntityManager em = entityManagerFactory.createEntityManager();
            return configureAction( em, action );
        }
    }


    public <R> ModelDaoAction<?,R> configureDaoAction( final ModelDaoAction<?, R> action ) {

        final Dao dao = action.getOwnDao();
        if (dao != null)
            return ( ModelDaoAction<?, R> ) configureAction( action );
        else {
            action.setOwnDao( getDao() );
            return ( ModelDaoAction<?, R> ) configureAction( action );
        }
    }


    public <R> EntityAction<R> configureAction( final EntityManager em, final EntityAction<R> action ) {

        action.setOwnEntityManager( em );
        // todo EntityAction interface has no close on cleanup  flag
        return configure_( action );
    }


    public <R> ModelDaoAction<?,R> configureDaoAction( final EntityManager em, final Dao dao,
                                              final ModelDaoAction<?, R> action )
    {
        action.setOwnDao( dao );
        return ( ModelDaoAction<?, R> ) configureAction( em, action );
    }


    protected <R> EntityAction<R> configure_( final EntityAction<R> action ) {

        if (action.getPostponedEntityActions() == null)
            action.setOwnPostponedEntityActions(new DefaultBatchUpdateAction<GridResource>());
        if (action.getPostponedEntityActions().getListener() == null)
            action.getPostponedEntityActions().setListener(getEntityUpdateListener());
        if (action instanceof ModelDaoAction )
            ((ModelDaoAction ) action).setOwnDao( getDao() );
        if (action instanceof AbstractEntityAction )
            ((AbstractEntityAction<?> )action).setUUIDGen( this );



        return action;
    }


    @PersistenceUnit
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }


    public ModelUpdateListener<GridResource> getEntityUpdateListener() {
        return entityUpdateListener;
    }


    public Dao getDao() {
        return dao;
    }


    @Inject
    public void setDao( Dao dao ) {
        this.dao = dao;
    }


    @NotNull
    @Override
    public String nextUUID() {

        return UUID.randomUUID().toString();
    }
}
