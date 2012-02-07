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


import de.zib.gndms.common.model.gorfx.types.AbstractOrder;
import de.zib.gndms.taskflows.filetransfer.client.FileTransferMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Order for file transfer.
 *
 *
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 29.09.2008, Time: 17:49:09
 */
public class FileTransferOrder extends AbstractOrder {

    private static final long serialVersionUID = 2695933268050863494L;

    /**
     * URI of the source files
     */
    private String sourceURI;

    /**
     * URI of the target files
     */
    private String destinationURI;

    /**
     * The map consists of pairs of source and target file names.
     * The target file name may be null, if it should be identical to the source files name.
     */ 
    private Map<String, String> fileMap;

    public FileTransferOrder() {
        super( );
        super.setTaskFlowType( FileTransferMeta.FILE_TRANSFER_TYPE_KEY );
    }


    @Override
    public @NotNull String getDescription() {
        return "File transfer from " + sourceURI + " to " + destinationURI;
    }


    public String getSourceURI() {
        return sourceURI;
    }


    public void setSourceURI( String sourceURI ) {
        this.sourceURI = sourceURI;
    }


    public String getDestinationURI() {
        return destinationURI;
    }


    public void setDestinationURI( String destinationURI ) {
        this.destinationURI = destinationURI;
    }


    public boolean hasFileMap( ) {
        return  fileMap != null;
    }
    

    public Map<String, String> getFileMap() {
        return fileMap;
    }


    public void setFileMap( Map<String, String> fileMap ) {
        this.fileMap = fileMap;
    }
}
