package de.zib.gndms.infra.action;

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



import de.zib.gndms.kit.monitor.ActionCaller;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;

/**
 * A class implementing the WSActionCaller interface must provide a method to created specified ConfigActions
 * and return the result of the ConfigAction's {@code call()} invocation.
 *
 * Note: WSActionCaller is intended to be applicably only for actions implementing the {@code PublicAccess} interface. 
 *
 * @see PublicAccessible
 * @see ActionCaller
 * @author  try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 26.01.2009, Time: 18:28:25
 */
public interface WSActionCaller extends ActionCaller {

    // public actions must implement the PublicAccess interface.

    /**
     * Creates a new ConfigAction object of the specified class {@code className}, sets its options according to
     * {@code opts} (see {@link de.zib.gndms.logic.model.config.ConfigAction#parseLocalOptions(String)})
     * and returns the result of the action's {@code call()} invokation.
     * All outputs are written to the PrintWriter. The postponed actions of the created action must
     * also be invoked.
     *
     * Note: the specified ConfigAction must implement the {@link PublicAccessible} interface.
     *
     * @param className the classname of the ConfigAction which shall be created
     * @param opts A String containing the configuration for the ConfigAction
     * @param writer a printwriter where all output, generated by the ConfigAction, will be written to
     * @return result of the ConfigAction's computation
     * @throws Exception
     */
    Object callPublicAction(final @NotNull String className, final @NotNull String opts,
                            final @NotNull PrintWriter writer) throws Exception;

}
