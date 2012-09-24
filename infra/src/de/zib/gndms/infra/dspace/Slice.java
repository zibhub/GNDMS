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

import de.zib.gndms.common.dspace.service.SliceInformation;
import de.zib.gndms.infra.grams.LinuxDirectoryAux;
import de.zib.gndms.kit.util.DirectoryAux;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import org.joda.time.DateTime;

/**
 * @date: 13.08.12
 * @time: 11:54
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class Slice extends de.zib.gndms.model.dspace.Slice {

    private DirectoryAux directoryAux;

    protected Slice( ) {
        directoryAux = new LinuxDirectoryAux();
    }


    public Slice ( String idParam, DateTime ttParam, String didParam, SliceKind kndParam,
                   Subspace subsParam, String ownParam, long tssParam ) {
        super( idParam, ttParam, didParam, kndParam, subsParam, ownParam, tssParam );

        directoryAux = new LinuxDirectoryAux();
    }


    public Slice ( String didParam, SliceKind kndParam, Subspace subsParam, String ownParam ) {
        super( didParam, kndParam,  subsParam,  ownParam );

        directoryAux = new LinuxDirectoryAux();
    }
    
    
    public  Slice( de.zib.gndms.model.dspace.Slice sliceModel ) {
        super(
                sliceModel.getId(),
                sliceModel.getTerminationTime(),
                sliceModel.getDirectoryId(),
                sliceModel.getKind(),
                sliceModel.getSubspace(),
                sliceModel.getOwner(),
                sliceModel.getTotalStorageSize() );
        setGroup( sliceModel.getGroup() );
        
        directoryAux = new LinuxDirectoryAux();
    }


    public long getDiskUsage() {
        final String directory = getSubspace().getPathForSlice( this );

        return getDirectoryAux().diskUsage(getOwner(), directory);

    }


    public DirectoryAux getDirectoryAux() {
        return directoryAux;
    }


    public void setDirectoryAux( DirectoryAux directoryAux ) {
        this.directoryAux = directoryAux;
    }


    public SliceInformation getSliceInformation() {
        SliceInformation info = new SliceInformation();

        info.setSize( getTotalStorageSize() );
        info.setTerminationTime( getTerminationTime() );
        info.setOwner( getOwner() );
        info.setGroup( getGroup() );
        info.setDiskUsage( getDiskUsage() );

        return info;
    }
}
