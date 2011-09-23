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



/**
 * Interface for a ProviderStageInOrder builder
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 10:06:00
 */
public interface ProviderStageInOrderWriter extends OrderWriter {

    public void writeDataFileName( String dfn );
    public void writeMetaDataFileName( String mfn );

    // see DataDescriptorWriter for an explanation of the following methods
    public DataDescriptorWriter getDataDescriptorWriter( );
    public void beginWritingDataDescriptor( );
    public void doneWritingDataDescriptor( );
}
