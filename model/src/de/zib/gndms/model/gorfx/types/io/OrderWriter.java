package de.zib.gndms.model.gorfx.types.io;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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



import java.util.HashMap;

/**
 * An GORFXWriter for ORQs.
 *
 * Provides the methods which are required to write an orq to a desired type.
 *
 * It should be used in conjunction with an OrderConverter.
 *
 * @see de.zib.gndms.model.gorfx.types.AbstractOrder
 * @see OrderConverter
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 17:02:56
 */
public interface OrderWriter extends GORFXWriterBase {

    public void writeJustEstimate( boolean je );

    public void writeContext( HashMap<String,String> ctx );

    public void writeId( String id );
}
