package de.zib.gndms.GORFX.ORQ.service.globus.resource;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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



import de.zib.gndms.GORFX.ORQ.stubs.types.ORQReference;
import de.zib.gndms.GORFX.service.globus.resource.ExtGORFXResourceHome;
import de.zib.gndms.infra.GNDMSTools;
import de.zib.gndms.infra.GridConfig;
import de.zib.gndms.infra.service.GNDMServiceHome;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.model.common.ModelUUIDGen;
import org.apache.axis.message.addressing.AttributedURI;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.impl.SimpleResourceKey;
import org.globus.wsrf.utils.AddressingUtils;
import org.jetbrains.annotations.NotNull;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;


/**
 * This class overrides the ResourceHome that is automatically generated by introduce for Globus
 * Toolkit. In GNDMS this is mainly necessary to provide RDBMS/JPA-based resource persistence.
 * In order to use the extended resource home they have to be configured in jndi-config.xml.
 * If this has been done properly, you should see an info-level log message during the start up
 * of the web service container that notifies succesfull initialization of the extended resource
 * home.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 16.07.2008 Time: 12:35:27
 */
public final class ExtORQResourceHome extends ORQResourceHome implements GNDMServiceHome {

    private boolean initialized;

    @NotNull
    private GNDMSystem system;

    @NotNull
    private AttributedURI serviceAddress;

    // logger can be an instance field since resource home classes are instantiated at most once
	@NotNull
	@SuppressWarnings({"FieldNameHidesFieldInSuperclass"})
	private final Log logger = LogFactory.getLog(ExtORQResourceHome.class);

	@SuppressWarnings({ "UseOfSystemOutOrSystemErr" })
    @Override
	public synchronized void initialize() throws Exception {
        if (! initialized) {
			logger.info("Extended ORQ home initializing");
			try { try {
                final GridConfig gridConfig = ExtGORFXResourceHome.getGridConfig();
                logger.debug("Config: " + gridConfig.asString());
                system = gridConfig.retrieveSystemReference();
				serviceAddress = GNDMSTools.getServiceAddressFromContext();

				initialized = true;

				super.initialize();    // Overridden method
			}
			catch ( NamingException e) {
				throw new RuntimeException(e);
			} }
            catch (RuntimeException e) {
                initialized = false;
                logger.error("Initialization failed", e);
                e.printStackTrace(System.err);
                throw e;
            }
		}
    }


    private void ensureInitialized() {
        try
        { initialize();	}
        catch (Exception e) {
            logger.error("Unexpected initialization error", e);
            throw new RuntimeException(e);
        }
    }


    @Override
    protected Resource createNewInstance() throws ResourceException {

        ORQResource res = (ORQResource) super.createNewInstance();
        res.setHome( this );
        return res;
    }


    @NotNull
    public synchronized GNDMSystem getSystem() throws IllegalStateException {
        ensureInitialized();
        return system;
    }


    public void setSystem(@NotNull GNDMSystem newSystem) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cant overwrite system");
    }

    @NotNull
    public String getNickName() {
        return "orq";
    }

    public ResourceKey getKeyForId(@NotNull final String id) {
        return new SimpleResourceKey( getKeyTypeName(), id );
    }


    @NotNull
    public EntityManagerFactory getEntityManagerFactory() {
        return getSystem().getEntityManagerFactory();
    }


    @NotNull
    public synchronized AttributedURI getServiceAddress() {
        ensureInitialized();
        return serviceAddress;
    }


    @Override
    public Resource find(final ResourceKey resourceKeyParam) throws ResourceException {
        return super.find(resourceKeyParam);    // Overridden method
    }


    public ModelUUIDGen getModelUUIDGen() {
        return getSystem().getModelUUIDGen();
    }


    @Override
    public ORQReference getResourceReference(final @NotNull ResourceKey key) throws Exception {
		EndpointReferenceType epr = AddressingUtils.createEndpointReference(serviceAddress.toString(), key);
		ORQReference ref = new ORQReference();
		ref.setEndpointReference(epr);
		return ref;
    }

}
