package de.zib.gndms.logic.access;

import de.zib.gndms.logic.model.TaskAction;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.lang.reflect.InvocationTargetException;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 05.12.2008 Time: 17:51:48
 */
@SuppressWarnings(
	  { "OverloadedMethodsWithSameNumberOfParameters", "MethodWithTooExceptionsDeclared" })
public interface TaskActionProvider {
   
	TaskAction newTaskAction(
	        @NotNull EntityManagerFactory emf,
	        @NotNull String offerTypeKey)
	        throws ClassNotFoundException, IllegalAccessException, InstantiationException,
	        NoSuchMethodException, InvocationTargetException;

	TaskAction newTaskAction(
		  EntityManager emParam, String offerTypeKey)
		  throws IllegalAccessException, InstantiationException, ClassNotFoundException;
}
