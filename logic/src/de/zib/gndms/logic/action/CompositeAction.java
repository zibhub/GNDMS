package de.zib.gndms.logic.action;

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



/**
 * A CompositeAction is an {@code action}, which can use several actions of another type to return its result.
 *
 * Thus, an implementation could execute a list of actions and return itself a result, 
 * based upon the results of the executed actions.
 *
 * The first template parameter is the return type, the second is the type of the other actions, wich can be added.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 12.08.2008 Time: 18:06:11
 */
public interface CompositeAction<R,V> extends Action<R> {

    void addAction(Action<V> actionParam);
}
