package de.zib.gndms.model;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



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
