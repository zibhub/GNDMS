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



import de.zib.gndms.logic.action.Action;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.common.ModelUUIDGen;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;


/**
 * An EntityAction is an Action containing an EntityManager and a list of actions being executed on cleanup.
 *
 * The template parameter is the return type.
 iaksldfja*
 * @see javax.persistence.EntityManager
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 14.08.2008 Time: 14:44:57
 */
public interface EntityAction<R> extends Action<R>, ModelUUIDGen {


    /**
     * Returns the EntityManager as set by {@code setOwnEntityManager}.
     * If not set, the first EntityManger of the parents of this action, being not {@code null}, will be returned.
     * If no parent has an EntityManager, {@code null} will be returned.
     *
     * @return the first appearance of an EntityManager in the parent chain of this action
     */
    EntityManager getEntityManager();

    /**
     * Returns the EntityManager as set by {@code setOwnEntityManager}
     * @return the EntityManager as set by {@code setOwnEntityManager}
     */
    EntityManager getOwnEntityManager();

    /**
     * Sets the EntityManager the EntityAction will use.
     *
     * @param entityManagerParam
     */
    void setOwnEntityManager(final @NotNull EntityManager entityManagerParam);


    /**
     * Returns the postponed actions.
     *
     * @return the postponed actions
     */
    BatchUpdateAction<GridResource, ?> getPostponedEntityActions();

    /**
     * Define actions that will be executed on {@code this.cleanup()}
     * 
     * @param postponedActionsParam a BatchUpdateAction containing actions, that will be executed on cleanup
     */
    void setOwnPostponedEntityActions(final @NotNull BatchUpdateAction<GridResource, ?> postponedActionsParam);

}
