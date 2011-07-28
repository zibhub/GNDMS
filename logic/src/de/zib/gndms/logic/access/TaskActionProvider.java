package de.zib.gndms.logic.access;

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



import de.zib.gndms.logic.model.TaskAction;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.lang.reflect.InvocationTargetException;


/**
 *
 * A TaskActionProvider is used to return TaskActions, which correspond to TaskFlowType objects using KeyFactories.
 *
 *
 * @see TaskAction
 * @see de.zib.gndms.model.gorfx.OfferType
 * @see de.zib.gndms.model.common.types.factory.KeyFactory
 * @see de.zib.gndms.model.common.types.factory.KeyFactoryInstance
 * @see de.zib.gndms.model.common.types.factory.IndustrialPark
 * 
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 05.12.2008 Time: 17:51:48
 */
@SuppressWarnings(
	  { "OverloadedMethodsWithSameNumberOfParameters", "MethodWithTooExceptionsDeclared" })
public interface TaskActionProvider {


}
