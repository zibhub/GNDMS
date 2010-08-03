package de.zib.gndms.dspace.common;

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



import de.zib.gndms.dspace.service.globus.resource.ExtDSpaceResourceHome;
import de.zib.gndms.dspace.stubs.types.DSpaceReference;
import de.zib.gndms.infra.GNDMSTools;
import de.zib.gndms.infra.service.GNDMPersistentServiceHome;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.dspace.DSpaceRef;
import org.apache.axis.types.UnsignedLong;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceHome;
import org.globus.wsrf.ResourceKey;

/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 27.07.2008 Time: 19:18:59
 */
public class DSpaceTools {

    public static UnsignedLong unsignedLongFromLong( long val ) {
        return new UnsignedLong( val );
    }


    public static long longFromUnsignedInteger( UnsignedLong val ) {
        return val.longValue( );
    }


    public static DSpaceReference dSpaceRefsAsReference(final DSpaceRef vep, GNDMSystem sysParam)
		throws Exception {
		if (vep.getGridSiteId() != null)
			throw new IllegalStateException("Remote dspace reference");

		final ResourceKey key = GNDMSTools.SRKVepRefAsKey(vep);
		final ExtDSpaceResourceHome dspaceHome =
			  sysParam.getInstanceDir().getInstance(ExtDSpaceResourceHome.class, "dspaceHome");
		final String serviceURI = dspaceHome.getServiceAddress().toString();
		return new DSpaceReference(org.globus.wsrf.utils.AddressingUtils.createEndpointReference(serviceURI, key));
	}


    @SuppressWarnings({ "unchecked" })
    public static <M extends GridResource, H extends GNDMPersistentServiceHome<M> & ResourceHome>
         void refreshModelResource( M model, H home ) throws ResourceException {

        ResourceKey key = home.getKeyForResourceModel( model );
        ReloadablePersistentResource<M, H> rps =
                ( ReloadablePersistentResource<M, H> ) home.find( key );

        rps.loadFromModel( model );

    }
}
