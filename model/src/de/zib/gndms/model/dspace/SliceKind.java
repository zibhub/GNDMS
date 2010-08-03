package de.zib.gndms.model.dspace;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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



import de.zib.gndms.model.common.AccessMask;
import de.zib.gndms.model.common.GridEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
public class SliceKind extends GridEntity {
    private String URI;

//    @Column(name="permission", nullable=false, updatable=false, columnDefinition="VARCHAR", length=15)
    private AccessMask permission;

    //de.zib.gndms.model.dspace.types.SliceKindMode mode

    private String sliceDirectory;

    private Set<MetaSubspace> metaSubspaces = new HashSet<MetaSubspace>();


    @Id @Column(name="uri", nullable=false, updatable=false, columnDefinition="VARCHAR")
    public String getURI() {
        return URI;
    }


    @Column(name="permission", nullable=false, updatable=false )
    public AccessMask getPermission() {
        return permission;
    }


    @Column( name="slice_directory", nullable=false, columnDefinition="VARCHAR" )
    public String getSliceDirectory() {
        return sliceDirectory;
    }


    @ManyToMany(mappedBy="creatableSliceKinds", cascade={CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST}, fetch=FetchType.EAGER)
    public Set<MetaSubspace> getMetaSubspaces() {
        return metaSubspaces;
    }


    public void setURI( String URI ) {
        this.URI = URI;
    }


    public void setPermission( AccessMask permission ) {
        this.permission = permission;
    }


    public void setSliceDirectory( String sliceDirectory ) {
        this.sliceDirectory = sliceDirectory;
    }


    public void setMetaSubspaces( Set<MetaSubspace> metaSubspaces ) {
        this.metaSubspaces = metaSubspaces;
    }
}
