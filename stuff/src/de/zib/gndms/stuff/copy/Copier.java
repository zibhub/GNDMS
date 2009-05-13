package de.zib.gndms.stuff.copy;

import de.zib.gndms.stuff.mold.Mold;
import de.zib.gndms.stuff.mold.Molding;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * This class provides several ways to copy an instance.
 *
 * Using {@link Copier#copy(boolean, Object)} an instance will be copied either as defined in
 *  {@link Copyable}'s annotation or according to its belonging to its superclasses.
 *
 * By now, an instance can be copied by
 *  <ul>
     *      <li>  molding
     *      </li>
     *      <li>  (de)serialization
     *      </li>
     *      <li>  cloning
     *      </li>
     *      <li>  using the class' constructor
     *      </li>
     * </ul> 
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

    /**
     * Returns a copy of an instance. Object will be copied either
     *
     * Selection of the copy mode is done using {@link Copier#selectMode(boolean, Object, Class)}}
     * 
     * @param fallbackToClone if true the instance will be copied using the {@code clone()} method,
     *        if it implements {@code Cloneable} but not {@code Molding}.
     * @param instance the instance to be copied
     * @return a copy of the instance
     */
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

    /**
     * Selects a proper copy mode for an instance.
     *
     * The copymode will be choosen by {@link Copyable}'s annotation.
     * If nothing denoted, the copy mode will be select by checking if {@code instance} belongs to {@code Molding, Clonable or Serializable}
     * in the given order. The first match will be taken.
     *
     * @param fallbackToClone if true the instance will be copied using the {@code clone()} method,
     * if it implements {@code Cloneable} but not {@code Molding}.
     * @param instance the instance to be copied
     * @param clazz the class the instance belongs to
     * @return the proper copy mode for an instance depeding on {@link Copyable}'s {@code annotations} or it's superclasses.
     */
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

    
    /**
     * Like {@code copyInstanceByCloning} but returns {@code null} if {@code instance} is {@code null}
     * 
     * @see de.zib.gndms.stuff.copy.Copier#copyInstanceByCloning(Cloneable)
     */
	public static <T extends Cloneable> T copyCloneable(final T instance) {
		if (instance == null)
			return null;
		else {
			return copyInstanceByCloning(instance);
		}
	}

    /**
     * Copies an instance by cloning
     * @param instance the Object to be copied
     * @return A copy of a Instance by using it's clone method
     */
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

    /**
     * Returns the clone method of a class
     *
     * @param clazzParam a Class implementing {@code Cloneable}
     * @return  the clone method of the class
     * @throws NoSuchMethodException if the class does not override  {@code Object}'s {@code clone} method
     */
	private static <T extends Cloneable> Method cloneMethod(final Class<T> clazzParam)
		  throws NoSuchMethodException {
        return clazzParam.getMethod("clone");
	}

    /**
     * Copies an object using {@code copyInstanceByMolding((Class<T>) instance.getClass(), instance)}, if {@code instance} is not {@code null}
     *
     * @param instance the instance to be copied
     * @return {@code copyInstanceByMolding((Class<T>) instance.getClass(), instance)} or {@code null} if {@code instance} is {@code null}
     */
	@SuppressWarnings({ "unchecked", "RawUseOfParameterizedType" })
	public static <T extends Molding> T copyMolding(final T instance) {
        if (instance == null)
			return null;
		else {
			return copyInstanceByMolding((Class<T>) instance.getClass(), instance);
		}
	}

    /**
     * Copies an instance by molding.
     *
     * @see Mold
     * @param clazz the class the instance belongs to
     * @param instance the instance to be copied
     * @return a copy of the instance, created by using molding
     */
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

    /**
     * Like {@code copyInstanceBySerialization} but returns {@code null} if {@code instance} is {@code null}
     *
     * @see de.zib.gndms.stuff.copy.Copier#copyInstanceBySerialization(java.io.Serializable) 
     */
	public static <T extends Serializable> T copySerializable(final T instance) {
		if (instance == null)
			return null;
		else
			return copyInstanceBySerialization(instance);
	}

     /**
     * Copies an instance by serialization. <br>
     * The Object will be deserialized and serialized again.
      *
     * @param instance the Object to be copied
     * @return A copy of a Instance by serialization
     */
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


   /**
     * Like {@code copyInstanceViaConstructor} but returns {@code null} if {@code instance} is {@code null}
     *
     * @see de.zib.gndms.stuff.copy.Copier#copyInstanceViaConstructor(Class, Object) 
     */

    @SuppressWarnings({ "unchecked" })
	public static <T> T copyViaConstructor(final T instance) {
		if (instance == null)
			return null;
		else {
			final @NotNull Class<T> instanceClass = (Class<T>) instance.getClass();
			return copyInstanceViaConstructor(instanceClass, instance);
		}
	}

     /**
     * Copies an instance by the Object's Constructor to create a new instance.<br>
      * If {@code oldObj} is a instance belonging to class {@code T} this method will invoke
      * {@code T newObj=new T(oldObj); }
      *
     * @param instance the Object to be copied
     * @return A copy of a Instance by serialization
     */
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
