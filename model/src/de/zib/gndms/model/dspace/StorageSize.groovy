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
import de.zib.gndms.model.ModelObject

@Embeddable
class StorageSize extends ModelObject {
	@Column(name="unit", nullable=false, columnDefinition="VARCHAR")
	String unit;

	@Column(name="amount", nullable=false)
	long amount;
}