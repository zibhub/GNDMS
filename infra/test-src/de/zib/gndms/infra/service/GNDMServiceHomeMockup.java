package de.zib.gndms.infra.service;

import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.dspace.DSpace;
import org.apache.axis.types.URI;
import org.globus.wsrf.*;
import org.globus.wsrf.impl.SimpleResourceKey;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.xml.namespace.QName;


/**
 * Mockup-home for testing ModelHandler
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 08.08.2008 Time: 13:30:08
 */
public final class GNDMServiceHomeMockup implements GNDMServiceHome {
	private final GNDMSystem sys;


	public GNDMServiceHomeMockup(final GNDMSystem sysParam) {
		sys = sysParam;
	}


	@NotNull
    public URI getServiceAddress() {
		throw new UnsupportedOperationException();
	}


	public QName getResourceKeyTypeName() {
		throw new UnsupportedOperationException();
	}


	@NotNull
	public EntityManagerFactory getEntityManagerFactory() {
		return sys.getEntityManagerFactory();
	}


	@NotNull
	public GNDMSystem getSystem() throws IllegalStateException {
		return sys;
	}


	public void setSystem(final @NotNull GNDMSystem system) throws IllegalStateException {
		throw new IllegalStateException("Cant't overwrite system");
	}


    public Query getListAllQuery(final @NotNull EntityManager em) {
        throw new UnsupportedOperationException();
    }


    public void refresh(final @NotNull DSpace resource) {
    }


    @NotNull
    public String getNickName() {
        return "dspaceMockup";
    }


    @NotNull
    public Class<DSpace> getModelClass() {
        return DSpace.class;
    }

    @NotNull
    public ResourceKey getKeyForResourceModel(final GridResource model) {
        throw new UnsupportedOperationException("Mockup");
    }


    @NotNull
    public ResourceKey getKeyForId(@NotNull final String id) {
        throw new UnsupportedOperationException("Mockup");
    }


    public Class getKeyTypeClass() {
        return SimpleResourceKey.class;
    }


    public QName getKeyTypeName() {
        return new QName("http://dspace.gndms.zib.de/DSpace", "DSpaceKey");
    }


    public Resource find(final ResourceKey resourceKeyParam)
            throws ResourceException, NoSuchResourceException, InvalidResourceKeyException {
        throw new UnsupportedOperationException();
    }


    public void remove(final ResourceKey resourceKeyParam)
            throws ResourceException, NoSuchResourceException, InvalidResourceKeyException,
            RemoveNotSupportedException {
        throw new RemoveNotSupportedException();
    }
}
