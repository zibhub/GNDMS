package de.zib.gndms.stuff.copy;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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

import java.io.Serializable;
import java.lang.annotation.*;


/**
 * This annotation is used to choose a copymode
 *
 * @see Copier
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 27.11.2008 Time: 17:20:28
 */
@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) @Inherited 
public @interface Copyable {
	@NotNull CopyMode value();

    /**
     * An enum holding all implemented possibilities to copy an instance.
     *
     * @see de.zib.gndms.stuff.copy.Copier
     * @author  try ste fan pla nti kow zib
     * @version $Id$
     *
     *          User: stepn Date: 27.11.2008 Time: 17:21:01
     */
    static enum CopyMode implements Serializable {
        /** copies an instance using its clone method */ CLONE,
        /** copies an instance using its mold method */ MOLD,
        /** copies an instance using plain java (de)serialization */ SERIALIZE,
        /** copies an instance using its class' constructor*/ CONSTRUCT,
        /** instance must not be copied */ DONT
    }
}
