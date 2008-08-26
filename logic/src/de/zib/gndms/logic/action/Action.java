package de.zib.gndms.logic.action;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.List;


/**
 * Interface for an action which requires a EntityManager
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 12.08.2008, Time: 15:25:36
 */

@SuppressWarnings({ "InterfaceNamingConvention" })
public interface Action<R> extends Callable<R> {

    void initialize( );

    R call( ) throws ActionInitializationException, RuntimeException;

    void cleanUp( );

	Action<?> getParent();

    void setParent(Action<?> parent);
    
    @Nullable <V> V nextParentOfType(final @NotNull Class<V> interfaceClass);

    @NotNull <V extends Action<?>> List<V> getParentChain(final @NotNull Class<V> interfaceClass);
}
