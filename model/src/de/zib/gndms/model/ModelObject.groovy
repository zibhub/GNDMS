package de.zib.gndms.model

import org.jetbrains.annotations.Nullable
import org.jetbrains.annotations.NotNull
import javax.persistence.Embeddable
import javax.persistence.Transient
import javax.persistence.Version
import javax.persistence.MappedSuperclass

/**
 * Shared superclass of all model objects.
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 30.07.2008 Time: 17:03:24
 */
public abstract class ModelObject {

	def public static int hashCode0(Object obj) {
		return obj == null ? 0 : obj.hashCode();
	}

}

/**
 * Shared superclass of all model entities.
 */
public abstract class ModelEntity extends ModelObject {
}

/**
 * Subclassed by model entity id helper classes
 *
 * Defines a sensible default equals method based on identity, null, and instanceof-testing
 * and deferrs all other checks to equalFields.
 */
public abstract class ModelEntityId {
	def public abstract boolean equalFields(@NotNull Object obj)

	@Override
	def public boolean equals(@Nullable Object obj) {
		if (obj == null) return false;
		if (obj.is(this)) return true;
		if (! this.class.isInstance(obj))
			return false;
		return this.equalFields(obj);
	}
}