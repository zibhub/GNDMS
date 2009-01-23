package de.zib.gndms.kit.config;

import de.zib.gndms.model.common.ImmutableScopedName;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import java.io.File;
import java.text.ParseException;
import java.util.Iterator;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.10.2008 Time: 11:17:49
 */
public interface ConfigProvider extends OptionProvider {

    /**
     *  Returns the value set in the current configuration for the chosen option
     * @param name the name of the option
     * @return the value set in the current configuration
     * @throws MandatoryOptionMissingException if the current configuration does not have the option {@code name}
     */
    int getIntOption(@NotNull String name) throws MandatoryOptionMissingException;

    /**
     * Returns the value for the chosen option set in the current configuration. If it has not been set, a default value will be returned
     * @param name the name of the option
     * @param def the default-value, needed if no value has been set in the current configuration
     * @return either the value set in the current configuration or a default value if no value has been set 
     */
    int getIntOption(@NotNull String name, int def);

    /**
     *  Returns the value set in the current configuration for the chosen option
     * @param name the name of the option
     * @return the value set in the current configuration
     * @throws MandatoryOptionMissingException if the current configuration does not have the option {@code name}
     */
    long getLongOption(@NotNull String name) throws MandatoryOptionMissingException;

     /**
     * Returns the value for the chosen option set in the current configuration. If it has not been set, a default value will be returned
     * @param name the name of the option
     * @param def the default-value, needed if no value has been set in the current configuration
     * @return either the value set in the current configuration or a default value if no value has been set
     */
    long getLongOption(@NotNull String name, long def);



    @NotNull ImmutableScopedName getISNOption(@NotNull String name) throws MandatoryOptionMissingException;

    @NotNull ImmutableScopedName getISNOption(@NotNull String name, @NotNull ImmutableScopedName def);


     /**
     *  Returns a new {@code File} with a name as set in the current configuration for the chosen option
     * @param name the name of the option
     * @return the new {@code File} set in the current configuration
     * @throws MandatoryOptionMissingException if the current configuration does not have the option {@code name}
     */
    @NotNull File getFileOption(@NotNull String name) throws MandatoryOptionMissingException;

     /**
     * Returns the value for the chosen option set in the current configuration. If it has not been set, a default value will be returned
     * @param name the name of the option
     * @param def the default-value, needed if no value has been set in the current configuration
     * @return either the value set in the current configuration or a default value if no value has been set
     */
    @NotNull File getFileOption(@NotNull String name, @NotNull File def);


     /**
     *  Returns the value set in the current configuration for the chosen option
     * @param name the name of the option
     * @return the value set in the current configuration
     * @throws MandatoryOptionMissingException if the current configuration does not have the option {@code name}
     */
    boolean isBooleanOptionSet(@NotNull String name) throws MandatoryOptionMissingException;

     /**
     * Returns the value for the chosen option set in the current configuration. If it has not been set, a default value will be returned
     * @param name the name of the option
     * @param def the default-value, needed if no value has been set in the current configuration
     * @return either the value set in the current configuration or a default value if no value has been set
     */
    boolean isBooleanOptionSet(@NotNull String name, boolean def);

   
    @NotNull <E extends Enum<E>> E getEnumOption(@NotNull Class<E> clazz,
                                        @NotNull String name, boolean toUpper)
                                                throws MandatoryOptionMissingException;

    @NotNull <E extends Enum<E>> E getEnumOption(
            @NotNull Class<E> clazz, @NotNull String name, boolean toUpper, @NotNull E def);

    
    @SuppressWarnings({ "InstanceMethodNamingConvention" })
    @NotNull
    DateTime getISO8601Option(@NotNull String name, @NotNull DateTime def) throws ParseException;

    @SuppressWarnings({ "InstanceMethodNamingConvention" })
    @NotNull DateTime getISO8601Option(@NotNull String name)
            throws MandatoryOptionMissingException, ParseException;

    <X> Class<? extends X> getClassOption(final @NotNull Class<X> baseClass, @NotNull String name)
            throws MandatoryOptionMissingException, ClassNotFoundException;

    <X> Class<? extends X> getClassOption(final @NotNull Class<X> baseClass, @NotNull String name,
                                          @NotNull Class<? extends X> def)
            throws ClassNotFoundException;


    /**
     * Returns a ConfigProvider as set in the current configuration for the chosen option
     * @param name the name of the option
     * @return the ConfigProvider , as set in the current configuration
     * @throws ParseException
     * @throws MandatoryOptionMissingException if the current configuration does not have the option {@code name}
     */
    @NotNull ConfigProvider getDynArrayOption(@NotNull String name)
		  throws ParseException, MandatoryOptionMissingException;

	int dynArraySize();
	
	@NotNull Iterator<String> dynArrayKeys();
}
