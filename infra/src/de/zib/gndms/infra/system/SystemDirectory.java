package de.zib.gndms.infra.system;

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



import de.zib.gndms.kit.access.InstanceProvider;
import de.zib.gndms.infra.access.ServiceHomeProvider;
import de.zib.gndms.logic.access.TaskActionProvider;
import de.zib.gndms.kit.configlet.ConfigletProvider;
import de.zib.gndms.kit.system.SystemInfo;
import de.zib.gndms.logic.model.gorfx.ORQCalculatorProvider;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 05.12.2008 Time: 17:55:14
 */
public interface SystemDirectory
	  extends ConfigletProvider, ORQCalculatorProvider, TaskActionProvider, InstanceProvider,
	  ServiceHomeProvider, SystemInfo {
}
