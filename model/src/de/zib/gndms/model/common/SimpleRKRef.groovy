package de.zib.gndms.model.common

import org.jetbrains.annotations.NotNull
import javax.xml.namespace.QName
import javax.persistence.Embeddable


/**
 * VEPRefs for grid resources that use a SimpleResourceKey with a UUID string value
 * (i.e. introduce-generated services)
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 30.07.2008 Time: 15:03:31
 */
@Embeddable
abstract class SimpleRKRef extends VEPRef {
	@NotNull
	def abstract public QName getResourceKeyName()

	def abstract public String getResourceKeyValue()
	def abstract public void setResourceKeyValue(final String newValue)
}

