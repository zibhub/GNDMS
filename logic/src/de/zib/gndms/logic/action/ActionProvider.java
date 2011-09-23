package de.zib.gndms.logic.action;
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

import java.util.List;
import de.zib.gndms.common.logic.action.ActionMeta;

/**
 * @author try ma ik jo rr a zib
 * @date: 08.02.11 16:12
 *
 * @brief Something that provides actions by name.
 */
public interface ActionProvider<M extends ActionMeta, A extends Action> {

    List<String> listAvailableActions();
    M getMeta( String config ) throws NoSuchActionException;

    A getAction( String actionName ) throws NoSuchActionException;
}
