package de.zib.gndms.kit.access;

import com.google.inject.Injector;
import org.jetbrains.annotations.NotNull;

/**
 * Provides a guice injector
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 */
public interface InjectorProvider {
    @NotNull Injector getInjector();
}
