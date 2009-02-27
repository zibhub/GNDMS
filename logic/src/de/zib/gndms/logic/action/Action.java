package de.zib.gndms.logic.action;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.List;


/**
 * Interface for an action which requires a EntityManager.
 *
 * Its action will be executed concurrently and the system will wait for its result, but not until really needed
 * .
 * The action must be implemented in {@code call()} and invoked by an {@code Executor}.
 *
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 12.08.2008, Time: 15:25:36
 */

@SuppressWarnings({ "InterfaceNamingConvention" })
public interface Action<R> extends Callable<R> {

    /**
     * Will be invoked before {@code execute()} when this is submitted to an {@code Executor}.
     *
     */
    void initialize( );


    /**
     * An implementing class must declare an action.
     *
     * Do not call this method directly and use an {@link Executor} instead to execute
     * the computation in a seperate thread.
     *
     *
     * The system will wait for the result not until really needed.
     * See {@link Executor} about the invocation and retrieving of the result
     * 
     * @return the calculated result
     * @throws ActionInitializationException
     * @throws RuntimeException
     */
    R call( ) throws ActionInitializationException, RuntimeException;

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
}
