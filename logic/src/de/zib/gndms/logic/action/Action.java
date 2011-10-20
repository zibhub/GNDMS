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



import de.zib.gndms.kit.access.InjectorProvider;
import de.zib.gndms.stuff.GNDMSInjector;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.List;


/**
 * Interface for an action.  Actions are atomic activities that are carried out by the system,
 *
 * A new action is implemented by defining {@code initialize()}, {@code call()}, and {@code cleanUp()}.
 *
 * {@code call()} is expected to be shaped like {@code initialize(); ... try { somehting() } finally { cleanup() }}
 * This pattern is realized by the default implementation AbstractAction.
 *
 * The template parameter R is the type of the result that is computed by the action.
 *
 * Actions may be executed concurrently using an {@code Executor}. Alternatively, an action may be executed
 * blockingly.
 *
 * Every action may have a parent unless it is a top-level action.  This means, hierarchies of nested
 * actions may be created.
 *
 *
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 *
 * User: mjorra, Date: 12.08.2008, Time: 15:25:36
 */

@SuppressWarnings({ "InterfaceNamingConvention" })
public interface Action<R> extends Callable<R>, InjectorProvider {

    /**
     * Will be invoked before {@code execute()} when this is submitted to an {@code Executor}.
     */
    void initialize( );


    /**
     * An implementing class must declare its action here.
     *
     *
     * Do not call this method directly unless and use an {@link Executor} instead to execute
     * the computation in a seperate thread.
     *
     * @return the calculated result
     * @throws ActionInitializationException
     * @throws RuntimeException
     */
    R call( ) throws ActionInitializationException;

    /**
     * Declare what needs to be done after the action has been finished
     */
    void cleanUp( );

	Action<?> getParent();

    void setParent(Action<?> parent);

    /**
     * Returns the next parent of this, which is an instance of {@code interfaceClass}
     * 
     * @param interfaceClass the class, this or one of its parents should be an instance of.
     * @return the next parent of this, which is an instance of {@code interfaceClass}
     */
    @Nullable <V> V nextParentOfType(final @NotNull Class<V> interfaceClass);

    @NotNull <V extends Action<?>> List<V> getParentChain(final @NotNull Class<V> interfaceClass);

    void setInjector(GNDMSInjector anInjector);
}
