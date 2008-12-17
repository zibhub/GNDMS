package de.zib.gndms.stuff.copy;

import de.zib.gndms.stuff.mold.Mold;
import de.zib.gndms.stuff.mold.Molding;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 27.11.2008 Time: 16:47:00
 */
@SuppressWarnings({ "StaticMethodOnlyUsedInOneClass" })
public final class Copier {

	private Copier() {
		super();
	}

                                                                                                                    
	@SuppressWarnings({ "unchecked", "RawUseOfParameterizedType" })
	public static <T> T copy(final boolean fallbackToClone, final T instance) {
		if (instance == null)
			return null;
		else {
			final @NotNull Class<T> clazz = (Class<T>) instance.getClass();
			final CopyMode mode = selectMode(fallbackToClone, instance, clazz);
            if (mode == null)
                throw new IllegalArgumentException("Don't know how to copy instances of class: " + clazz.getName());
            switch (mode) {
				case MOLD:
					return copyInstanceByMolding(clazz, instance);
				case SERIALIZE:
					return (T) copyInstanceBySerialization((Serializable) instance);
				case CLONE:
					return (T) copyInstanceByCloning((Cloneable) instance);
				case CONSTRUCT:
					return copyInstanceViaConstructor(clazz, instance);
				case DONT:
					throw new IllegalArgumentException("Copying forbidden by Annotation");
				default:
					throw new IllegalArgumentException(
						  "Don't know how to copy given instance of class: " + clazz.getName());
			}
		}
	}


	@SuppressWarnings({ "unchecked" })
	private static <T> CopyMode selectMode(
		  final boolean fallbackToClone, final T instance, final Class<T> clazz) {
		final Copyable via = clazz.getAnnotation( Copyable.class);
		final CopyMode mode;
		if (via == null) {
			if (instance instanceof Molding)
				mode = CopyMode.MOLD;
			else if (fallbackToClone && instance instanceof Cloneable)
				mode = CopyMode.CLONE;
            else if (instance instanceof Serializable)
                mode = CopyMode.SERIALIZE;
			else
				mode = null;
		}
		else
			mode = via.value();
		return mode;
	}


	public static <T extends Cloneable> T copyCloneable(final T instance) {
		if (instance == null)
			return null;
		else {
			return copyInstanceByCloning(instance);
		}
	}


	@SuppressWarnings({ "unchecked" })
	private static <T extends Cloneable> T copyInstanceByCloning(final @NotNull T instance) {
		final @NotNull Class<T> clazz = (Class<T>) instance.getClass();
		try {
			return (T) cloneMethod(clazz).invoke(instance);
		}
		catch (IllegalAccessException e) {
			throw new IllegalArgumentException(
				  "Can't call clone() for given instance of class: " + clazz.getName());
		}
		catch (InvocationTargetException e) {
			throw new IllegalArgumentException(e);
		}
		catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(e);
		}
	}


	private static <T extends Cloneable> Method cloneMethod(final Class<T> clazzParam)
		  throws NoSuchMethodException {
		return clazzParam.getMethod("clone");
	}


	@SuppressWarnings({ "unchecked", "RawUseOfParameterizedType" })
	public static <T extends Molding> T copyMolding(final T instance) {
		if (instance == null)
			return null;
		else {
			return copyInstanceByMolding((Class<T>) instance.getClass(), instance);
		}
	}


	@SuppressWarnings({ "unchecked" })
	private static
	<T> T copyInstanceByMolding(final @NotNull Class<T> clazz, final @NotNull T instance) {
		try {
			final @NotNull T newInstance = clazz.newInstance();
			Mold.dynCopyMolding(clazz, instance, newInstance);
			return newInstance;
		}
		catch (InstantiationException e) {
			throw new IllegalArgumentException(e);
		}
		catch (IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		}
	}


	public static <T extends Serializable> T copySerializable(final T instance) {
		if (instance == null)
			return null;
		else
			return copyInstanceBySerialization(instance);
	}


	@SuppressWarnings({ "unchecked" })
	private static <T extends Serializable> T copyInstanceBySerialization(final @NotNull T instance)
	{
		try {
			final byte[] data;
		    final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		    try {
			    final ObjectOutputStream outStream = new ObjectOutputStream(byteOut);
			    try {
				     outStream.writeObject(instance);
				     outStream.flush();
				     byteOut.flush();
				     data = byteOut.toByteArray();
			    }
			    finally { outStream.close(); }
		    }
		    finally { byteOut.close(); }

		    final ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
		    try {
			     final ObjectInputStream inStream = new ObjectInputStream(byteIn);
			    try {
					return (T) inStream.readObject();
			    }
			    finally { inStream.close(); }
		    }
		    finally { byteIn.close(); }
	    }
	    catch (IOException e) {
		      throw new IllegalArgumentException(e);
	    }
	    catch (ClassNotFoundException cne) {
			throw new IllegalArgumentException(cne);
	    }
	}

	@SuppressWarnings({ "unchecked" })
	public static <T> T copyViaConstructor(final T instance) {
		if (instance == null)
			return null;
		else {
			final @NotNull Class<T> instanceClass = (Class<T>) instance.getClass();
			return copyInstanceViaConstructor(instanceClass, instance);
		}
	}


	private static <T> T copyInstanceViaConstructor(final @NotNull Class<T> clazz,
	                                                final @NotNull T instance)
	{
		try {
			return clazz.getConstructor(clazz).newInstance(instance);
		}
		catch (InstantiationException e) {
			throw new IllegalArgumentException(e);
		}
		catch (IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		}
		catch (InvocationTargetException e) {
			throw new IllegalArgumentException(e);
		}
		catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
