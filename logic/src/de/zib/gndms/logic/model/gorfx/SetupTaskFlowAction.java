package de.zib.gndms.logic.model.gorfx;

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



import de.zib.gndms.model.common.types.factory.KeyFactory;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.logic.model.config.ConfigActionHelp;
import de.zib.gndms.logic.model.config.ConfigActionResult;
import de.zib.gndms.logic.model.config.ConfigOption;
import de.zib.gndms.logic.model.config.SetupAction;
import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.TaskFlowType;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * <p>An Action to create new {@link de.zib.gndms.neomodel.gorfx.TaskFlowType} instances and store them in the database.
 *
 * <p>Depending on the chosen <tt>SetupMode</tt>, it will either create or update an {@code OfferType} entity by using the getter methods
 * provided in this class.
 * On both modes, the entity is completly overwritten and stored in the database. If only the entity's configmap should be updated,
 * use {@link de.zib.gndms.logic.model.gorfx.ConfigOfferTypeAction} instead, after the entity has been created once.
 *
 * <p>The following parameters are required for a new entity creation:
 * {@link #orqType 'orqType'},{@link #resType 'resType'}. {@link #calcFactory 'calcFactory'},{@link #taskActionFactory 'taskAction'}.
 * They must be set in the configuration map before this action is started on <tt>create</tt> modus.
 * Otherwise an <tt>IllegalStateException</tt> will be thrown.
 * <tt>orqType</tt> must be set in every mode.
 *
 * <p>An instance of this class returns a {@code ConfigActionResult} informing about the success of its execution, when
 * the <tt>execute()</tt> method is called.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 15.09.2008 Time: 17:06:54
 */
@ConfigActionHelp(shortHelp="Sets up supported OfferTypes", longHelp="Create, Update and Delete all OfferTypes supported by this GNDMS installation")
public class SetupTaskFlowAction extends SetupAction<ConfigActionResult> {
    @ConfigOption(descr="Unique URI identifying this offerType; must match entries in given arg and result xsd types")
    private String offerType;

    @ConfigOption(descr="QName of xsd type for ORQs of this TaskFlowType")
    private ImmutableScopedName orqType;

    @ConfigOption(descr="QName of xsd type for results of this TaskFlowType")
    private ImmutableScopedName resType;

    //@ConfigOption(altName = "class", descr="FQN of QuoteCalculator class for this TaskFlowType")
    //private Class<? extends QuoteCalculator<?, ?>> calcClass;

    @ConfigOption(descr="FQN of QuoteCalculator factory class")
    private Class<KeyFactory<String, AbstractQuoteCalculator<?>>> calcFactory;

    //@ConfigOption(altName = "class", descr="FQN of TaskAction class for this TaskFlowType")
    //private Class<? extends TaskAction<?>> taskActionClass;

    @ConfigOption(descr="FQN of TaskAction factory class")
    private Class<KeyFactory<String, TaskFlowAction<?>>> taskActionFactory;

    @ConfigOption(descr = "File from which the initial config should be read; UPDATE will overwrite!")
    private String configFile;

    private Properties configProps;


    @SuppressWarnings({ "unchecked", "RawUseOfParameterizedType" })
    @Override
    public void initialize() {
        super.initialize();    // Overridden method
        try {
            initOptions();
            initConfigProps();

            switch (getMode()) {
                case CREATE:
                    requireParameter("orqType", orqType);
                    requireParameter("resType", resType);
                    requireParameter("calcFactory", calcFactory);
                    requireParameter("taskAction", taskActionFactory);
                default:
            }
        }
        catch (MandatoryOptionMissingException e) {
            throw new IllegalArgumentException(e);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }


    /**
     * Tries to initialize all TaskFlowType fields from the configuration map.
     * 
     * @throws MandatoryOptionMissingException if the configuration has not all need options set, needed for an TaskFlowType
     * @throws ClassNotFoundException if calcFactory or taskActionFactory class could not be found
     */
    @SuppressWarnings({ "unchecked", "RawUseOfParameterizedType" })
    private void initOptions() throws MandatoryOptionMissingException, ClassNotFoundException {
        if (offerType == null)
            setOfferType(getOption("offerType"));
        if (orqType == null && hasOption("orqType"))
            setOrqType(getISNOption("orqType"));
        if (resType == null && hasOption("resType"))
            setResType(getISNOption("resType"));
        if (calcFactory == null && hasOption("calcFactory"))
            setCalcFactory((Class)Class.forName(getOption("calcFactory")));
        if (taskActionFactory == null && hasOption("taskActionFactory"))
            setTaskActionFactory((Class)Class.forName(getOption("taskActionFactory")));
    }

    /**
     * Initializes the config properties.
     * If the configuration map has an option <tt>configFile</tt>, the properities will be loaded from the file <tt>configFile</tt>,
     * using {@link #loadConfigFromFile()}. 
     */
    private void initConfigProps() {
        if (configProps == null & hasOption("configFile"))
            try {
                loadConfigFromFile();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

        if (configProps == null)
            configProps = new Properties();
    }

    
    /**
     * Creates, updates or deletes the entity with the primary key {@code getOfferType()} from the entityclass {@code OfferType.class}
     * @param em the EntityManager, managing the TaskFlowType instance.
     * @param writer
     * @return An {@code OKResult} instance, if no problem occurred. Otherwise a {@code FailedResult} instance.
     */
    @Override
    public ConfigActionResult execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
        final Session session = getDao().beginSession();

        try {
            switch(getMode()) {
                case CREATE:
                    executeCreate(session);
                    session.success();
                    break;

                case UPDATE:
                    executeUpdate(session);
                    session.success();
                    break;

                case DELETE:
                    executeDelete(session);
                    session.success();
                    break;

                default:
                    session.failure();
                    throw new IllegalStateException("Unreachable location");

            }

        }
        finally { session.finish(); }
        return ok();
    }

    /**
     * Removes the entity with the primary key {@code getOfferType()} and the entityclass {@code OfferType.class} from the EntityManager.
     *
     * @param session the EnityManager, containing an entity instance for the entityClass {@code OfferType} and
     *      the primary key {@code getOfferType()}
     */
    private void executeDelete(final Session session) {
        session.findOfferType(getOfferType()).delete();
    }


    /**
     * Retrieves the entity instance with the primary key {@code getOfferType()} from the entityclass {@code OfferType.class}
     * and sets its fields using the getter method of this class.
     *
     * @param session the EnityManager, containing an entity instance for the entityClass {@code OfferType} and
     *      the primary key {@code getOfferType()}
     */
    @SuppressWarnings({ "MethodWithMoreThanThreeNegations", "FeatureEnvy" })
    private void executeUpdate(final Session session) {
        final @NotNull TaskFlowType type = session.findOfferType(getOfferType());
        if (calcFactory != null)
            type.setCalculatorFactoryClassName(calcFactory.getCanonicalName());
        if (taskActionFactory != null)
            type.setTaskActionFactoryClassName(taskActionFactory.getCanonicalName());
        if (orqType != null)
            type.setOfferArgumentType(orqType);
        if (resType != null)
            type.setOfferResultType(resType);
        pushConfigProps(type);        
    }


    /**
     * Creates a new {@code OfferType} ,sets its fields using the getter method of this
     * class and makes it managed and persistent by the EntityManager.
     *
     * @param session the EntityManager,
     */
    @SuppressWarnings({ "FeatureEnvy" })
    private void executeCreate(final Session session) {
        final TaskFlowType type = session.createOfferType();
        type.setOfferTypeKey(getOfferType());
        type.setCalculatorFactoryClassName(getCalcFactory().getCanonicalName());
        type.setTaskActionFactoryClassName(getTaskActionFactory().getCanonicalName());
        type.setOfferArgumentType(orqType);
        type.setOfferResultType(resType);
        pushConfigProps(type);
    }


    /**
     * Loads <tt> configFile </tt> as properties into <tt>configProps</tt>.
     * 
     * @throws IOException if the file could not be loaded
     */
    private void loadConfigFromFile() throws IOException {
        FileInputStream in = new FileInputStream(new File(configFile));
        try {
            configProps = new Properties();
            configProps.load(in);
        }
        catch (IOException e) {
            configProps = null;
            throw new RuntimeException(e);
        }
        finally {
            in.close();
        }
    }

    /**
     * Loads the current properties (<tt>configProps</tt>) to a map and and sets it as {@code typeParam}'s config map.
     *
     * @param typeParam an TaskFlowType, which should use the current properties as its config map
     */
    private void pushConfigProps(final TaskFlowType typeParam) {
        Map<String, String> map = new HashMap<String, String>(configProps.size());
        configProps.putAll(map);
        typeParam.setConfigMapData(map);
    }


    public String getOfferType() {
        return offerType;
    }


    public void setOfferType(final String offerTypeParam) {
        offerType = offerTypeParam;
    }


    public ImmutableScopedName getOrqType() {
        return orqType;
    }


    public void setOrqType(final ImmutableScopedName orqTypeParam) {
        orqType = orqTypeParam;
    }


    public ImmutableScopedName getResType() {
        return resType;
    }


    public void setResType(final ImmutableScopedName resTypeParam) {
        resType = resTypeParam;
    }


    public Class<KeyFactory<String, AbstractQuoteCalculator<?>>> getCalcFactory() {
        return calcFactory;
    }


    public void setCalcFactory(
            final Class<KeyFactory<String, AbstractQuoteCalculator<?>>> calcFactoryParam) {
        calcFactory = calcFactoryParam;
    }


    public Class<KeyFactory<String, TaskFlowAction<?>>> getTaskActionFactory() {
        return taskActionFactory;
    }


    public void setTaskActionFactory(
            final Class<KeyFactory<String, TaskFlowAction<?>>> taskActionFactoryParam) {
        taskActionFactory = taskActionFactoryParam;
    }
}
