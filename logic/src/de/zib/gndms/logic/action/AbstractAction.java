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

        initialize( );
        try{
            return execute( );
        } finally{
            cleanUp( );
        }
    }


	public abstract R execute();


	public Action<?> getParent() {
		return parent;
	}


	public void setParent(final Action<?> parentParam) {
		if (parent != null)
			throw new IllegalStateException("Cant overwrite parent");

		parent = parentParam;
	}

	public @Nullable <V> V nextParentOfType(final @NotNull Class<V> interfaceClass) {
		return nextParentOfType(interfaceClass, getParent());
	}

	public static @Nullable <V> V nextParentOfType(final @NotNull Class<V> interfaceClass,
	                                               final Action<?> parentParam) {
		Action<?> parent = parentParam;
		while (true) {
			if (parent == null)
				return null;
			else {
				if (interfaceClass.isInstance(parent))
					return interfaceClass.cast(parent);
				else {
					parent = parent.getParent();
				}
			}
		}
	}
}
