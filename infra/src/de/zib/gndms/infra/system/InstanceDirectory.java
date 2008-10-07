package de.zib.gndms.infra.system;

import de.zib.gndms.kit.monitor.GroovyBindingFactory;
import de.zib.gndms.kit.monitor.GroovyMoniServer;
import de.zib.gndms.infra.service.GNDMServiceHome;
import de.zib.gndms.infra.service.GNDMSingletonServiceHome;
import de.zib.gndms.kit.factory.Factory;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.gorfx.OfferType;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.ResourceException;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;


/**
 * ThingAMagic.
*
* @author Stefan Plantikow<plantikow@zib.de>
* @version $Id$
*
*          User: stepn Date: 03.09.2008 Time: 16:50:06
*/
public class InstanceDirectory {
    private @NotNull final Log logger = LogFactory.getLog(InstanceDirectory.class);
    private static final int INITIAL_CAPACITY = 32;
    private static final long INSTANCE_RETRIEVAL_INTERVAL = 250L;

    private final @NotNull String systemName;

    private final @NotNull Map<String, Object> instances;
    private final @NotNull Map<Class<? extends GridResource>, GNDMServiceHome<?>> homes;

    @SuppressWarnings({ "RawUseOfParameterizedType" })
    private final @NotNull Map<String, Factory<? extends AbstractORQCalculator>>
            orqCalcMap = new HashMap<String, Factory<? extends AbstractORQCalculator>>(8);

    InstanceDirectory(final @NotNull String sysNameParam) {
        instances = new HashMap<String, Object>(INITIAL_CAPACITY);
        homes = new HashMap<Class<? extends GridResource>, GNDMServiceHome<?>>(INITIAL_CAPACITY);
        systemName = sysNameParam;
    }


    public synchronized void addHome(final @NotNull GNDMServiceHome<?> home)
            throws ResourceException {
        addHome(home.getModelClass(), home);
    }


    @SuppressWarnings(
            { "RawUseOfParameterizedType", "unchecked", "MethodWithTooExceptionsDeclared" })
    public <M extends AbstractORQ, O extends AbstractORQCalculator<M, O>> O getORQCalculator(
            final @NotNull GNDMSystem sys,
            final @NotNull EntityManagerFactory emf,
            final @NotNull String offerTypeKey)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException {
        EntityManager em = emf.createEntityManager();
        try {
            OfferType type = em.find(OfferType.class, offerTypeKey);
            Class<O> clazz = (Class<O>) Class.forName(type.getCalculatorClassName());
            if (! AbstractORQCalculator.class.isAssignableFrom(clazz))
                throw new IllegalArgumentException("Incompatible class type detected");

            synchronized (orqCalcMap) {
                Factory<O> instance = (Factory<O>) ((Factory) orqCalcMap.get(offerTypeKey));
                if (instance == null) {
                    instance = createORQCalculatorFactory(sys, type, clazz);
                    orqCalcMap.put(offerTypeKey, instance);
                }
                return clazz.cast(instance.getInstance());
            }
        }
        finally {
            if (! em.isOpen())
                em.close();
        }
    }


    @SuppressWarnings({ "MethodMayBeStatic", "unchecked", "MethodWithTooExceptionsDeclared" })
    private <M extends AbstractORQ, O extends AbstractORQCalculator<M, O>> Factory<O>
    createORQCalculatorFactory(
            final GNDMSystem sys, final OfferType typeParam, final Class<O> clazzParam)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException {
        final Class<Factory<O>> clazz =
                (Class<Factory<O>>)
                        Factory.class.asSubclass(Class.forName(typeParam.getFactoryClassName()));
        final Factory<O> oFactory = clazz.getConstructor(OfferType.class).newInstance(typeParam);
        if (oFactory instanceof SystemHolder)
            ((SystemHolder)oFactory).setSystem(sys);
        oFactory.setup();
        return oFactory;
    }


    public @NotNull  <T> T retrieveInstance(@NotNull Class<T> clazz, @NotNull String name) {
        T instance;
        try { instance = getInstance(clazz, name); }
        catch (IllegalStateException e) { instance = null; }
        while (instance != null) {
            try {
                Thread.sleep(INSTANCE_RETRIEVAL_INTERVAL);
            }
            catch (InterruptedException e) {
                // inteded
            }
            try { instance = getInstance(clazz, name); }
            catch (IllegalStateException e) { instance = null; }
        }
        return instance;
    }


    @SuppressWarnings({ "HardcodedFileSeparator", "RawUseOfParameterizedType" })
    private synchronized <K extends GridResource> void addHome(
            final @NotNull Class<K> modelClazz, final @NotNull GNDMServiceHome<?> home)
            throws ResourceException {
        if (homes.containsKey(modelClazz))
            throw new IllegalStateException("Name clash in home registration");
        else {
            final String homeName = home.getNickName() + "Home";
            addInstance_(homeName, home);
            try {
                if (home instanceof GNDMSingletonServiceHome) {
                    Object instance = home.find(null);
                    final String resourceName = home.getNickName() + "Resource";
                    addInstance_(resourceName, instance);
                    logger.debug(getSystemName() + " addSingletonResource: '"
                            + resourceName + "' = '" + modelClazz.getName() + '/'
                            + ((GNDMSingletonServiceHome)home).getSingletonID() + '\'');
                }
            }
            catch (RuntimeException e) {
                instances.remove(homeName);
                throw e;
            }
            catch (ResourceException e) {
                instances.remove(homeName);
                throw e;
            }
            homes.put(modelClazz, home);
        }

        logger.debug(getSystemName() + " addHome: '" + modelClazz.getName() + '\'');
    }

    @SuppressWarnings({ "unchecked" })
    public synchronized <M extends GridResource> GNDMServiceHome<M> getHome(Class<M> modelClazz) {
        final GNDMServiceHome<M> home = (GNDMServiceHome<M>) homes.get(modelClazz);
        if (home == null)
            throw new IllegalStateException("Unknown home");
        return home;
    }

    public synchronized void addInstance(@NotNull String name, @NotNull Object obj) {
        if (name.endsWith("Home") || name.endsWith("Resource") || name.endsWith("ORQC"))
            throw new IllegalArgumentException("Reserved instance name");

        addInstance_(name, obj);

        logger.debug(getSystemName() + " addInstance: '" + name + '\'');
    }


    private void addInstance_(final String name, final Object obj) {
        if ("out".equals(name) || "err".equals(name) || "args".equals(name) || "em".equals(name)
            || "emg".equals(name))
            throw new IllegalArgumentException("Reserved instance name");

        if (instances.containsKey(name))
            throw new IllegalStateException("Name clash in instance registration");
        else
            instances.put(name, obj);
    }


    public synchronized @NotNull <T> T getInstance(@NotNull Class<? extends T> clazz,
                                                   @NotNull String name)
    {
        final Object obj = instances.get(name);
        if (obj == null)
            throw new
                  IllegalStateException("Null instance retrieved or invalid or unregistered name");
        return clazz.cast(obj);
    }

    public synchronized GNDMServiceHome<?> lookupServiceHome(@NotNull String instancePrefix) {
        return getInstance(GNDMServiceHome.class, instancePrefix+"Home");
    }


    public GroovyBindingFactory createBindingFactory() {
        return new GNDMSBindingFactory();
    }



    public @NotNull String getSystemName() {
        return systemName;
    }


    private final class GNDMSBindingFactory implements GroovyBindingFactory {

        public @NotNull
        Binding createBinding(
              final @NotNull GroovyMoniServer moniServer,
              final @NotNull Principal principal, final @NotNull String args) {
            final Binding binding = new Binding();
            for (Map.Entry<String, Object> entry : instances.entrySet())
                binding.setProperty(entry.getKey(), entry.getValue());
            return binding;
        }

        @SuppressWarnings({"StringBufferWithoutInitialCapacity"})
        public void initShell(@NotNull GroovyShell shell, @NotNull Binding binding) {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, Object> entry : instances.entrySet()) {
                final String key = entry.getKey();
                builder.append("Object.metaClass.");
                builder.append(key);
                builder.append('=');
                builder.append(key);
                builder.append(';');
            }
            shell.evaluate(builder.toString());
        }


        public void destroyBinding(final @NotNull GroovyMoniServer moniServer,
                                   final @NotNull Binding binding) {
            // intended
        }

    }
}
