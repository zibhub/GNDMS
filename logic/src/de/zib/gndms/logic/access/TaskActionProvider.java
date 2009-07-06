package de.zib.gndms.logic.access;

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
