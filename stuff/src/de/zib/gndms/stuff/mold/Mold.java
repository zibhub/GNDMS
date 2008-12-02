package de.zib.gndms.stuff.mold;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationTargetException;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 01.12.2008 Time: 11:25:18
 */
@SuppressWarnings({ "unchecked", "StaticMethodOnlyUsedInOneClass", "RawUseOfParameterizedType" })
public final class Mold {

	private Mold() {}


	public static <C, D> void dynCopyMolding(final Class<C> moldingClass, final C molding, final D molded) {
		if (molding == null) return;
		if (molded == null)	return;
		final @NotNull Class<D> moldedClass = (Class<D>) molded.getClass();
		final @NotNull Molder<D> molder;
		if (molding instanceof Molding)
			molder = ((Molding) molding).molder(moldedClass);
		else
			molder = newMolderProxy( moldingClass, molding, moldedClass );
		molder.mold(molded);
	}
	public static <C extends Molding, D> void copyMolding(final C molding, final D molded) {
		if (molding == null) return;
		if (molded == null)	return;
		final Class<D> aClass = (Class<D>) molded.getClass();
		molding.molder(aClass).mold(molded);
	}


	public static <C, D> Molder<D> newMolderProxy(
		  @NotNull Class<C> moldingClassParam, @NotNull C moldingParam,
		  @NotNull Class<D> moldedClassParam) {
		final @NotNull Method method;
		try {
			method = Molder.class.getMethod("mold", Object.class);
		}
		catch (NoSuchMethodException e) {
			// Shouldnt happen
			throw new RuntimeException("This should not happen", e);
		}
		final @NotNull InvocationHandler handler =
			  new MoldInvocationHandler<C, D>(moldingClassParam, moldingParam,
											   method,
											   moldedClassParam);
		return (Molder<D>) Proxy.newProxyInstance(Molder.class.getClassLoader(),
												  new Class[] { Molder.class }, handler);
	}


	private static final class MoldInvocationHandler<C, D> implements InvocationHandler {
		private final @NotNull Class<C> moldingClass;
		private final @NotNull C molding;
		private final @NotNull Method moldMethod;
		private final @NotNull Class<D> moldedClass;


		private MoldInvocationHandler(
			  @NotNull final Class<C> moldingClassParam,
			  @NotNull final C moldingParam, @NotNull final Method moldMethodParam,
			  @NotNull final Class<D> moldedClassParam
		) {
			moldingClass = moldingClassParam;
			moldedClass = moldedClassParam;
			molding = moldingParam;
			moldMethod = moldMethodParam;
		}


		@SuppressWarnings({ "unchecked" })
		public Object invoke(final Object proxy, final Method method, final Object[] args)
			  throws Throwable {
			if (moldMethod.equals(method) && args[0] != null)
					doMold(args[0]);
			else
				if (Object.class.equals(method.getDeclaringClass()))
					method.invoke(molding, args);
				else
					throw new IllegalArgumentException("Unknown method: " + method);
			return null;
		}

		private void doMold(final @NotNull Object molded)
			  throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
			moldingClass.getMethod("mold", moldedClass).invoke(molding,  moldedClass.cast(molded));
		}
	}
}