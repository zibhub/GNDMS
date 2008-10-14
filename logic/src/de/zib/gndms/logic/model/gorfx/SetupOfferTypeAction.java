package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.kit.factory.Factory;
import de.zib.gndms.logic.action.MandatoryOptionMissingException;
import de.zib.gndms.logic.model.config.ConfigActionHelp;
import de.zib.gndms.logic.model.config.ConfigOption;
import de.zib.gndms.logic.model.config.SetupAction;
import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.gorfx.OfferType;
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
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.09.2008 Time: 17:06:54
 */
@ConfigActionHelp(shortHelp="Sets up supported OfferTypes", longHelp="Create, Update and Delete all OfferTypes supported by this GNDMS installation")
public class SetupOfferTypeAction extends SetupAction<Void> {
    @ConfigOption(descr="Unique URI identifying this offerType; must match entries in given arg and result xsd types")
    private String offerType;

    @ConfigOption(descr="QName of xsd type for ORQs of this OfferType")
    private ImmutableScopedName orqType;

    @ConfigOption(descr="QName of xsd type for results of this OfferType")
    private ImmutableScopedName resType;

    //@ConfigOption(altName = "class", descr="FQN of AbstractORQCalculator class for this OfferType")
    //private Class<? extends AbstractORQCalculator<?, ?>> calcClass;

    @ConfigOption(descr="FQN of AbstractORQCalculator factory class")
    private Class<Factory<OfferType, AbstractORQCalculator<?, ?>>> calcFactory;

    //@ConfigOption(altName = "class", descr="FQN of TaskAction class for this OfferType")
    //private Class<? extends TaskAction<?>> taskActionClass;

    @ConfigOption(descr="FQN of TaskAction factory class")
    private Class<Factory<OfferType, ORQTaskAction<?>>> taskActionFactory;

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


    @Override
    public Void execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
        switch(getMode()) {
            case CREATE:
                executeCreate(em);
                break;

            case UPDATE:
                executeUpdate(em);
                break;

            case DELETE:
                executeDelete(em);
                break;

            default:
                throw new IllegalStateException("Unreachable location");

        }

        return null;
    }


    private void executeDelete(final EntityManager em) {
        final OfferType type = em.find(OfferType.class, getOfferType());
        em.remove(type);
    }


    @SuppressWarnings({ "MethodWithMoreThanThreeNegations", "FeatureEnvy" })
    private void executeUpdate(final EntityManager em) {
        final @NotNull OfferType type = em.find(OfferType.class, getOfferType());
        if (calcFactory != null)
            type.setCalculatorFactoryClassName(calcFactory.getCanonicalName());
        if (taskActionFactory != null)
            type.setCalculatorFactoryClassName(taskActionFactory.getCanonicalName());
        if (orqType != null)
            type.setOfferArgumentType(orqType);
        if (resType != null)
            type.setOfferResultType(resType);
        pushConfigProps(type);        
    }


    @SuppressWarnings({ "FeatureEnvy" })
    private void executeCreate(final EntityManager em) {
        final OfferType type = new OfferType();
        type.setOfferTypeKey(getOfferType());
        type.setCalculatorFactoryClassName(getCalcFactory().getCanonicalName());
        type.setTaskActionFactoryClassName(getTaskActionFactory().getCanonicalName());
        type.setOfferArgumentType(orqType);
        type.setOfferResultType(resType);
        pushConfigProps(type);
        em.persist(type);
    }


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


    private void pushConfigProps(final OfferType typeParam) {
        Map<String, String> map = new HashMap<String, String>(configProps.size());
        configProps.putAll(map);
        typeParam.setConfigMap(map);
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


    public Class<Factory<OfferType, AbstractORQCalculator<?, ?>>> getCalcFactory() {
        return calcFactory;
    }


    public void setCalcFactory(
            final Class<Factory<OfferType, AbstractORQCalculator<?, ?>>> calcFactoryParam) {
        calcFactory = calcFactoryParam;
    }


    public Class<Factory<OfferType, ORQTaskAction<?>>> getTaskActionFactory() {
        return taskActionFactory;
    }


    public void setTaskActionFactory(
            final Class<Factory<OfferType, ORQTaskAction<?>>> taskActionFactoryParam) {
        taskActionFactory = taskActionFactoryParam;
    }
}
