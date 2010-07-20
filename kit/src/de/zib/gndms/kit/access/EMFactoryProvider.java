package de.zib.gndms.kit.access;

import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManagerFactory;


/**
 * Simple interface for classes that provide access to an EntityManagerFactory
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 08.08.2008 Time: 11:27:22
 */
public interface EMFactoryProvider {
	public @NotNull	EntityManagerFactory getEntityManagerFactory();
}
