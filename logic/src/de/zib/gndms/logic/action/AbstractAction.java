package de.zib.gndms.logic.action;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;


/**
 *
 * This class provides a default implementation of the {@code Action} Interface.
 *
 * An implementing action just has to define its action by implementing the {@code execute()} method.
 *
 * An action is started by invoking {@code call()}. If the action should compute the result concurrently,
 * submit it to an Executor, instead of directly invoking {@code call()]}}.
 *
 * The first template parameter is the return type.
 * 
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * 
 * User: mjorra, Date: 12.08.2008, Time: 15:33:10
 */
@SuppressWarnings({ "NoopMethodInAbstractClass" })
public abstract class AbstractAction<R> implements Action<R> {
	private Action<?> parent;

    /**
     * Will be invoked before {@code execute()} when this is submitted to an {@code Executor}.
     * Does nothing by default, but can be overridden by subclasses.
     */
    public void initialize() { }

    /**
     * Will be invoked after {@code execute()} when this is submitted to an {@code Executor}.
     * Does nothing by default, but can be overridden by subclasses.
     */
    public void cleanUp() { }



   /**
     * Do not call this method directly unless you don't need it to be executed concurrently.
     * 
     * An implementing should not override this method.
     * Use {@link AbstractAction#execute()} } instead
     *
     * If concurrent execution is neeed this has to be submitted to an {@code Executor}.
     * The system will wait for the result not until really needed.
     * See {@link Executor} about the invocation and retrieving of the result
     *
     * @return the calculated result
     * @throws ActionInitializationException
     * @throws RuntimeException
     */
    public R call ( ) throws RuntimeException {
        try {
	        initialize( );
        }
        catch (ActionInitializationException ae) {
            throw ae;
        }
        catch (RuntimeException re) {
	        throw new ActionInitializationException(re);
        }
        try {
            return execute( );
        }
        finally {
            cleanUp( );
        }
    }

    /**
     * An implementing action class can define here its action, being executed concurrently
     * @return the calculated result
     */
	public abstract R execute();


	public final Action<?> getParent() {
		return parent;
	}


	public final void setParent(final Action<?> parentParam) {
		if (parent != null)
			throw new IllegalStateException("Cannot overwrite parent");

		parent = parentParam;
	}

    
    public final @Nullable <V> V nextParentOfType(final @NotNull Class<V> interfaceClass) {
		return nextParentOfType(interfaceClass, getParent());
	}

    @NotNull
    public final <V extends Action<?>> List<V> getParentChain(
            final @NotNull Class<V> interfaceClass) {
        return getParentChain(interfaceClass, getParent());
    }


    /**
     * Returns the next parent of {@code startParent}, which is an instance of {@code interfaceClass}.
     *
     * @param interfaceClass the class, {@code startParent} or one of its parents should be an instance of.
     * @param startParent the first appearance in the parentchain, starting from {@code startParent}
     * being an instance of {@code interfaceClass} is searched for
     * @return the first parent in the parentchain of {@code startParent}, which is an instance of {@code interfaceClass}
     */
    public static @Nullable <V> V nextParentOfType(final @NotNull Class<V> interfaceClass,
	                                               final Action<?> startParent) {
		Action<?> currentParent = startParent;
		while (true) {
			if (currentParent == null)
				return null;
			else {
				if (interfaceClass.isInstance(currentParent))
					return interfaceClass.cast(currentParent);
				else {
					currentParent = currentParent.getParent();
				}
			}
		}
	}

    /**
     * Returns a list containing all {@code Actions} being a parent of {@code startParent} and
     * an instance of {@code interfaceClass}.  
     * @param interfaceClass the class, {@code startParent} and its parents should be an instance of.
     * @param startParent all appearances in the parentchain, starting from {@code startParent}
     * being an instance of {@code interfaceClass} are searched for
     * @return  all parents in the parentchain of {@code startParent}, which are an instance of {@code interfaceClass}
     */
    public static <V extends Action<?>> List<V> getParentChain(
            final @NotNull Class<V> interfaceClass,
            final Action<?> startParent) {
        LinkedList<V> result = new LinkedList<V>();
        V currentParent = nextParentOfType(interfaceClass, startParent);
        while(currentParent != null) {
            result.addFirst(interfaceClass.cast(currentParent));
            currentParent = nextParentOfType(interfaceClass, currentParent.getParent());
        }
        return result;
    }


    public static void requireParameter(final @NotNull String parameterName, final Object object) {
        if (object == null)
            throw new IllegalStateException("Parameter '\'" + parameterName + "' not set");
    }


    @SuppressWarnings({ "StaticMethodOnlyUsedInOneClass" })
    public static void doNotOverwrite(final @NotNull String parameterName, final Object object) {
        if (object != null)
            throw new IllegalStateException("Overwriting the set parameter '\'" + parameterName + "' is not allowed");
    }
}
