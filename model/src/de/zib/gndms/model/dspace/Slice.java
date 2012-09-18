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


import de.zib.gndms.common.dspace.SliceConfiguration;
import de.zib.gndms.common.dspace.service.SliceInformation;
import de.zib.gndms.model.common.TimedGridResource;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 *
 * User: mjorra, Date: 06.08.2008, Time: 16:26:37
 */
@Entity( name="Slices" )
@Table( name="slices", schema="dspace" )
@Inheritance( strategy = InheritanceType.JOINED )
//@MappedSuperclass
public class Slice extends TimedGridResource {
 
    private String directoryId;

    private SliceKind kind;

    private Subspace subspace;

    private String owner;
    
    private String group;

    private long totalStorageSize;

    private boolean published;

    protected Slice( ) { }

    public Slice ( String idParam, DateTime ttParam, String didParam, SliceKind kndParam,
                   Subspace subsParam, String ownParam, long tssParam ) {
        super( );
        setId( idParam );
        setTerminationTime ( ttParam );
        published = false;
        directoryId = didParam;
        kind = kndParam;
        subspace = subsParam;
        owner = ownParam;
        totalStorageSize = tssParam;
    }

    
    public Slice ( String didParam, SliceKind kndParam, Subspace subsParam, String ownParam ) {
        super( );
        published = false;
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
     * @return The path.
     */ 
    @Column( name="directory_id", nullable=false, updatable=false, columnDefinition="CHAR", length=36 )
    public String getDirectoryId() {
        return directoryId;
    }


    public void setDirectoryId( String directoryId ) {
        this.directoryId = directoryId;
    }


    @ManyToOne( targetEntity=SliceKind.class, cascade={CascadeType.REFRESH}, fetch=FetchType.EAGER )
 //   @JoinColumn( name="kind_uri", nullable=false, referencedColumnName="uri", updatable=false )
    public SliceKind getKind() {
        return kind;
    }


    public void setKind( SliceKind kind ) {
        this.kind = kind;
    }


    @ManyToOne( targetEntity=Subspace.class, cascade={CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER )
   // @JoinColumn( name="subspace_id", nullable=false, referencedColumnName="id", updatable=false )
    public Subspace getSubspace() {
        return subspace;
    }


    public void setSubspace( Subspace subspace ) {
        this.subspace = subspace;
    }


    @Column(name="owner", nullable=false, updatable=true)
    public String getOwner() {
        return owner;
    }


    public void setOwner( String owner ) {
        this.owner = owner;
    }


    @Column(name="group", nullable=true, updatable=true)
    public String getGroup() {
        return group;
    }


    public void setGroup( String group ) {
        this.group = group;
    }


    @Column(name="total_size", nullable=false, updatable=true)
    public long getTotalStorageSize() {
        return totalStorageSize;
    }


    public void setPublished( boolean published ) {
        this.published = published;
    }


    @Column( name="published", nullable=false, updatable=true )
    public boolean getPublished() {
        return published;
    }


    public void setTotalStorageSize( long totalStorageSize ) {
        this.totalStorageSize = totalStorageSize;
    }
    

    @Transient
    public void setConfiguration( SliceConfiguration sliceConfiguration ) {
        if( sliceConfiguration.getSize() != null )
            setTotalStorageSize( sliceConfiguration.getSize() );
        if( sliceConfiguration.getTerminationTime() != null )
            setTerminationTime( sliceConfiguration.getTerminationTime() );
    }


    @Transient
    public SliceConfiguration getConfiguration( ) {
        return new SliceConfiguration(
                getTotalStorageSize(),
                getTerminationTime() );
    }
}
