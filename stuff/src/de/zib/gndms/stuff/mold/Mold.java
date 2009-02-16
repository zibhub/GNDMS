package de.zib.gndms.stuff.mold;


import org.jetbrains.annotations.NotNull;
import java.lang.reflect.InvocationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * This class provides methods to copy an instance which either has implemented {@link Molding} or at least has a method
 * {@code void mold(D molded)}.
 * See {@link Molder} for further details about the actual copy process and the difference to a 'normal' copy.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 01.12.2008 Time: 11:25:18
 */
@SuppressWarnings({ "unchecked", "StaticMethodOnlyUsedInOneClass", "RawUseOfParameterizedType" })
public final class Mold {

	private Mold() {}


    /**
     * Copies {@code molding} to {@code molded} using a {@code Molder}&lt;D&gt;.
     * This means only the <i>D-part</i> of {@code molding} will be copied.
     * The instance {@code molding} must either implement {@link Molding} or at least it must have a method
     * {@code mold(D molded)}.
     * See {@link Molder} for further details about the copy process.
     * @param moldingClass the class of the molder
     * @param molding the instance which will mold a new instance
     * @param molded the instance to be molded out of {@code molding}-instance
     */
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

    /**
     * Copies an Object implementing {@code Molding} to a new instance.
     * In this case it is the same like {@code dynCopyMolding(molding.getClass(),molding,molded)}
     *
     * @param molding the instance which will mold a new instance
     * @param molded the instance to be molded out of {@code molding}-instance
     */
	public static <C extends Molding, D> void copyMolding(final C molding, final D molded) {
		if (molding == null) return;
		if (molded == null)	return;
		final Class<D> aClass = (Class<D>) molded.getClass();
		molding.molder(aClass).mold(molded);
	}

    /**
     * Implements the {@code Molder}-Interface at runtime for {@code moldingParam} and returns a {@code Molder}&lt;D&gt;
     * out of {@code moldingParam}.
     * Therefore {@code moldingParam} must have a method {@code void mold(D obj)}, at least in a superclass.
     * @param moldingClassParam the Class of the instance supposed to mold a new instance
     * @param moldingParam the instance supposed to mold a new instance
     * @param moldedClassParam the Class of the instance to be molded
     * @return a {@code Molder}&lt;D&gt; out of {@code moldingParam}
     */
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

    /**
     * An InvocationHandler for classes having a {@code mold} method.
     *
     * @see Mold
     */
	private static final class MoldInvocationHandler<C, D> implements InvocationHandler {

        /**
         * the Class of the molding instance, which should be copied
         */
        private final @NotNull Class<C> moldingClass;

        /**
         * the molding instance, which should be copied
         */
        private final @NotNull C molding;

        /**
         * the mold method of the molding instance,signature: mold(Object o)
         */
        private final @NotNull Method moldMethod;

        /**
         * the Class of the molded instance, which will be a copy of the molding instance
         */
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

        /**
         * Tries to invoke {@code mold(molded)} on the Object {@code molding}.
         * @param proxy
         * @param method
         * @param args
         * @return
         * @throws Throwable  if {@code moldingClass} and all it's superclasses dont have a method {@code void mold(Object )}
         *
         */
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

        /**
         * Tries to invoke {@code mold(molded)} on the Object {@code molding}.
         * If {@code molding} does not have a method {@code mold(C molded)} where {@code C} is
         * {@code moldedClass} or one of its superclasses, an exception will be thrown.
         * 
         * @param molded the Object, the {@code mold} method should be invoked with
         * @throws Throwable if {@code moldingClass} and all it's superclasses dont have a method {@code void mold(Object )}
         */
        private void doMold(final @NotNull Object molded)
			  throws Throwable {
            Class curClass = moldedClass;
            while (! Object.class.equals(curClass)) {
                try {
                    moldingClass.getMethod("mold", curClass).invoke(molding,  curClass.cast(molded));
                    return;
                }
                catch (NoSuchMethodException nme) {
                    curClass = curClass.getSuperclass();
                    if (Object.class.equals(curClass))
                        throw nme;
                }
            }
        }
	}
}