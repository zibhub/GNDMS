package de.zib.gndms.dspace.common;

import de.zib.gndms.model.dspace.StorageSize;
import org.apache.axis.types.PositiveInteger;
import org.jetbrains.annotations.NotNull;
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
		return new StorageSizeT(size.getUnit(), new PositiveInteger(Integer.toString(size.getAmount())));
	}

	@NotNull
	public static StorageSize buildSize(StorageSizeT sizeT) {
		StorageSize value = new StorageSize();
		value.setUnit(sizeT.getStorageSizeUnit());
		value.setAmount(Integer.parseInt(sizeT.getStorageSizeValue().toString()));
		return value;
	}
}
