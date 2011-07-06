package de.zib.gndms.kit.access;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import de.zib.gndms.stuff.GNDMSInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Provides a GUICE GNDMSInjector to everybody
 *
 * @auhtor Stefan Plantikow <plantikow@zib.de>
 */
public final class GNDMSBinding {
    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(GNDMSBinding.class);

    public static final InjectorProvider PROVIDER = new GNDMSInjectorProvider();

    public static final InheritableThreadLocal<GNDMSInjector> THREAD_LOCAL = new InheritableThreadLocal<GNDMSInjector>() {
        @Override
        protected @NotNull GNDMSInjector initialValue() {
            final GNDMSInjector aInjectorInstance = DEFAULT.get();
            if (aInjectorInstance == null)
                throw new IllegalStateException("Default GNDMSInjector not set");
            return aInjectorInstance;
        }
    };

    @SuppressWarnings({"FieldHasSetterButNoGetter"})
    private static final AtomicReference<GNDMSInjector> DEFAULT = new AtomicReference<GNDMSInjector>();


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
    public static void setDefaultInjector(final @NotNull GNDMSInjector aInjector) {
        if (! DEFAULT.compareAndSet(null, aInjector))
            throw new IllegalStateException("Default Injector already set");
        LOGGER.info("Default Injector set as a fallback for GNDMSBinding");
    }

    private static class GNDMSInjectorProvider implements InjectorProvider {
        public @NotNull GNDMSInjector getInjector() {
            return THREAD_LOCAL.get();
        }
    }


    public static @NotNull GNDMSInjector getInjector() { return PROVIDER.getInjector(); }
}
