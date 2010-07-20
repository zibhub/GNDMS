package de.zib.gndms.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Subclassed by model entity id helper classes
 *
 * Defines a sensible default equals method based on identity, null, and instanceof-testing
 * and defer all other checks to equalFields.
 */
abstract public class ModelId extends ModelObject {
	abstract protected boolean equalFields(@NotNull Object obj);

	@SuppressWarnings({"EqualsWhichDoesntCheckParameterClass"})
    @Override
	public boolean equals(@Nullable Object obj) {
        return obj != null && (obj == this || this.getClass().isInstance(obj) && this.equalFields(obj));
    }
}
