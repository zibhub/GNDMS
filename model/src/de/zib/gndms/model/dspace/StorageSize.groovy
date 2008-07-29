/**
 * ThingAMagic.
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 27.07.2008 Time: 18:43:28
 */
package de.zib.gndms.model.dspace

import javax.persistence.Embeddable
import javax.persistence.Column
import org.jetbrains.annotations.NotNull

@Embeddable
class StorageSize {
	@Column(nullable=false) @NotNull
	String unit;

	@Column(nullable=false)
	int amount;
}