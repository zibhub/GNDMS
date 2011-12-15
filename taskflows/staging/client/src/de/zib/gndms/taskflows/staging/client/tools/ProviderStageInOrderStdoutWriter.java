package de.zib.gndms.taskflows.staging.client.tools;

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


import de.zib.gndms.common.model.gorfx.types.io.OrderStdoutWriter;
import de.zib.gndms.taskflows.staging.client.tools.DataDescriptorStdoutWriter;
import de.zib.gndms.taskflows.staging.client.tools.DataDescriptorWriter;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 17:36:36
 */
public class ProviderStageInOrderStdoutWriter extends OrderStdoutWriter implements ProviderStageInOrderWriter {


    public void writeDataFileName( String dfn ) {
        System.out.println ( "DataFileName: " +dfn );
    }


    public void writeMetaDataFileName( String mfn ) {
        System.out.println ( "MetaDataFileName: " +mfn );
    }


    public DataDescriptorWriter getDataDescriptorWriter() {
        return new DataDescriptorStdoutWriter();
    }


    public void beginWritingDataDescriptor() {
        // Not required here
    }


    public void doneWritingDataDescriptor() {
        // Not required here
    }


    public void begin() {
        System.out.println( "******************** ProviderStageInOrder ********************" );
    }


    public void done() {
        System.out.println( "******************* EOProviderStageInORQ *******************" );
    }
}
