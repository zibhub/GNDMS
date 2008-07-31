package de.zib.gndms.model.common;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Embeddable
import javax.persistence.ManyToOne
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Transient
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import de.zib.gndms.model.ModelEntityId
import javax.persistence.JoinColumn
import de.zib.gndms.model.ModelObject
import javax.persistence.DiscriminatorColumn
import javax.persistence.DiscriminatorType
import de.zib.gndms.model.ModelEntity

/**
 * Abstract superclass of "virtual" endpoint references.
 *
 * Points to a resource on a runtime-resolved GridSite.
 *
 * Subclasses specialize by resource key type.
 *
 * No version field since instances are immutable
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 28.07.2008 Time: 14:34:05
 */
@SuppressWarnings(["ClassNamingConvention"])
@Entity(name="Refs") @IdClass(VEPRefId.class)
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
// DiscriminatorColumn(name="refType", discriminatorType=DiscriminatorType.STRING)
abstract class VEPRef extends ModelEntity {
	private GridSite gridSite;

	@Id
	@ManyToOne(optional = true)
	@JoinColumn(name="siteId", referencedColumnName="siteId", nullable=true, updatable=false)
	def public GridSite getGridSite() { gridSite }
	def public void setGridSite(final GridSite newSite) { gridSite = newSite }

	@Transient
	def abstract public String getParentResourceName();
}

@Embeddable
abstract class VEPRefId extends ModelEntityId {
	String gridSite;

	@Override
	def public int hashCode()
		{ ModelObject.hashCode0(getGridSite()) }

	def boolean equalFields(final @NotNull Object obj)
		{ getGridSite() == ((VEPRefId)obj).getGridSite() }
}