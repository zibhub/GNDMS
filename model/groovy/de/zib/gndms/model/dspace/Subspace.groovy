/**
 * Subspace model class
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 24.07.2008 Time: 11:17:22
 */
package de.zib.gndms.model.dspace

import de.zib.gndms.model.common.TimedGridResource
import javax.persistence.OneToMany
import de.zib.gndms.model.dspace.types.SliceKindMode
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Embedded
import javax.persistence.AttributeOverrides
import javax.persistence.AttributeOverride
import javax.persistence.OneToOne
import javax.persistence.PrimaryKeyJoinColumns
import javax.persistence.FetchType
import javax.persistence.CascadeType
import javax.persistence.PrimaryKeyJoinColumn
import javax.persistence.EntityListeners
import javax.persistence.Column
import javax.persistence.Transient
import de.zib.gndms.model.common.ModelUUIDGen
import org.jetbrains.annotations.NotNull
import de.zib.gndms.model.common.GridResource
import javax.persistence.MappedSuperclass

/**
 *
 * Instances represent concrete subspaces in the local DSpace
 *
 */
@Entity(name="Subspaces")
@Table(name="subspaces", schema="dspace")
@MappedSuperclass
class Subspace extends GridResource {

    @Column(name="avail_size", nullable=false, updatable=false)
    long availableSize

    @Column(name="total_size", nullable=false, updatable=false)
	long totalSize

	@OneToOne(targetEntity=MetaSubspace.class, optional=false, cascade=[CascadeType.REFRESH], fetch=FetchType.EAGER)
	@PrimaryKeyJoinColumns([@PrimaryKeyJoinColumn(name="schema_uri"),
	                        @PrimaryKeyJoinColumn(name="specifier")])
	MetaSubspace metaSubspace

	@Embedded
	@AttributeOverrides([
		@AttributeOverride(name="gridSiteId", column=@Column(name="dspace_site", nullable=true, updatable=false)),
        @AttributeOverride(name="resourceKeyValue", column=@Column(name="dspace_uuid", nullable=false, updatable=false)),

    ])
	DSpaceRef dSpaceRef

    @OneToMany( targetEntity=Slice.class, mappedBy="subspace", cascade=[CascadeType.REFRESH,CascadeType.PERSIST, CascadeType.REMOVE ], fetch=FetchType.EAGER )
    Set<Slice> slices = new HashSet<Slice>();

    String path


    String gsiFtpPath

    public DSpaceRef getDSpaceRef() { return dSpaceRef }
    public void setDSpaceRef(newRef) { dSpaceRef = newRef }

    /**
     * Sets the path of the Subspace to pth.
     *
     * If pth must exists and be a valid directory with read/write access.
     *
     * @note The read permission will be removed form pth.
     */
    public void setPath( String pth ) {

        File f = new File( pth )

        if( ! ( f.exists( ) && f.isDirectory( ) ) )
            throw new IllegalStateException( pth+" does not exists or isn't a directory" )

        if( ! f.canWrite( ) )
            throw new IllegalStateException( pth+" is not writable" )

        path = pth;
    }


    public void addSlice( @NotNull Slice sl ) {

        if( slices == null )
            throw IllegalStateException( "No slices set provided" )

        slices.add( sl )
    }


    /**
     * Remove a slice from the slice set.
     *
     * The slice itself isn't destroy, and the fold stil exists.
     */
    public void removeSlice( @NotNull Slice sl ) {

        slices.remove( sl )
    }


    /** 
     * @brief Delivers the absolute path to a slice sl.
     */
    public String getPathForSlice( Slice sl )  {
        path + File.separator + sl.getKind( ).getSliceDirectory() + File.separator + sl.getAssociatedPath( )
    }


    public String getGsiFtpPathForSlice( Slice sl )  {
        gsiFtpPath + "/" + sl.getKind( ).getSliceDirectory() + "/" + sl.getAssociatedPath( )
    }
}
