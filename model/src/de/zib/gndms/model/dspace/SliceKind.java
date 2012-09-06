package de.zib.gndms.model.dspace;

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



import de.zib.gndms.common.model.common.AccessMask;
import de.zib.gndms.model.common.GridResource;

import javax.persistence.*;

/**
 * SliceKinds are identified by a kindURI
 * 
 * @author  try ste fan pla nti kow zib
 * @version $Id$ 
 *
 * User: stepn Date: 01.08.2008 Time: 16:17:45
 */
@Entity(name="SliceKinds")
// @Inheritance
// @DiscriminatorColumn(name="class", discriminatorType=DiscriminatorType.STRING, length=8)
// @DiscriminatorValue("PLAIN")
@Table(name="slice_kinds", schema="dspace")
//@MappedSuperclass
public class SliceKind extends GridResource {
    private AccessMask permission;

    private String sliceDirectory;
    private Subspace subspace;

    // default time to live of slice in milliseconds
    // if not given on creation, one day is taken as default
    private long defaultTimeToLive = 86400000;
    // default size of slice in bytes
    // if not given on creation, 10MB is taken as default
    private int defaultSliceSize = 1024*1024*10;

    @Column(name="permission", nullable=false, updatable=true )
    public AccessMask getPermission() {
        return permission;
    }

    @Column( name="slice_directory", nullable=false, columnDefinition="VARCHAR" )
    public String getSliceDirectory() {
        return sliceDirectory;
    }

    @ManyToOne( cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH} )
    public Subspace getSubspace() {
        return subspace;
    }

    public void setSubspace( final Subspace subspace ) {

        this.subspace = subspace;
    }

    public void setPermission( AccessMask permission ) {
        this.permission = permission;
    }

    public void setPermission( long permission ) {
        this.permission = AccessMask.fromString(Long.toString(permission));

    }

    public void setSliceDirectory( String sliceDirectory ) {
        this.sliceDirectory = sliceDirectory;
    }

    public long getDefaultTimeToLive() {
        return defaultTimeToLive;
    }

    public void setDefaultTimeToLive(long defaultTimeToLive) {
        this.defaultTimeToLive = defaultTimeToLive;
    }

    public int getDefaultSliceSize() {
        return defaultSliceSize;
    }

    public void setDefaultSliceSize(int defaultSliceSize) {
        this.defaultSliceSize = defaultSliceSize;
    }
}
