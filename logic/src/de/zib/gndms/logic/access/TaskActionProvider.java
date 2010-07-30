package de.zib.gndms.logic.access;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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



import de.zib.gndms.logic.model.TaskAction;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.lang.reflect.InvocationTargetException;


/**
 *
 * A TaskActionProvider is used to return TaskActions, which correspond to OfferType objects using KeyFactories.
 *
 *
 * @see TaskAction
 * @see de.zib.gndms.model.gorfx.OfferType
 * @see de.zib.gndms.model.common.types.factory.KeyFactory
 * @see de.zib.gndms.model.common.types.factory.KeyFactoryInstance
 * @see de.zib.gndms.model.common.types.factory.IndustrialPark
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 05.12.2008 Time: 17:51:48
 */
@SuppressWarnings(
	  { "OverloadedMethodsWithSameNumberOfParameters", "MethodWithTooExceptionsDeclared" })
public interface TaskActionProvider {


    /**
     * Creates an EntityManager and calls {@link #newTaskAction(javax.persistence.EntityManager, String)}
     *
     * @param emf
     * @param offerTypeKey
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
	TaskAction newTaskAction(
	        @NotNull EntityManagerFactory emf,
	        @NotNull String offerTypeKey)
	        throws ClassNotFoundException, IllegalAccessException, InstantiationException,
	        NoSuchMethodException, InvocationTargetException;

    /**
     * Retrieves the {@link de.zib.gndms.model.gorfx.OfferType} object, which has {@code offerTypeKey} as its key
     * in the database and is managed by the EntityManager {@code em}.
     *
     * A TaskAction is then returned which corresponds to the OfferType key object. 
     *
     *
     * @param emParam ane EntityManager which manages OfferType objects
     * @param offerTypeKey the primary key for a specific OfferType object
     * @return a TaskAction corresponding to the OfferTypeKey
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
	TaskAction newTaskAction(
		  EntityManager emParam, String offerTypeKey)
		  throws IllegalAccessException, InstantiationException, ClassNotFoundException;
}
