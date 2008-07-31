package de.zib.gndms.model.common

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Column
import org.jetbrains.annotations.NotNull
import javax.xml.namespace.QName
import javax.persistence.Transient
import javax.persistence.IdClass
import javax.persistence.UniqueConstraint
import javax.persistence.Embeddable
import de.zib.gndms.model.ModelObject


/**
 * VEPRefs for grid resources that use a SimpleResourceKey with a UUID string value
 * (i.e. introduce-generated services)
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 30.07.2008 Time: 15:03:31
 */
@Entity(name="SimpleRKRefs") @IdClass(SimpleRKVEPRefId.class)
@UniqueConstraint(columnNames = ["rkValue"])
abstract class SimpleRKVEPRef extends VEPRef {
	private String resourceKeyValue;

	@Transient
	def abstract public QName getResourceKeyName();

	@Id
	@Column(name="rkValue", nullable=false, length=36, columnDefinition="CHAR", updatable=false)
	def public String getResourceKeyValue() { resourceKeyValue }
	def public void setResourceKeyValue(final String newValue) { resourceKeyValue = newValue }
}


@Embeddable
abstract class SimpleRKVEPRefId extends VEPRefId {
	String resourceKeyValue;

	@Override
	def public boolean equalFields(@NotNull final Object obj) {
		super.equalFields(obj) &&
			  (getResourceKeyValue() == ((SimpleRKVEPRefId)obj).getResourceKeyValue())
	}

	@Override
	def public int hashCode()
	{	return super.hashCode() ^ ModelObject.hashCode0(getResourceKeyValue()) }
}