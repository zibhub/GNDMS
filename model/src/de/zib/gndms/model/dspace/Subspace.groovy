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

    @OneToMany( targetEntity=Slice.class, mappedBy="owner", cascade=[CascadeType.REFRESH,CascadeType.PERSIST, CascadeType.REMOVE ], fetch=FetchType.EAGER )
    Set<Slice> slices = new HashSet<Slice>();

    String path


    String gsiFtpPath

    /**
     * Sets the path of the Subspace to pth.
     *
     * If pth must exists and be a valid directory with read/write access.
     *
     * @note The read permission will be removed form pth.
     */
    public boolean setPath( String pth ) {

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
     * @brief Destroys a slice and removes its directory.
     *
     * @param sl The slice to remove.
     *
     * @return True if the destruction was successful.
     *   Reasons for failure might be:
     *       - Subspace doesn't own the slice.
     *       - Directory of the slice couldn't be removed.
     *
     * @note The subspace can only destroy its own slices.
     */
    public boolean destroySlice( Slice sl ) {

        if(! slices.contains( sl ) )
            return false 

        if ( deleteDirectory( getPathForSlice( sl ) ) ) {
            slices.remove( sl )
            return true
        }

        return false
    }

    /**
     * Little helper which delets a direcotry and its contents.
     *
     * @param pth The complete Path to the directory.
     * @return The success of the operation.
     */
    public static boolean deleteDirectory( String pth ) {

        File f = new File( pth )

        if( ! ( f.exists( ) && f.isDirectory( ) ) )
            return false

        try{
            String[] fl = f.list( )
            for( i in 0..<fl.length )  {
                File cf = new File( fl[i] )
                cf.delete( )
            }

            return f.delete( )

        } catch (SecurityException e) {
            return false
        }
    }

    /** 
     * @brief Delivers the absolute path to a slice sl.
     */
    public String getPathForSlice( Slice sl )  {
        path + File.separator + sl.getKind( ).getPermission().toString( ) + File.separator + sl.getAssociatedPath( )
    }


    public String getGsiFtpPathForSlice( Slice sl )  {
        gsiFtpPath + "/" + sl.getKind( ).getPermission().toString( ) + "/" + sl.getAssociatedPath( )
    }
}
