package de.zib.gndms.model

import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

/**
 * Shared superclass of all model objects.
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 * User: stepn Date: 30.07.2008 Time: 17:03:24
 */
abstract class ModelObject {

	static int hashCode0(Object obj) {
		return obj.is(null) ? 0 : obj.hashCode();
	}

}

/**
 * Shared superclass of all model entities.
 */
abstract class ModelEntity extends ModelObject {
}

/**
 * Subclassed by model entity id helper classes
 *
 * Defines a sensible default equals method based on identity, null, and instanceof-testing
 * and deferrs all other checks to equalFields.
 */
abstract class ModelId extends ModelObject {
	abstract protected boolean equalFields(@NotNull Object obj)

	@Override
	 boolean equals(@Nullable Object obj) {
		if (obj.is(null)) return false;
		if (obj.is(this)) return true;
		if (! this.class.isInstance(obj))
			return false;
		return this.equalFields(obj);
	}
}