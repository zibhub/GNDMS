package de.zib.gndms.infra.db;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static java.util.Collections.unmodifiableMap;
import java.util.Map;


/**
 * EntityManagerFactory that cannot be closed nor allows a modification of the parameter map.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 08.08.2008 Time: 11:21:21
 */
@SuppressWarnings({ "RawUseOfParameterizedType" })
public final class RestrictedEMFactory implements EntityManagerFactory {
	private final EntityManagerFactory factory;
	private final Map paramMap;


	@SuppressWarnings({ "unchecked" })
	public RestrictedEMFactory(@NotNull final EntityManagerFactory factoryParam,
	                                      final Map mapParam) {
		factory = factoryParam;
		paramMap = mapParam == null ? null : unmodifiableMap(mapParam);
	}


	public RestrictedEMFactory(@NotNull final EntityManagerFactory factoryParam) {
		this(factoryParam, null);
	}

	public boolean isOpen() {
		return factory.isOpen();
	}


	public EntityManager createEntityManager() {
		if (paramMap == null)
			return factory.createEntityManager();
		else
			return factory.createEntityManager(paramMap);
	}


	public EntityManager createEntityManager(final Map mapParam) {
		throw new UnsupportedOperationException();
	}


	public void close() {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings({ "ReturnOfCollectionOrArrayField" })
	@Nullable
	public Map getParamMap() {
		return paramMap;
	}
}
