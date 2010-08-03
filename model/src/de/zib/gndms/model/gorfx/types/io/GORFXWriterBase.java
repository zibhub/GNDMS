package de.zib.gndms.model.gorfx.types.io;

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



/**
 * Interface for writer classes of the gorfx model.
 *
 * A gndms class can be written to Stdout, it can be written as a Properties instance, or a their corresponding axis type.
 *
 * @see de.zib.gndms.model.gorfx.types.io.GORFXConverterBase
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 17.09.2008, Time: 15:27:47
 */
public interface GORFXWriterBase {

    /**
     * Defines the writer's action, before the convertion starts.
     */
    public void begin ( );

    /**
     * Defines the writer's action, after the convertion has finished.
     */
    public void done ( );
}
