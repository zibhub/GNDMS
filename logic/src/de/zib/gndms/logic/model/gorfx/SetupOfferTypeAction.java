package de.zib.gndms.logic.model.gorfx;

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
    private String key;

    @ConfigOption(descr="Scope part of qname of xsd type for arguments of ORQs of this OfferType")
    private String argScope;

    @ConfigOption(descr="Local part of qname of xsd type for arguments of ORQs of this OfferType")
    private String argName;

    @ConfigOption(descr="Scope part of qname of xsd type for results of ORQs of this OfferType")
    private String resScope;

    @ConfigOption(descr="Local part of xsd type for results of ORQs of this OfferType")
    private String resName;

    @ConfigOption(altName = "class", descr="FQN of AbstractORQCalculator class that implements this OfferType")
    private Class<? extends AbstractORQCalculator<?, ?>> clazz;

    @ConfigOption(descr="The ORQCalculator Factory Class")
    private Class<?> factoryClass;

    @ConfigOption(descr = "File from which the initial config should be read")
    private String configFile;

    private Properties configProps;


    @SuppressWarnings({ "unchecked" })
    @Override
    public void initialize() {
        super.initialize();    // Overridden method
        try {
            if (key == null)
                setKey(getOption("key"));
            if (argScope == null && hasOption("argScope"))
                setArgScope(getOption("argScope"));
            if (argName == null && hasOption("argName"))
                setArgName(getOption("argName"));
            if (resScope == null && hasOption("resScope"))
                setResScope(getOption("resScope"));
            if (resName == null && hasOption("resName"))
                setResName(getOption("resName"));
            if (clazz == null && hasOption("class"))
                setClazz(
                        (Class<? extends AbstractORQCalculator<?, ?>>)
                                Class.forName(getOption("class"))
                                        .asSubclass(AbstractORQCalculator.class));
            if (factoryClass == null && hasOption("factoryClass"))
                setFactoryClass(Class.forName(getOption("factoryClass")));

            if (configProps == null & hasOption("configFile"))
                try {
                    loadConfigFromFile();
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }

            switch (getMode()) {
                case CREATE:
                    if (configProps == null)
                        configProps = new Properties();

                    requireParameter("argScope", argScope);
                    requireParameter("argName", argName);
                    requireParameter("resScope", resScope);
                    requireParameter("resName", resName);
                    requireParameter("factoryClass", factoryClass);
                    requireParameter("class", clazz);

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
        final OfferType type = em.find(OfferType.class, getKey());
        em.remove(type);
    }


    @SuppressWarnings({ "MethodWithMoreThanThreeNegations", "FeatureEnvy" })
    private void executeUpdate(final EntityManager em) {
        final OfferType type = em.find(OfferType.class, getKey());
        if (factoryClass != null)
            type.setFactoryClassName(factoryClass.getCanonicalName());
        if (clazz != null)
            type.setCalculatorClassName(clazz.getName());
        if (argScope != null)
            type.setOfferArgumentType(new ImmutableScopedName(argScope, type.getOfferArgumentType().getLocalName()));
        if (argName != null)
            type.setOfferArgumentType(new ImmutableScopedName(type.getOfferArgumentType().getNameScope(), argName));
        if (resScope != null)
            type.setOfferResultType(new ImmutableScopedName(resScope, type.getOfferResultType().getLocalName()));
        if (resName != null)
            type.setOfferResultType(new ImmutableScopedName(type.getOfferResultType().getNameScope(), resName));
        pushConfigProps(type);        
    }


    @SuppressWarnings({ "FeatureEnvy" })
    private void executeCreate(final EntityManager em) {
        final OfferType type = new OfferType();
        type.setFactoryClassName(getFactoryClass().getCanonicalName());
        type.setCalculatorClassName(clazz.getName());
        type.setOfferArgumentType(new ImmutableScopedName(argScope, argName));
        type.setOfferResultType(new ImmutableScopedName(resScope, resName));
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
        if (configProps != null) {
            Map<String, String> map = new HashMap<String, String>(configProps.size());
            configProps.putAll(map);
            typeParam.setConfigMap(map);
        }
    }


    public String getKey() {
        return key;
    }


    public void setKey(final String keyParam) {
        key = keyParam;
    }


    public String getArgScope() {
        return argScope;
    }


    public void setArgScope(final String argScopeParam) {
        argScope = argScopeParam;
    }


    public String getArgName() {
        return argName;
    }


    public void setArgName(final String argNameParam) {
        argName = argNameParam;
    }


    public String getResScope() {
        return resScope;
    }


    public void setResScope(final String resScopeParam) {
        resScope = resScopeParam;
    }


    public String getResName() {
        return resName;
    }


    public void setResName(final String resNameParam) {
        resName = resNameParam;
    }


    public Class<? extends AbstractORQCalculator<?, ?>> getClazz() {
        return clazz;
    }


    public void setClazz(final Class<? extends AbstractORQCalculator<?, ?>> clazzParam) {
        clazz = clazzParam;
    }


    public Class<?> getFactoryClass() {
        return factoryClass;
    }


    public void setFactoryClass(final Class<?> factoryClassParam) {
        factoryClass = factoryClassParam;
    }
}
