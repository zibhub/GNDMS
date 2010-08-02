package de.zib.gndms.model.dspace;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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



import de.zib.gndms.model.common.TimedGridResource;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author: try ma ik jo rr a zib
 * @version: $Id$
 *
 * User: mjorra, Date: 06.08.2008, Time: 16:26:37
 */
@Entity(name="Slices")
@Table(name="slices", schema="dspace")
//@MappedSuperclass
public class Slice extends TimedGridResource {
 
    private String directoryId;

    private SliceKind kind;

    private Subspace subspace;

    private String owner;

    private long totalStorageSize;

    protected Slice( ) { }

    public Slice ( String idParam, Calendar ttParam, String didParam, SliceKind kndParam, Subspace subsParam, String ownParam, long tssParam ) {
        super( );
        setId( idParam );
        setTerminationTime ( ttParam );
        directoryId = didParam;
        kind = kndParam;
        subspace = subsParam;
        owner = ownParam;
        totalStorageSize = tssParam;
    }

    
    public Slice ( String didParam, SliceKind kndParam, Subspace subsParam, String ownParam ) {
        super( );
        directoryId = didParam;
        kind = kndParam;
        subspace = subsParam;
        owner = ownParam;
    }


    /**
     * @brief Delivers the path associated with this slice.
     *
     * This path is relative to the path of the Owner.
     *
     * To obtain the absolute path use Subspace.getPathForSlice .
     */ 
    @Column( name="directory_id", nullable=false, updatable=false, columnDefinition="CHAR", length=36 )
    public String getDirectoryId() {
        return directoryId;
    }


    public void setDirectoryId( String directoryId ) {
        this.directoryId = directoryId;
    }


    @ManyToOne( targetEntity=SliceKind.class, cascade={CascadeType.REFRESH}, fetch=FetchType.EAGER )
    @JoinColumn( name="kind_uri", nullable=false, referencedColumnName="uri", updatable=false )
    public SliceKind getKind() {
        return kind;
    }


    public void setKind( SliceKind kind ) {
        this.kind = kind;
    }


    @ManyToOne( targetEntity=Subspace.class, cascade={CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER )
    @JoinColumn( name="subspace_id", nullable=false, referencedColumnName="id", updatable=false )
    public Subspace getSubspace() {
        return subspace;
    }


    public void setSubspace( Subspace subspace ) {
        this.subspace = subspace;
    }


    @Column(name="owner", nullable=false, updatable=false)
    public String getOwner() {
        return owner;
    }


    public void setOwner( String owner ) {
        this.owner = owner;
    }


    @Column(name="total_size", nullable=false, updatable=false)
    public long getTotalStorageSize() {
        return totalStorageSize;
    }


    public void setTotalStorageSize( long totalStorageSize ) {
        this.totalStorageSize = totalStorageSize;
    }
}
