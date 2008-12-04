package de.zib.gndms.infra;

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
 * @author Stefan Plantikow <plantikow@zib.de>
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
