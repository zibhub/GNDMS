package de.zib.gndms.kit.monitor;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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



import org.jetbrains.annotations.NotNull;


import java.io.PrintWriter;


/**
 * Decoupling interface between GNDMSystem and GroovyMMoniServer
 *
 * A class implementing the ActionCaller interface must provide a method to created specified ConfigActions
 * and return the result of the ConfigAction's {@code call()} invokation.
 * 
 * @see de.zib.gndms.logic.model.config.ConfigAction
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 15.08.2008 Time: 13:02:48
 */
public interface ActionCaller {

  /**
     * Creates a new ConfigAction object of the specified class {@code className}, sets its options according to
     * {@code opts} (see {@link de.zib.gndms.logic.model.config.ConfigAction#parseLocalOptions(String)})
     * and returns the result of the action's {@code call()} invokation.
     * All outputs are written to the PrintWriter. The postponed actions of the created action must
     * also be invoked.
     *
     *
     * @param className the classname of the ConfigAction which shall be created
     * @param opts A String containing the configuration for the ConfigAction
     * @param writer a printwriter where all output, generated by the ConfigAction, will be written to
     * @return result of the ConfigAction's computation
     * @throws Exception
     */
    Object callAction(final @NotNull String className, final @NotNull String opts,
                      final @NotNull PrintWriter writer) throws Exception;

}
