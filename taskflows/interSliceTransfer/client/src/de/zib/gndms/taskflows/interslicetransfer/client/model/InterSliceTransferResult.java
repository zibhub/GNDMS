package de.zib.gndms.taskflows.interslicetransfer.client.model;
/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.model.gorfx.types.SliceResult;
import de.zib.gndms.taskflows.filetransfer.client.model.FileTransferResult;
import de.zib.gndms.taskflows.interslicetransfer.client.InterSliceTransferMeta;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 20.02.12  18:13
 * @brief
 */
public class InterSliceTransferResult extends FileTransferResult implements SliceResult {

    private static final long serialVersionUID = -2893502852619621570L;

    private Specifier<Void> sliceSpecifier;


    public InterSliceTransferResult() {
        super();
        setTaskFlowType( InterSliceTransferMeta.INTER_SLICE_TRANSFER_KEY );
    }


    @Override
    public Specifier<Void> getSliceSpecifier() {

        return sliceSpecifier;
    }


    @Override
    public void setSliceSpecifier( final Specifier<Void> sliceSpecifier ) {

        this.sliceSpecifier = sliceSpecifier;
    }


    public void populate( final FileTransferResult transferResult ) {
        setFiles( transferResult.getFiles() );
    }
}
