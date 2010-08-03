package de.zib.gndms.infra;

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



import org.globus.wsrf.impl.SimpleResourceKey;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.utils.AddressingUtils;
import org.jetbrains.annotations.NotNull;
import org.apache.axis.message.addressing.AttributedURI;
import org.apache.axis.message.addressing.EndpointReferenceType;
import de.zib.gndms.model.common.SimpleRKRef;
import de.zib.gndms.model.dspace.types.SliceRef;


/**
 * Utility functions that are used globally.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 07.08.2008 Time: 15:52:55
 */
public final class GNDMSTools {
	public static SimpleResourceKey SRKVepRefAsKey(final SimpleRKRef vep) {
		return new SimpleResourceKey(vep.getResourceKeyName(), vep.getResourceKeyValue());
	}


    public static EndpointReferenceType SliceRefAsEPR( final SliceRef sr ) throws Exception {

        SimpleResourceKey sk = SRKVepRefAsKey( sr );
        return AddressingUtils.createEndpointReference( sr.getGridSiteId(), sk );
    }


    @NotNull
	public static AttributedURI getServiceAddressFromContext() throws Exception {
		final ResourceContext context = ResourceContext.getResourceContext();
		return AddressingUtils.createEndpointReference(context, null).getAddress();
	}
}
