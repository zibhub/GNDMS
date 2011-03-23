package de.zib.gndms.model.gorfx;

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



import de.zib.gndms.model.common.GridEntity;

import javax.persistence.*;
import java.io.Serializable;


/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 30.09.2008, Time: 17:24:14
 */
public final class FTPTransferState implements Serializable {
    // maybe use fc constraint here
    private String transferId;
    private String ftpArgs;
    private String currentFile;


    /**
     * Returns the ready to use version of the ftpArgs argument.
     *
     * The normal getFtpArgs method only returns the pure data,
     * which isn't suited to construct a restart marker.
     * @return ftpArgsString
     */
    @Transient
    public String getFtpArgsString( ) {
        return "Range Marker " + ftpArgs;
    }


    public String getTransferId() {
        return transferId;
    }


    public String getFtpArgs() {
        return ftpArgs;
    }


    public String getCurrentFile() {
        return currentFile;
    }

    public void setTransferId( String transferId ) {
        this.transferId = transferId;
    }


    public void setFtpArgs( String ftpArgs ) {
        this.ftpArgs = ftpArgs;
    }


    public void setCurrentFile( String currentFile ) {
        this.currentFile = currentFile;
    }
}
