/**
 * Holds storage size
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 27.07.2008 Time: 18:43:28
 */
package de.zib.gndms.model.dspace

import javax.persistence.Embeddable
import javax.persistence.Column
import de.zib.gndms.model.ModelObject
import org.jetbrains.annotations.NotNull

@Embeddable
class StorageSize extends ModelObject {
	@Column(name="unit", nullable=false, columnDefinition="VARCHAR")
	String unit;

	@Column(name="amount", nullable=false)
	long amount;

	public long getAmountInBytes() {
		long baseAmount = getAmount()
		String nUnit = normalizeUnit(getUnit().trim())
		switch (nUnit) {
			case "PB":
				baseAmount *= 1024
			case "TB":
				baseAmount *= 1024
			case "GB":
				baseAmount *= 1024
			case "MB":
				baseAmount *= 1024
			case "KB":
			case "kB":
				baseAmount *= 1024
			case "":
			case "b":
			case "B":
				return baseAmount
			default:
				throw new IllegalStateException("Unknown unit: '$unit'");
		}
	}

	@NotNull String normalizeUnit(@NotNull String unit) {
		if (len >= 5 && unit.toLowerCase().endsWith("bytes"))
			unit.substring(0, unit.length() - 4)
		else
			unit
	}
}