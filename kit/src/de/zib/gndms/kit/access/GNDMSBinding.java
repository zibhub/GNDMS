package de.zib.gndms.kit.access;

import com.google.inject.Injector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Provides a GUICE Injector to everybody
 *
 * @auhtor Stefan Plantikow <plantikow@zib.de>
 */
public final class GNDMSBinding {
    private static final @NotNull Log LOGGER = LogFactory.getLog(GNDMSBinding.class);

    public static final InjectorProvider PROVIDER = new GNDMSInjectorProvider();

    public static final InheritableThreadLocal<Injector> THREAD_LOCAL = new InheritableThreadLocal<Injector>() {
        @Override
        protected @NotNull Injector initialValue() {
            final Injector aInjectorInstance = DEFAULT.get();
            if (aInjectorInstance == null)
                throw new IllegalStateException("Default Injector not set");
            return aInjectorInstance;
        }
    };

    @SuppressWarnings({"FieldHasSetterButNoGetter"})
    private static final AtomicReference<Injector> DEFAULT = new AtomicReference<Injector>();


    private GNDMSBinding() {
    }

    /**
     * Set's the default (fallback) Injector instance
     *
     * May only be called once, this is done during initialize by GNDMSystem
     *
     * @param aInjector
     */
    @SuppressWarnings({"StaticMethodOnlyUsedInOneClass"})
    public static void setDefaultInjector(final @NotNull Injector aInjector) {
        if (! DEFAULT.compareAndSet(null, aInjector))
            throw new IllegalStateException("Default Injector already set");
        LOGGER.info("Default Injector set as a fallback for GNDMSBinding");
    }

    private static class GNDMSInjectorProvider implements InjectorProvider {
        public @NotNull Injector getInjector() {
            return THREAD_LOCAL.get();
        }
    }


    public static @NotNull Injector getInjector() { return PROVIDER.getInjector(); }
}
