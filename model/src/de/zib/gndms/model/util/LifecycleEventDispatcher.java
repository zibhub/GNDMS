package de.zib.gndms.model.util;

import de.zib.gndms.model.common.GridEntity;
import org.jetbrains.annotations.NotNull;

import javax.persistence.PostPersist;
import java.util.HashMap;
import java.util.Map;

/**
 * Can be used to dispatch Lifecycle events to an upper layer without exposing its classes
 * Drawback: Entities require an additional field that denotes the responsible InstanceResolver
 *
 * Typical usage: Before starting the database, call createDispatcher(gridName) and
 * register handlers-per-class.  Using entites must subclass GridEntity.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 04.08.2008 Time: 12:07:02
 */
@SuppressWarnings({"RawUseOfParameterizedType"})
public final class LifecycleEventDispatcher {
	private static final Map<String, LifecycleEventDispatcher>
		dispatchers = new HashMap<String, LifecycleEventDispatcher>(1);

	@NotNull
	public static synchronized LifecycleEventDispatcher lookupDispatcher(@NotNull String name) {
		LifecycleEventDispatcher dispatcher = dispatchers.get(name);
		if (dispatcher == null)
			throw new IllegalArgumentException("Unknown dispatcher '" + name + '\'');
		return dispatcher;
	}

	@NotNull
	public static synchronized LifecycleEventDispatcher createDispatcher(
		  @NotNull String name,
		  @NotNull InstanceResolver<Object> theResolver, int expectedRegistrations)
	{
		LifecycleEventDispatcher dispatcher = dispatchers.get(name);
		if (dispatcher != null)
			throw new IllegalArgumentException("Cant overwrite dispatcher '" + name + '\'');
		dispatcher = new LifecycleEventDispatcher(theResolver, expectedRegistrations);
		dispatchers.put(name, dispatcher);
		return dispatcher;
	}

	private Map<Class, String> targetNames;
	private InstanceResolver<Object> resolver;

	private LifecycleEventDispatcher() { throw new RuntimeException("Dont"); }

	public LifecycleEventDispatcher(@NotNull InstanceResolver<Object> theResolver, int capacity) {
		resolver = theResolver;
		targetNames = new HashMap<Class, String>(capacity);
	}

	public synchronized void register(@NotNull Class clazz, @NotNull String instName) {
		if (targetNames.containsKey(clazz))
			throw new IllegalArgumentException("Already registered");
		targetNames.put(clazz, instName);
	}

	public <T> T lookupTarget(@NotNull Class<T> clazz) {
		String instName = lookupTargetObject(clazz);
		return resolver.retrieveInstance(clazz, instName);
	}

	/* Hope this is ok */
	@SuppressWarnings({"CallToNativeMethodWhileLocked"})
	private synchronized <T> String lookupTargetObject(Class clazz) {
		String instName = null;
		Class nextClazz = clazz;
		while (instName == null) {
			instName = targetNames.get(nextClazz);
			nextClazz = clazz.getSuperclass();
		}
		return instName;
	}

	@PostPersist
	public static void jpaPostPersist(@NotNull Object persisted) {
		if (persisted instanceof GridEntity)
			lookupDispatcher(((GridEntity)persisted).getGridName()).gridPostPersist(persisted);
		else
			throw new IllegalArgumentException("Dont know how to handle persisted instance");
	}


	public void gridPostPersist(@NotNull Object persisted) {
		gridPostPersist(persisted.getClass(), persisted);
	}

	@SuppressWarnings({"unchecked"})
	private <V> void gridPostPersist(@NotNull Class<V> clazz, @NotNull Object persisted) {
		lookupTarget(PostPersistExecutor.class).onPostPersist(clazz, persisted);
	}
}
