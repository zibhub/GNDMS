/*
 * Copyright 2008-2012 Zuse Institute Berlin (ZIB)
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
 *
 */

package de.zib.gndms.common.dspace.service;

import de.zib.gndms.common.dspace.SubspaceConfiguration;
import de.zib.gndms.common.logic.config.SetupMode;

import java.io.Serializable;

/**
 * @date: 28.08.12
 * @time: 18:05
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class SubspaceInformation extends SubspaceConfiguration implements Serializable {

    private static final long serialVersionUID = -7527037415644965567L;

    private Long diskUsage;


    /**
     * Constructs a SubspaceConfiguration.
     * @param path The path.
     * @param gsiFtpPath The gsi ftp path.
     * @param visible The visibility.
     * @param size the size.
     * @param mode The setup mode.
     */
    public SubspaceInformation( final String path, final String gsiFtpPath,
                                 final boolean visible, final long size, final SetupMode mode, final String subspace ) {
        super( path, gsiFtpPath, visible, size, mode, subspace );
    }

    /**
     * Constructs a SubspaceConfiguration.
     * @param path The path.
     * @param gsiFtpPath The gsi ftp path.
     * @param visible The visibility.
     * @param size the size.
     * @param mode The setup mode.
     */
    public SubspaceInformation( final String path, final String gsiFtpPath,
                                 final boolean visible, final long size, final String mode, final String subspace ) {
        super( path, gsiFtpPath, visible, size, mode, subspace );
    }




    public Long getDiskUsage() {
        return diskUsage;
    }


    public void setDiskUsage( Long diskUsage ) {
        this.diskUsage = diskUsage;
    }
}
