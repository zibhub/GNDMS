package de.zib.gndms.logic.action;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * 
 * User: mjorra, Date: 12.08.2008, Time: 15:33:10
 */
@SuppressWarnings({ "NoopMethodInAbstractClass" })
public abstract class AbstractAction<R> implements Action<R> {
	private Action<?> parent;

    public void initialize() { }

    public void cleanUp() { }


    public R call ( ) throws RuntimeException {
        try {
	        initialize( );
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
}
