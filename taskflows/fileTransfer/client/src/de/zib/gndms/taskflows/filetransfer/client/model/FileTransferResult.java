package de.zib.gndms.taskflows.filetransfer.client.model;

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


import de.zib.gndms.model.gorfx.types.AbstractTaskFlowResult;
import de.zib.gndms.model.gorfx.types.GORFXConstantURIs;

import java.util.List;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 12:38:01
 */
public class FileTransferResult extends AbstractTaskFlowResult {

    private static final long serialVersionUID = -1282729323012008244L;
    private List<String> files;

    public FileTransferResult( ) {
        super( );
        super.setTaskFlowType( GORFXConstantURIs.FILE_TRANSFER_URI );
    }

    public List<String> getFiles() {
        return files;
    }


    public void setFiles( List<String> files ) {
        this.files = files;
    }
}
