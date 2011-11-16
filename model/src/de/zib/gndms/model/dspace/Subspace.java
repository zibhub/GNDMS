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

import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.common.ImmutableScopedName;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;

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
	 * The logger.
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * The still available size in this subspace.
	 */
    private long availableSize;

    /**
     * The total size of the subspace.
     */
    private long totalSize;

    /**
     * The set of slices of this subspace. 
     */
    private Set<Slice> slices = new HashSet<Slice>();

    /**
     * The absolute path of this subspace.
     */
    private String path;

    /**
     * The gsiftp path of this subspace.
     */
    private String gsiFtpPath;

    /**
     * The name / id of this subspace.
     */
    private ImmutableScopedName name;

    /**
     * If this subspace is publicly visible.
     */
    private boolean visibleToPublic;

    /**
     * The set of slice kinds which are allowed in this subspace.
     */
    private Set<SliceKind> creatableSliceKinds = new HashSet<SliceKind>();

    /**
     * Sets the path of this subspace.
     *
     * path must exist and be a valid directory with read/write access.
     * @param path  The path.
     *
     * @note The read permission will be removed from path.
     */
    public final void setPath(final String path) {
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
     * Adds a slice to the subspace's slice set.
     * @param sl The slice.
     */
    public final void addSlice(@NotNull final Slice sl) {
        if (slices == null) {
            logger.warn( "No slice set provided for subspace " + name + ". It is newly created." );
            slices = new HashSet<Slice>();
        }
        slices.add(sl);
    }

    /**
     * Removes a slice from the subspace's slice set.
     *
     * The slice itself isn't destroy, and the folder still exists.
     * @param sl The slice to be removed.
     */
    public final void removeSlice(@NotNull final Slice sl) {
        slices.remove(sl);
    }

    /** 
     * @brief Returns the absolute path to a slice.
     * 
     * @param sl The slice. 
     * @return The path.
     */
    public final String getPathForSlice(final Slice sl)  {
        return path + File.separator + sl.getKind( ).getSliceDirectory() + File.separator + sl.getDirectoryId();
    }

    /**
     * Returns the gsi ftp path to a slice.
     * @param sl The slice.
     * @return The gsi ftp path.
     */
    public final String getGsiFtpPathForSlice(final Slice sl)  {
        return gsiFtpPath + "/" + sl.getKind( ).getSliceDirectory() + "/" + sl.getDirectoryId( );
    }

    /**
     * Returns the available size of this subspace.
     * @return The available size.
     */
    @Column(name = "avail_size", nullable = false, updatable = false)
    public final long getAvailableSize() {
        return availableSize;
    }

    /**
     * Returns the total size of this subspace.
     * @return The total size
     */
    @Column(name = "total_size", nullable = false, updatable = false)
    public final long getTotalSize() {
        return totalSize;
    }

    /**
     * Returns the set of slices of this subspace.
     * @return The set of slices.
     */
    @OneToMany(targetEntity = Slice.class, mappedBy = "subspace", 
    		cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE }, fetch = FetchType.EAGER)
    public final Set<Slice> getSlices() {
        return slices;
    }

    /**
     * Returns the absolute path of this subspace.
     * @return The path.
     */
    @Column(name = "path", nullable = false, updatable = true)
    public final String getPath() {
        return path;
    }

    /**
     * Returns the gsi ftp path of this subspace.
     * @return The gsi ftp path.
     */
    public final String getGsiFtpPath() {
        return gsiFtpPath;
    }

    /**
     * Sets the available size of this subspace.
     * @param availableSize The size to set.
     */
    public final void setAvailableSize(final long availableSize) {
        this.availableSize = availableSize;
    }

    /**
     * Sets the total size of this subspace.
     * @param totalSize The size to set.
     */
    public final void setTotalSize(final long totalSize) {
        this.totalSize = totalSize;
    }

    /**
     * Sets the set of slices for this subspace.
     * @param slices The slices.
     */
    public final void setSlices(final Set<Slice> slices) {
        this.slices = slices;
    }
    
    /**
     * Sets the gsi ftp path for this subspace.
     * @param gsiFtpPath The path to set.
     */
    public final void setGsiFtpPath(final String gsiFtpPath) {
        this.gsiFtpPath = gsiFtpPath;
    }	
    
    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name="nameScope", column=@Column(name="schema_uri", nullable=false, updatable=false, columnDefinition="VARCHAR")),
        @AttributeOverride(name="localName", column=@Column(name="specifier", nullable=false, updatable=false, columnDefinition="VARCHAR", length=64))
        })
    public ImmutableScopedName getName() {
        return name;
    }
    
    public void setName( ImmutableScopedName name ) {
        this.name = name;
    }

    @Column(name="visible", nullable=false, updatable=false)
    public boolean isVisibleToPublic() {
        return visibleToPublic;
    }

    public void setVisibleToPublic( boolean visibleToPublic ) {
        this.visibleToPublic = visibleToPublic;
    }

    @ManyToMany(targetEntity=SliceKind.class, cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST}, fetch=FetchType.EAGER)
   @JoinTable(name = "creatable_slice_kinds", schema="dspace",
        uniqueConstraints={@UniqueConstraint(columnNames = {"subspace_schema_uri", "subspace_specifier", "slice_kind_uri"})},
        joinColumns={@JoinColumn(name="subspace_schema_uri", referencedColumnName="schema_uri", columnDefinition="VARCHAR", nullable=false, updatable=true),
            @JoinColumn(name="subspace_specifier", referencedColumnName="specifier", columnDefinition="VARCHAR", nullable=false, updatable=true)},
        inverseJoinColumns={@JoinColumn(name="slice_kind_uri", referencedColumnName="uri", columnDefinition="VARCHAR", nullable=false, updatable=true)})
    
    public Set<SliceKind> getCreatableSliceKinds() {
        return creatableSliceKinds;
    }


    public void setCreatableSliceKinds( Set<SliceKind> creatableSliceKinds ) {
        this.creatableSliceKinds = creatableSliceKinds;
    }

}
