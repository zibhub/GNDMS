package de.zib.gndms.infra.system;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.QueryBuilder;
import java.util.Collections;
import static java.util.Collections.unmodifiableMap;
import java.util.Map;
import java.util.Set;


/**
 * Delegating EntityManagerFactory that cannot be closed nor allows a modification of
 * the parameter map.
 *
 * Used by GNDMSystem to protect its EntityManagerFactory.
 * 
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 08.08.2008 Time: 11:21:21
 */
@SuppressWarnings({ "RawUseOfParameterizedType" })
public final class RestrictedEMFactory implements EntityManagerFactory {
	private final EntityManagerFactory factory;
	private final Map paramMap;


	@SuppressWarnings({ "unchecked" })
	public RestrictedEMFactory(final @NotNull EntityManagerFactory factoryParam,
	                                      final Map mapParam) {
		factory = factoryParam;
		paramMap = mapParam == null ? null : unmodifiableMap(mapParam);
	}


	public RestrictedEMFactory(final @NotNull EntityManagerFactory factoryParam) {
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


	public Cache getCache() {return factory.getCache();}


	public Set<String> getSupportedProperties() {return factory.getSupportedProperties();}


	public QueryBuilder getQueryBuilder() {return factory.getQueryBuilder();}


	@SuppressWarnings({ "unchecked" })
	public Map getProperties() {return Collections.unmodifiableMap(factory.getProperties());}
}
