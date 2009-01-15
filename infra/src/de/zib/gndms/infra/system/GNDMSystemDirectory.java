package de.zib.gndms.infra.system;

import com.google.common.collect.Maps;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import de.zib.gndms.infra.access.ServiceHomeProvider;
import de.zib.gndms.infra.service.GNDMPersistentServiceHome;
import de.zib.gndms.infra.service.GNDMServiceHome;
import de.zib.gndms.infra.service.GNDMSingletonServiceHome;
import de.zib.gndms.kit.monitor.GroovyBindingFactory;
import de.zib.gndms.kit.monitor.GroovyMoniServer;
import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.logic.access.TaskActionProvider;
import de.zib.gndms.logic.model.gorfx.*;
import de.zib.gndms.kit.access.InstanceProvider;
import de.zib.gndms.model.common.ConfigletState;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.common.ModelUUIDGen;
import de.zib.gndms.model.common.types.factory.IndustrialPark;
import de.zib.gndms.model.common.types.factory.KeyFactory;
import de.zib.gndms.model.common.types.factory.KeyFactoryInstance;
import de.zib.gndms.model.common.types.factory.RecursiveKeyFactory;
import de.zib.gndms.model.gorfx.OfferType;
import de.zib.gndms.stuff.BoundInjector;
import de.zib.gndms.stuff.configlet.ConfigletProvider;
import de.zib.gndms.stuff.configlet.Configlet;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.ResourceException;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * ThingAMagic.
*
* @author Stefan Plantikow<plantikow@zib.de>
* @version $Id$
*
*          User: stepn Date: 03.09.2008 Time: 16:50:06
*/
public class GNDMSystemDirectory implements SystemDirectory, Module {

    private @NotNull final Log logger = LogFactory.getLog(GNDMSystemDirectory.class);
    private static final int INITIAL_CAPACITY = 32;
    private static final long INSTANCE_RETRIEVAL_INTERVAL = 250L;

    private final @NotNull String systemName;

    private final @NotNull Map<String, Object> instances;
    private final @NotNull Map<Class<? extends GridResource>, GNDMPersistentServiceHome<?>> homes;

	private final Map<String, Configlet> configlets = Maps.newConcurrentHashMap();

    @SuppressWarnings({ "RawUseOfParameterizedType" })
    private final @NotNull IndustrialPark<OfferType, String, AbstractORQCalculator<?, ?>> orqPark;

    @SuppressWarnings({ "RawUseOfParameterizedType" })
    private final @NotNull IndustrialPark<OfferType, String, ORQTaskAction<?>> taskActionPark;

	@SuppressWarnings({ "FieldCanBeLocal" })
	private final Wrapper<Object> sysHolderWrapper;

	private final @NotNull ModelUUIDGen uuidGen;


	private final @NotNull BoundInjector boundInjector = new BoundInjector();



	@SuppressWarnings({ "ThisEscapedInObjectConstruction" })
	GNDMSystemDirectory(
	      final @NotNull String sysNameParam,
	      final @NotNull ModelUUIDGen uuidGenParam,
	      final Wrapper<Object> systemHolderWrapParam,
	      final @NotNull Module sysModule) {
        instances = new HashMap<String, Object>(INITIAL_CAPACITY);
        homes = new HashMap<Class<? extends GridResource>, GNDMPersistentServiceHome<?>>(INITIAL_CAPACITY);
        systemName = sysNameParam;
		uuidGen = uuidGenParam;
	    sysHolderWrapper = systemHolderWrapParam;
		final Injector injector = Guice.createInjector(sysModule, this);
		boundInjector.setInjector(injector);

	    final ORQCalculatorMetaFactory calcMF = new ORQCalculatorMetaFactory();
		calcMF.setInjector(injector);
	    calcMF.setWrap(sysHolderWrapper);
	    orqPark = new OfferTypeIndustrialPark<AbstractORQCalculator<?,?>>(calcMF);

	    final ORQTaskActionMetaFactory taskMF = new ORQTaskActionMetaFactory();
	    taskMF.setWrap(sysHolderWrapper);
		taskMF.setInjector(injector);
	    taskActionPark = new OfferTypeIndustrialPark<ORQTaskAction<?>>(taskMF);
    }


	public synchronized void addHome(final @NotNull GNDMServiceHome home)
            throws ResourceException {
        if (home instanceof GNDMPersistentServiceHome<?>)
            addHome(((GNDMPersistentServiceHome<?>) home).getModelClass(), home);
        else
            addHome(null, home);
    }


    @SuppressWarnings({ "MethodWithTooExceptionsDeclared" })
    @NotNull
    public AbstractORQCalculator<?,?> newORQCalculator(
        final @NotNull EntityManagerFactory emf,
        final @NotNull String offerTypeKey)
        throws ClassNotFoundException, IllegalAccessException, InstantiationException,
        NoSuchMethodException, InvocationTargetException {
        EntityManager em = emf.createEntityManager();
        try {
            OfferType type = em.find(OfferType.class, offerTypeKey);
            if (type == null)
                throw new IllegalArgumentException("Unknow offer type: " + offerTypeKey);
            AbstractORQCalculator<?,?> orqc = orqPark.getInstance(type);
            orqc.setConfigletProvider( this );
            return orqc;
        }
        finally {
            if (! em.isOpen())
                em.close();
        }
    }


    @SuppressWarnings(
	      { "MethodWithTooExceptionsDeclared", "OverloadedMethodsWithSameNumberOfParameters" })
    public TaskAction newTaskAction(
            final @NotNull EntityManagerFactory emf,
            final @NotNull String offerTypeKey)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException {
        EntityManager em = emf.createEntityManager();
        try {
	        return newTaskAction(em, offerTypeKey);
        }
        finally {
            if (! em.isOpen())
                em.close();
        }
    }


	@SuppressWarnings({ "OverloadedMethodsWithSameNumberOfParameters" })
	public TaskAction newTaskAction(
		  final EntityManager emParam, final String offerTypeKey)
		  throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		OfferType type = emParam.find(OfferType.class, offerTypeKey);
		TaskAction ta = taskActionPark.getInstance(type);
		ta.setUUIDGen( uuidGen );
		return ta;
	}


	public @NotNull  <T> T waitForInstance(@NotNull Class<T> clazz, @NotNull String name) {
        T instance;
        try { instance = getInstance(clazz, name); }
        catch (IllegalStateException e) { instance = null; }
        while (instance != null) {
            try {
                Thread.sleep(INSTANCE_RETRIEVAL_INTERVAL);
            }
            catch (InterruptedException e) {
                // intended
            }
            try { instance = getInstance(clazz, name); }
            catch (IllegalStateException e) { instance = null; }
        }
        return instance;
    }


    @SuppressWarnings({ "HardcodedFileSeparator", "RawUseOfParameterizedType" })
    public synchronized <K extends GridResource> void addHome(
            final Class<K> modelClazz, final @NotNull GNDMServiceHome home)
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
                            + resourceName + "' = '" + (modelClazz == null ? "(no model class)" : modelClazz.getName()) + '/'
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
            if (modelClazz != null)
                homes.put(modelClazz, (GNDMPersistentServiceHome<?>) home);
        }

        logger.debug(getSystemName() + " addHome: '" + home + '\'');
    }

    @SuppressWarnings({ "unchecked" })
    public synchronized <M extends GridResource> GNDMPersistentServiceHome<M>
    getHome(Class<M> modelClazz) {
        final GNDMPersistentServiceHome<M> home =
                (GNDMPersistentServiceHome<M>) homes.get(modelClazz);
        if (home == null)
            throw new IllegalStateException("Unknown home");
        return home;
    }

    public synchronized void addInstance(@NotNull String name, @NotNull Object obj) {
        if (name.endsWith("Home") || name.endsWith("Resource") || name.endsWith("ORQC"))
            throw new IllegalArgumentException("Reserved instance name");

        addInstance_(name, obj);

    }


    private void addInstance_(final String name, final Object obj) {
        if ("out".equals(name) || "err".equals(name) || "args".equals(name) || "em".equals(name)
            || "emg".equals(name))
            throw new IllegalArgumentException("Reserved instance name");

        if (instances.containsKey(name))
            throw new IllegalStateException("Name clash in instance registration: " + name);
        else
            instances.put(name, obj);

        logger.debug(getSystemName() + " addInstance: '" + name + '\'');        
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


	public void reloadConfiglets(final EntityManagerFactory emf) {
		ConfigletState[] states;
		EntityManager em = emf.createEntityManager();
		try {
			states = loadConfigletStates(em);
			createOrUpdateConfiglets(states);
			shutdownOldConfiglets(em);
		}
		finally { if (em.isOpen()) em.close(); }
	}


	@SuppressWarnings({ "unchecked", "JpaQueryApiInspection", "MethodMayBeStatic" })
	private ConfigletState[] loadConfigletStates(final EntityManager emParam) {
		final ConfigletState[] states;
		emParam.getTransaction().begin();
		try {
			Query query = emParam.createNamedQuery("listAllConfiglets");
			final List<ConfigletState> list = (List<ConfigletState>) query.getResultList();
			Object[] states_ = list.toArray();
			states = new ConfigletState[states_.length];
			for (int i = 0; i < states_.length; i++)
				states[i] = (ConfigletState) states_[i];
			return states;
		}
		finally {
			if (emParam.getTransaction().isActive())
				emParam.getTransaction().commit();
		}
	}


	private void createOrUpdateConfiglets(final ConfigletState[] statesParam) {
		for (ConfigletState configletState : statesParam) {
			final String name = configletState.getName();
			if (configlets.containsKey(name)) {
				configlets.get(name).update(configletState.getState());
			}
			else {
				final Configlet configlet = createConfiglet(configletState);
				configlets.put(name, configlet);
			}
		}
	}

    
	@SuppressWarnings({ "FeatureEnvy" })
	private Configlet createConfiglet(final ConfigletState configParam) {
		try {
			final Class<? extends Configlet> clazz = Class.forName(configParam.getClassName()).asSubclass(Configlet.class);
			final Configlet instance = clazz.newInstance();
			instance.init(logger, configParam.getName(), configParam.getState());
			return instance;
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}



	@SuppressWarnings({ "SuspiciousMethodCalls" })
	private void shutdownOldConfiglets(final EntityManager emParam) {
		Set<String> set = configlets.keySet();
		Object[] keys = set.toArray();
		for (Object name : keys) {
			emParam.getTransaction().begin();
			try {
				if (emParam.find(ConfigletState.class, name) == null) {
					Configlet let = configlets.get(name);
					configlets.remove(name);
					let.shutdown();
				}
			}
			catch (RuntimeException e) {
				logger.warn(e);
			}
			finally {
				if (emParam.getTransaction().isActive())
					emParam.getTransaction().commit();
			}
		}
	}


	void shutdownConfiglets() {
		for (Configlet configlet : configlets.values())
		    try {
			    configlet.shutdown();
		    }
		    catch (RuntimeException e) {
			    logger.warn(e);
		    }		
	}

	public <T extends Configlet> T getConfiglet(final @NotNull Class<T> clazz, final @NotNull String name) {
		return clazz.cast(configlets.get(name));
	}


    public synchronized GNDMServiceHome lookupServiceHome(@NotNull String instancePrefix) {
        return getInstance(GNDMServiceHome.class, instancePrefix+"Home");
    }


    public GroovyBindingFactory createBindingFactory() {
        return new GNDMSBindingFactory();
    }



    public @NotNull String getSystemName() {
        return systemName;
    }


	@NotNull
	@SuppressWarnings({ "HardcodedFileSeparator" })
	public String getSystemTempDirName() {
		String tmp = System.getenv("GNDMS_TMP");
		tmp = tmp == null ?  ""  : tmp.trim();
		if (tmp.length() == 0) {
			tmp = System.getenv("TMPDIR");
			tmp = tmp == null ?  ""  : tmp.trim();
		}
		if (tmp.length() == 0) {
			tmp = "/tmp";
		}
		return tmp;
	}


	public void configure(final @NotNull Binder binder) {
		// binder.bind(EntityManagerFactory.class).toInstance();
		binder.bind(BoundInjector.class).toInstance(boundInjector);
		binder.bind(SystemDirectory.class).toInstance(this);
		binder.bind(SystemInfo.class).toInstance(this);
		binder.bind(InstanceProvider.class).toInstance(this);
		binder.bind(ServiceHomeProvider.class).toInstance(this);
		binder.bind(TaskActionProvider.class).toInstance(this);
		binder.bind(ORQCalculatorProvider.class).toInstance(this);
		binder.bind(ConfigletProvider.class).toInstance(this);
		binder.bind(ModelUUIDGen.class).toInstance(uuidGen);
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

    private static class OfferTypeIndustrialPark<T extends KeyFactoryInstance<OfferType, T>>
            extends IndustrialPark<OfferType, String, T> {

        private OfferTypeIndustrialPark(
                final @NotNull
                KeyFactory<OfferType, RecursiveKeyFactory<OfferType, T>> factoryParam) {
            super(factoryParam);
        }


        @NotNull
        @Override
            public String mapKey(final @NotNull OfferType keyParam) {
            return keyParam.getOfferTypeKey();
        }
    }

	public @NotNull Injector getSystemAccessInjector() {
		return boundInjector.getInjector();
	}
}
