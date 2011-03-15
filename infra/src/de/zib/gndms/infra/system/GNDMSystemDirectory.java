package de.zib.gndms.infra.system;

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



import com.google.common.collect.Maps;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import de.zib.gndms.infra.access.ServiceHomeProvider;
import de.zib.gndms.infra.service.GNDMPersistentServiceHome;
import de.zib.gndms.infra.service.GNDMServiceHome;
import de.zib.gndms.infra.service.GNDMSingletonServiceHome;
import de.zib.gndms.kit.access.GNDMSBinding;
import de.zib.gndms.kit.configlet.DefaultConfiglet;
import de.zib.gndms.kit.monitor.GroovyBindingFactory;
import de.zib.gndms.kit.monitor.GroovyMoniServer;
import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.logic.access.TaskActionProvider;
import de.zib.gndms.logic.model.gorfx.*;
import de.zib.gndms.kit.access.InstanceProvider;
import de.zib.gndms.model.common.ConfigletState;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.common.GridResourceItf;
import de.zib.gndms.model.common.ModelUUIDGen;
import de.zib.gndms.model.common.types.factory.IndustrialPark;
import de.zib.gndms.model.common.types.factory.KeyFactory;
import de.zib.gndms.model.common.types.factory.KeyFactoryInstance;
import de.zib.gndms.model.common.types.factory.RecursiveKeyFactory;
import de.zib.gndms.stuff.BoundInjector;
import de.zib.gndms.kit.configlet.ConfigletProvider;
import de.zib.gndms.kit.configlet.Configlet;
import de.zib.gndms.kit.system.SystemInfo;
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
* @see GNDMSystem
* @author  try ste fan pla nti kow zib
* @version $Id$
*
*          User: stepn Date: 03.09.2008 Time: 16:50:06
*/
public class GNDMSystemDirectory implements SystemDirectory, Module {

    private @NotNull final Log logger = LogFactory.getLog(GNDMSystemDirectory.class);
    private static final int INITIAL_CAPACITY = 32;
    private static final long INSTANCE_RETRIEVAL_INTERVAL = 250L;

    private final @NotNull String systemName;

    /**
     * stores several instances needed for the <tt>GNDMSystem</tt>
     */
    private final @NotNull Map<String, Object> instances;


    private final @NotNull Map<Class<? extends GridResourceItf>, GNDMPersistentServiceHome<?>> homes;


	private final Map<String, Configlet> configlets = Maps.newConcurrentHashMap();

    @SuppressWarnings({ "RawUseOfParameterizedType" })
    private final @NotNull IndustrialPark<String, String, AbstractORQCalculator<?, ?>> orqPark;

    @SuppressWarnings({ "RawUseOfParameterizedType" })
    private final @NotNull IndustrialPark<String, String, ORQTaskAction<?>> taskActionPark;

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
        homes = new HashMap<Class<? extends GridResourceItf>, GNDMPersistentServiceHome<?>>(INITIAL_CAPACITY);
        systemName = sysNameParam;
		uuidGen = uuidGenParam;
	    sysHolderWrapper = systemHolderWrapParam;
		final Injector injector = Guice.createInjector(sysModule, this);
		boundInjector.setInjector(injector);
        GNDMSBinding.setDefaultInjector(injector);

	    final ORQCalculatorMetaFactory calcMF = new ORQCalculatorMetaFactory();
		calcMF.setInjector(injector);
	    calcMF.setWrap(sysHolderWrapper);
	    orqPark = new OfferTypeIndustrialPark<AbstractORQCalculator<?,?>>(calcMF);

	    final ORQTaskActionMetaFactory taskMF = new ORQTaskActionMetaFactory();
	    taskMF.setWrap(sysHolderWrapper);
		taskMF.setInjector(injector);
	    taskActionPark = new OfferTypeIndustrialPark<ORQTaskAction<?>>(taskMF);
    }

    /**
     * Adds the <tt>GNDMServiceHome</tt> and the corresponding <tt>org.globus.wsrf.Resource</tt> to the {@link #instances} map
     *
     * @param home a GNDMS Service resource home instance
     * @throws ResourceException if the corresponding ressource could not be found
     */
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
            if (offerTypeKey == null)
                throw new IllegalArgumentException("Unknow offer type: " + offerTypeKey);
            AbstractORQCalculator<?,?> orqc = orqPark.getInstance(offerTypeKey);
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
		TaskAction ta = taskActionPark.getInstance(offerTypeKey);
		ta.setUUIDGen( uuidGen );
		return ta;
	}


    /**
     * Waits until an instance on {@link #instances} with the key <tt>name</tt>, has been registered and returns it.
     *  
     * @param clazz the class the instance belongs to
     * @param name the name of the instance as denoted on the map {@link #instances}
     * @param <T> the class the instance will be casted to
     * @return an instance from the <tt>instances</tt> map
     */
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

    /**
     * Adds the <tt>GNDMServiceHome</tt> and the corresponding <tt>org.globus.wsrf.Resource</tt> to the {@link #instances} map
     * and stores the key <tt>modelClazz</tt> with the value <tt>home</tt> to {@link #homes}.
     *
     * <p>The key for the <tt>GNDMServiceHome</tt> is the nickname of <tt>home</tt> appended by "HOME", whereas
     * the key for the Resource is the nickname of <tt>home</tt> appended by "Resource".
     *
     * @param modelClazz the class instance of the model
     * @param home a GNDMS Service resource home instance
     * @param <K> the specific subclass of the model instance 
     * @throws ResourceException if the corresponding ressource could not be found
     */
    @SuppressWarnings({ "HardcodedFileSeparator", "RawUseOfParameterizedType" })
    public synchronized <K extends GridResourceItf> void addHome(
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

    /**
     * Returns the GNDMPersistentServiceHome which is mapped by the key <tt>modelClazz</tt> in the map {@link #homes}. 
     *
     * @param modelClazz the class of the model
     * @param <M> the specific class the model belongs to
     * @return
     */
    @SuppressWarnings({ "unchecked" })
    public synchronized <M extends GridResource> GNDMPersistentServiceHome<M>
    getHome(Class<M> modelClazz) {
        final GNDMPersistentServiceHome<M> home =
                (GNDMPersistentServiceHome<M>) homes.get(modelClazz);
        if (home == null)
            throw new IllegalStateException("Unknown home");
        return home;
    }

    /* Adds an instance to the {@link #instances} map.
     * The name which will be mapped to the instance must not end with the keywords "HOME","Resource" or "ORQC".
     *
     * <p> Except them, there are more keywords which are not allowed.
     * See {@link #addInstance_ }, as this method will be invoked

     *
     * @param name the name which is to be mapped to the specified instance
     * @param obj the instance to be associated with the specified name
     */
    public synchronized void addInstance(@NotNull String name, @NotNull Object obj) {
        if (name.endsWith("Home") || name.endsWith("Resource") || name.endsWith("ORQC"))
            throw new IllegalArgumentException("Reserved instance name");

        addInstance_(name, obj);

    }

    
    /**
     * Adds an instance to the {@link #instances} map.
     * The name which will be mapped to the instance must not be equal to the keywords "out","err","args","em" or "emg".
     *
     * @param name the name which is to be mapped to the specified instance
     * @param obj the instance to be associated with the specified name
     */
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


    /**
     * Returns the instance, which has been registered on {@link #instances} with the name <tt>name</tt>.
     * The instace will be casted to the parameter <tt>T</tt> of <tt>clazz</tt>.
     *
     * @param clazz the class the instance belongs to
     * @param name the name of the instance as denoted on the map {@link #instances}
     * @param <T> the class the instance will be casted to
     * @return an instance from the <tt>instances</tt> map
     */
    public synchronized @NotNull <T> T getInstance(@NotNull Class<? extends T> clazz,
                                                   @NotNull String name)
    {
        final Object obj = instances.get(name);
        if (obj == null)
            throw new
                  IllegalStateException("Null instance retrieved or invalid or unregistered name");
        return clazz.cast(obj);
    }

    /**
     * Creates a new EntityManager using <tt>emf</tt>.
     *
     * <p>Calls {@link #loadConfigletStates(javax.persistence.EntityManager)} and
     * {@link #createOrUpdateConfiglets(de.zib.gndms.model.common.ConfigletState[])} to load all configlets managed by
     * this EntityManager and update the {@link #configlets} map.
     * Old Configlets will be removed and shutted down using {@link #shutdownConfiglets()} 
     *
     * @param emf the factory the EntityManager will be created of
     */
	public synchronized void reloadConfiglets(final EntityManagerFactory emf) {
		ConfigletState[] states;
		EntityManager em = emf.createEntityManager();
		try {
			states = loadConfigletStates(em);
			createOrUpdateConfiglets(states);
			shutdownOldConfiglets(em);
		}
		finally { if (em.isOpen()) em.close(); }
	}

    /**
     * Loads all <tt>ConfigletStates</tt> managed by a specific <tt>EntityManager</tt> into an array.
     *
     * <p>Performs the query "listAllConfiglets" on the database and returns an array containing the result.
     *
     * @param emParam an EntityManager managing ConfigletStates
     * @return an array containing all ConfigletStates of the database
     */
	@SuppressWarnings({ "unchecked", "JpaQueryApiInspection", "MethodMayBeStatic" })
	private synchronized ConfigletState[] loadConfigletStates(final EntityManager emParam) {
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

    /**
     * Iterates through the <tt>ConfigletState</tt> array and either
     * updates the <tt>state</tt> of the corresponding <tt>Configlet</tt>, if already stored in the {@link #configlets} map,
     * or creates a new <tt>Configlet</tt> using {@link #createConfiglet(de.zib.gndms.model.common.ConfigletState)}
     * and stores it together with the name of the Configlet in the map.
     *
     * @param statesParam an array containing several ConfigletStates to be stored in the <tt>configlets</tt> map
     */
	private synchronized void createOrUpdateConfiglets(final ConfigletState[] statesParam) {
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

    /**
     * Creates a <tt>Configlet</tt> out of a ConfigletState.
     *
     * <p>The created instance uses {@link #logger} as its <tt>Log</tt> object.
     * The name and state of the new Configlet is taken from <tt>configParam</tt>. 
     *
     * @param configParam A ConfigletState to be converted to a Configlet
     *
     * @return a Configlet out of a ConfigletState
     */
	@SuppressWarnings({ "FeatureEnvy" })
	private synchronized Configlet createConfiglet(final ConfigletState configParam) {
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


    /**
     * Removes old configlets from the {@link #configlets} map.
     *
     * Checks for every <tt>Configlet</tt> in the map, if still exists in the database.
     * If not, the map entry will be removed and <tt>shutdown()</tt> invoked on the old configlet entry. 
     *
     * @param emParam an EntityManager managing <tt>Configlet</tt>s
     */
	@SuppressWarnings({ "SuspiciousMethodCalls" })
	private synchronized void shutdownOldConfiglets(final EntityManager emParam) {
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

    /**
     * Shuts down all configlets stored in the {@link #configlets} map
     */
	synchronized void shutdownConfiglets() {
		for (Configlet configlet : configlets.values())
		    try {
			    configlet.shutdown();
		    }
		    catch (RuntimeException e) {
			    logger.warn(e);
		    }		
	}

    /**
     * Retrieves the configlet stored with the key <tt>name</tt> from {@link #configlets} map, casts it to a <tt>T</tt> class
     * and returns it.
     *
     * @param clazz the class the Configlet belongs to
     * @param name the name of the Configlet
     * @param <T> the class the instance will be casted to 
     * @return a casted configlet from the <tt>configlets</tt> map
     */
	public <T extends Configlet> T getConfiglet(final @NotNull Class<T> clazz, final @NotNull String name) {
		return clazz.cast(configlets.get(name));
	}

    /**
     * Returns a <tt>GNDMServiceHome</tt> stored with a specif name in the {@link #instances} map.
     *
     * @param instancePrefix the prefix for the lookup key, which will be appended by "HOME"
     * @return
     */
    public synchronized GNDMServiceHome lookupServiceHome(@NotNull String instancePrefix) {
        return getInstance(GNDMServiceHome.class, instancePrefix+"Home");
    }


    public GroovyBindingFactory createBindingFactory() {
        return new GNDMSBindingFactory();
    }



    public @NotNull String getSystemName() {
        return systemName;
    }

    /**
     * Returns the value set for the environment variable <tt>GNDMS_TMP</tt>. If nothing denoted,
     * it will return the value of the enviroment variable <tt>TMPDIR</tt> instead.
     * If also not denoted, "/tmp" will be returned.
     *
     * @return the temp directory of the GNDMSystem according to enviroment variables
     */
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


   /**
     * Binds certain classes to {@code this} or other corresponding instances
     *
     * @param binder binds several classe with certain fields.
     */
	public void configure(final @NotNull Binder binder) {
		// binder.bind(EntityManagerFactory.class).toInstance();
		binder.bind(BoundInjector.class).toInstance(boundInjector);
		binder.bind(SystemDirectory.class).toInstance(this);
		binder.bind( SystemInfo.class).toInstance(this);
		binder.bind(InstanceProvider.class).toInstance(this);
		binder.bind(ServiceHomeProvider.class).toInstance(this);
		binder.bind(TaskActionProvider.class).toInstance(this);
		binder.bind(ORQCalculatorProvider.class).toInstance(this);
		binder.bind(ConfigletProvider.class).toInstance(this);
		binder.bind(ModelUUIDGen.class).toInstance(uuidGen);
	}

    public final String DEFAULT_SUBGRID_NAME="gndms";

    @NotNull
    public String getSubGridName() {
        final DefaultConfiglet defaultConfiglet = getConfiglet(DefaultConfiglet.class, "gridconfig");
        if (defaultConfiglet == null)
            return DEFAULT_SUBGRID_NAME;
        else
            return defaultConfiglet.getMapConfig().getOption("subGridName", DEFAULT_SUBGRID_NAME);
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

    private static class OfferTypeIndustrialPark<T extends KeyFactoryInstance<String, T>>
            extends IndustrialPark<String, String, T> {

        private OfferTypeIndustrialPark(
                final @NotNull
                KeyFactory<String, RecursiveKeyFactory<String, T>> factoryParam) {
            super(factoryParam);
        }


        @NotNull
        @Override
        public String mapKey(final @NotNull String keyParam) {
            return keyParam;
        }
    }

	public @NotNull Injector getSystemAccessInjector() {
		return boundInjector.getInjector();
	}
}
