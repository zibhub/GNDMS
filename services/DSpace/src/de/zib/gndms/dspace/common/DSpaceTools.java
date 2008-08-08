package de.zib.gndms.dspace.common;

import de.zib.gndms.model.dspace.StorageSize;
import de.zib.gndms.model.dspace.DSpaceRef;
import de.zib.gndms.dspace.stubs.types.DSpaceReference;
import de.zib.gndms.dspace.service.globus.resource.ExtDSpaceResourceHome;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.infra.GNDMSTools;
import org.apache.axis.types.PositiveInteger;
import org.jetbrains.annotations.NotNull;
import org.globus.wsrf.ResourceKey;
import types.StorageSizeT;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 27.07.2008 Time: 19:18:59
 */
public class DSpaceTools {
	public static StorageSizeT buildSizeT(StorageSize size) {
		return new StorageSizeT(size.getUnit(), new PositiveInteger(Long.toString(size.getAmount())));
	}

	@NotNull
	public static StorageSize buildSize(StorageSizeT sizeT) {
		StorageSize value = new StorageSize();
		value.setUnit(sizeT.getStorageSizeUnit());
		value.setAmount(Long.parseLong(sizeT.getStorageSizeValue().toString()));
		return value;
	}


	public static DSpaceReference dSpaceRefsAsReference(final DSpaceRef vep, GNDMSystem sysParam)
		throws Exception {
		if (vep.getGridSiteId() != null)
			throw new IllegalStateException("Remote DSpace reference");

		final ResourceKey key = GNDMSTools.SRKVepRefAsKey(vep);
		final ExtDSpaceResourceHome dspaceHome =
			  sysParam.getInstance(ExtDSpaceResourceHome.class, "dspaceHome");
		final String serviceURI = dspaceHome.getServiceAddress().toString();
		return new DSpaceReference(org.globus.wsrf.utils.AddressingUtils.createEndpointReference(serviceURI, key));
	}
}
