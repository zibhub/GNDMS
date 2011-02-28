package de.zib.gndms.kit.config;

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



import de.zib.gndms.model.common.ImmutableScopedName;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import java.io.File;
import java.text.ParseException;
import java.util.Iterator;


/**
 * A configuration-object with methods
 * to convert the option values (being a String) to their appropriate types.
 *
 * It provides several getter methods for the option values.
 * <p>
 * In detail a class implementing this interface must define how to convert a String representing one of the following types:
 * <ul>
 *  <li> numercial value (Int,Long)</li>
 *  <li> Boolean </li>
 *  <li> File </li>
 *  <li> ISN (ImmutableScopedName)</li>
 * <li> Enum</li>
 * <li> ISO8601</li>
 * <li> class</li>
 * <li> dynamic array </li>
 * </ul>
 * to it's appropriate type
 * </p>
 *
 * 
 * @author  try ste fan pla nti kow zib
 * @see OptionProvider
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
     * Returns the value for the chosen option set in the current configuration.
     * If it has not been set, a default value will be returned
     *
     * @param name the name of the option
     * @param def the default-value, needed if no value has been set in the current configuration
     * @return either the value set in the current configuration or a default value if no value has been set 
     */
    int getIntOption(@NotNull String name, int def);


    

    /**
     * @see ConfigProvider#getIntOption(String)
     */
    long getLongOption(@NotNull String name) throws MandatoryOptionMissingException;

    /**
     * @see ConfigProvider#getIntOption(String, int) 
     */
    long getLongOption(@NotNull String name, long def);




    /**
     *  Returns an ImmutableScopedName-Object as set in the current configuration for the chosen option
     *
     * @param name the name of the option
     * @return an ImmutableScopedName-Object as set in the current configuration for the chosen option
     * @throws MandatoryOptionMissingException if the current configuration does not have the option {@code name}
     */
    @NotNull ImmutableScopedName getISNOption(@NotNull String name) throws MandatoryOptionMissingException;


    /**
     *  Returns an ImmutableScopedName-Object as set in the current configuration for the chosen option
     *
     * @param name the name of the option
     * @param def the default-value, needed if no value has been set in the current configuration
     * @return either the value set in the current configuration or a default value if no value has been set
     */
    @NotNull ImmutableScopedName getISNOption(@NotNull String name, @NotNull ImmutableScopedName def);


     /**
     *  Returns a new {@code File} with a name as set in the current configuration for the chosen option
      *
     * @param name the name of the option
     * @return the new {@code File} set in the current configuration
     * @throws MandatoryOptionMissingException if the current configuration does not have the option {@code name}
     */
    @NotNull File getFileOption(@NotNull String name) throws MandatoryOptionMissingException;

     /**
     * Returns the value for the chosen option set in the current configuration. If it has not been set, a default value will be returned
      *
     * @param name the name of the option
     * @param def the default-value, needed if no value has been set in the current configuration
     * @return either the value set in the current configuration or a default value if no value has been set
     */
    @NotNull File getFileOption(@NotNull String name, @NotNull File def);


    /**
     * @see ConfigProvider#getIntOption(String) 
     */
    boolean isBooleanOptionSet(@NotNull String name) throws MandatoryOptionMissingException;

    /**
     * @see ConfigProvider#getIntOption(String, int)
     */
    boolean isBooleanOptionSet(@NotNull String name, boolean def);


    /**
     *  Returns the Enum-constant set in the current configuration for the chosen option
     *
     * @param clazz the specific enum-class the constant belongs to
     * @param name the name of the option
     * @param toUpper if true, the name will converted to uppercase
     * @return the Enum-constant set in the current configuration for the chosen option
     * @throws MandatoryOptionMissingException if the current configuration does not have the option {@code name}
     */
    @NotNull <E extends Enum<E>> E getEnumOption(@NotNull Class<E> clazz,
                                        @NotNull String name, boolean toUpper)
                                                throws MandatoryOptionMissingException;

    /**
     *  Returns the Enum-constant set in the current configuration for the chosen option. If it has not been set, a default enum-constant will be returned
     *
     * @param clazz the specific enum-class the constant belongs to
     * @param name the name of the option
     * @param toUpper if true, the name will converted to uppercase
     * @param def default-value if the option could not be found
     * @return either the enum-constant set in the current configuration or a default value if no class has been set
     * @throws MandatoryOptionMissingException if the current configuration does not have the option {@code name}
     */
    @NotNull <E extends Enum<E>> E getEnumOption(
            @NotNull Class<E> clazz, @NotNull String name, boolean toUpper, @NotNull E def);


    /**
     *  Returns an DateTime-Object as set in the current configuration for the chosen option. If it has not been set, a default DateTime will be returned
     *
     * @param name the name of the option
     * @param def default-value if the option could not be found
     * @return either DateTime-Object set in the current configuration or a default value if no DateTime-Object has been set
     * @throws ParseException if the String-value set in current configuration is not in ISO8601-format
     */
    @SuppressWarnings({ "InstanceMethodNamingConvention" })
    @NotNull
    DateTime getISO8601Option(@NotNull String name, @NotNull DateTime def) throws ParseException;


    /**
     *  Returns an DateTime-Object as set in the current configuration for the chosen option
     *
     * @param name the name of the option
     * @return either the DateTime-Object as set in the current configuration
     * @throws ParseException if the String-value set in current configuration is not in ISO8601-format
     */
    @SuppressWarnings({ "InstanceMethodNamingConvention" })
    @NotNull DateTime getISO8601Option(@NotNull String name)
            throws MandatoryOptionMissingException, ParseException;

    /**
     * Returns the class set in the current configuration for the chosen option
     *
     * @param baseClass the class the returned class will be subclass of
     * @param name the name of the option
     * @return the class set in the current configuration for the chosen option
     * @throws MandatoryOptionMissingException if the current configuration does not have the option {@code name}
     * @throws ClassNotFoundException
     */
    <X> Class<? extends X> getClassOption(final @NotNull Class<X> baseClass, @NotNull String name)
            throws MandatoryOptionMissingException, ClassNotFoundException;

    /**
     * Returns the class set in the current configuration for the chosen option. If it has not been set, a default class will be returned
     *
     * @param baseClass  the class the returned class will be subclass of
     * @param name the name of the option
     * @param def default-class if the option could not be found
     * @return  either the class set in the current configuration or a default value if no class has been set
     * @throws ClassNotFoundException
     */
    <X> Class<? extends X> getClassOption(final @NotNull Class<X> baseClass, @NotNull String name,
                                          @NotNull Class<? extends X> def)
            throws ClassNotFoundException;

    /**
     * Returns an ConfigProvider containing the array as set in the current configuration for the chosen option
     *
     * @param name the name of the option
     * @return an ConfigProvider containing the array as set in the current configuration for the chosen option
     * @throws ParseException
     * @throws MandatoryOptionMissingException if the current configuration does not have the option {@code name}
     */
    @NotNull ConfigProvider getDynArrayOption(@NotNull String name)
		  throws ParseException, MandatoryOptionMissingException;


    /**
     * Returns the amount of entries of the array as created by {@link ConfigProvider#getDynArrayOption(String)}
     *
     * @return the amount of entries of the array as created by {@link ConfigProvider#getDynArrayOption(String)} 
     */
    int dynArraySize();

    /**
     * An iterator over the keys of the created array
     *
     * @return An iterator over the keys of the created array
     */
    @NotNull Iterator<String> dynArrayKeys();
}
