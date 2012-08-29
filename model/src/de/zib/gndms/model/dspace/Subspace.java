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



/**
 * Subspace model class
 * 
 * @author  try ste fan pla nti kow zib
 * @version $Id$ 
 *
 * User: stepn Date: 24.07.2008 Time: 11:17:22
 */

import de.zib.gndms.common.dspace.SubspaceConfiguration;
import de.zib.gndms.model.common.GridResource;

import javax.persistence.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * Instances represent concrete subspaces in the local DSpace.
 *
 */
@Entity(name = "Subspaces")
@Table(name = "subspaces", schema = "dspace")
//@MappedSuperclass
public class Subspace extends GridResource {

	/**
	 * The still available size in this subspace.
	 */
    private long availableSize;

    /**
     * The total size of the subspace.
     */
    private long totalSize;

    /**
     * The absolute path of this subspace.
     */
    private String path;

    /**
     * The gsiftp path of this subspace.
     */
    private String gsiFtpPath;

    /**
     * If this subspace is publicly visible.
     */
    private boolean visibleToPublic;

    /**
     * The set of slice kinds which are allowed in this subspace.
     */
    private Set< SliceKind > creatableSliceKinds = new HashSet<SliceKind>();

    /**
     * Sets the path of this subspace.
     *
     * path must exist and be a valid directory with read/write access.
     * @param path  The path.
     *
     * @note The read permission will be removed from path.
     */
    public void setPath(final String path) {
    	// TODO this test seemed to be useful?
        /*
        File f = new File( pth );

        if( ! ( f.exists( ) && f.isDirectory( ) ) )
            throw new IllegalStateException( pth+" does not exists or isn't a directory" );

        if( ! f.canWrite( ) )
            throw new IllegalStateException( pth+" is not writable" );
            */

//        if( pth == null )
//            throw new IllegalStateException( "path must not be null" );
        this.path = path;
    }

    /** 
     * @brief Returns the absolute path to a slice.
     * 
     * @param sl The slice. 
     * @return The path.
     */
    public String getPathForSlice(final Slice sl)  {
        return getPath() + File.separator + sl.getKind( ).getSliceDirectory() + File.separator + sl.getDirectoryId();
    }

    /**
     * Returns the gsi ftp path to a slice.
     * @param sl The slice.
     * @return The gsi ftp path.
     */
    public String getGsiFtpPathForSlice(final Slice sl)  {
        return getGsiFtpPath() + "/" + sl.getKind( ).getSliceDirectory() + "/" + sl.getDirectoryId( );
    }

    /**
     * Returns the available size of this subspace.
     * @return The available size.
     */
    @Column(name = "avail_size", nullable = false, updatable = false)
    public long getAvailableSize() {
        return availableSize;
    }

    /**
     * Returns the total size of this subspace.
     * @return The total size
     */
    @Column(name = "total_size", nullable = false, updatable = false)
    public long getTotalSize() {
        return totalSize;
    }

    /**
     * Returns the absolute path of this subspace.
     * @return The path.
     */
    @Column(name = "path", nullable = false, updatable = true)
    public String getPath() {
        return path;
    }

    /**
     * Returns the gsi ftp path of this subspace.
     * @return The gsi ftp path.
     */
    public String getGsiFtpPath() {
        return gsiFtpPath;
    }

    /**
     * Sets the available size of this subspace.
     * @param availableSize The size to set.
     */
    public void setAvailableSize(final long availableSize) {
        this.availableSize = availableSize;
    }

    /**
     * Sets the total size of this subspace.
     * @param totalSize The size to set.
     */
    public void setTotalSize(final long totalSize) {
        this.totalSize = totalSize;
    }
    
    /**
     * Sets the gsi ftp path for this subspace.
     * @param gsiFtpPath The path to set.
     */
    public void setGsiFtpPath(final String gsiFtpPath) {
        this.gsiFtpPath = gsiFtpPath;
    }	
    
	/**
	 * Returns the visibility of this subspace.
	 * @return The visibility.
	 */
    @Column(name = "visible", nullable = false, updatable = false)
    public boolean isVisibleToPublic() {
        return visibleToPublic;
    }

	/**
	 * Sets the visibility of this subspace.
	 * @param visibleToPublic The visibility.
	 */
    public void setVisibleToPublic(final boolean visibleToPublic) {
        this.visibleToPublic = visibleToPublic;
    }

    /**
     * Returns the slice kinds of this subspace.
     * @return The slice kinds.
     */
    @OneToMany( cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH} )
    /*@JoinTable(name = "creatable_slice_kinds", schema = "dspace",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"subspace_schema_uri", 
        		"subspace_specifier", "slice_kind_uri" }) },
        joinColumns = {@JoinColumn(name = "subspace_schema_uri", 
        		referencedColumnName = "schema_uri", columnDefinition = "VARCHAR", nullable = false, updatable = true),
    @JoinColumn(name = "subspace_specifier", referencedColumnName = "specifier", 
    			columnDefinition = "VARCHAR", nullable = false, updatable = true) },
        		inverseJoinColumns = {@JoinColumn(name = "slice_kind_uri", 
        		referencedColumnName = "uri", columnDefinition = "VARCHAR", nullable = false,
        		updatable = true) }) */
    public Set<SliceKind> getCreatableSliceKinds() {
        return creatableSliceKinds;
    }


    /**
     * Sets the slice kinds of this subspace.
     * @param creatableSliceKinds The slice kinds.
     */
    public void setCreatableSliceKinds(final Set<SliceKind> creatableSliceKinds) {
        this.creatableSliceKinds = creatableSliceKinds;
    }


    public SubspaceConfiguration getConfiguration() {
        return new SubspaceConfiguration(
                getPath(),
                getGsiFtpPath(),
                isVisibleToPublic(),
                getTotalSize(),
                "READ",
                getId()
        );
    }
    
    
    public void setConfiguration( final SubspaceConfiguration configuration ) {
        if( configuration.getPath() != null )
            setPath( configuration.getPath() );
        if( configuration.getGsiFtpPath() != null )
            setGsiFtpPath( configuration.getGsiFtpPath() );
        if( configuration.isVisible() != null )
            setVisibleToPublic( configuration.isVisible() );
        if( configuration.getSize() != null )
            setTotalSize( configuration.getSize() );
    }
}
