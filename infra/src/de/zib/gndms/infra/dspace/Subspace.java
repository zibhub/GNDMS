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

package de.zib.gndms.infra.dspace;

import de.zib.gndms.common.dspace.service.SubspaceInformation;
import de.zib.gndms.infra.grams.LinuxDirectoryAux;
import de.zib.gndms.kit.util.DirectoryAux;

/**
 * @date: 29.08.12
 * @time: 11:52
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class Subspace extends de.zib.gndms.model.dspace.Subspace {

    private DirectoryAux directoryAux;
    
    
    public Subspace( de.zib.gndms.model.dspace.Subspace subspaceModel ) {
        setPath( subspaceModel.getPath() );
        setGsiFtpPath( subspaceModel.getGsiFtpPath() );
        setVisibleToPublic( subspaceModel.isVisibleToPublic() );
        setTotalSize( subspaceModel.getTotalSize() );
        setId( subspaceModel.getId() );
        setCreatableSliceKinds( subspaceModel.getCreatableSliceKinds() );

        directoryAux = new LinuxDirectoryAux();
    }


    public SubspaceInformation getInformation() {
        SubspaceInformation information = new SubspaceInformation(
                getPath(),
                getGsiFtpPath(),
                isVisibleToPublic(),
                getTotalSize(),
                "READ",
                getId()
        );

        final long diskUsage = getDirectoryAux().diskUsage( "root", getPath() );
        information.setDiskUsage( diskUsage );
        information.setPotentialUsage( getAvailableSize() );

        return information;
    }


    public DirectoryAux getDirectoryAux() {
        return directoryAux;
    }


    public void setDirectoryAux( DirectoryAux directoryAux ) {
        this.directoryAux = directoryAux;
    }
}
