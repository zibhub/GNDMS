/**
 * Subspace model class
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 24.07.2008 Time: 11:17:22
 */
package de.zib.gndms.model.dspace

import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.AttributeOverrides
import javax.persistence.AttributeOverride
import de.zib.gndms.model.common.TimedGridResource
import javax.persistence.FetchType
import javax.persistence.OneToOne
import javax.persistence.CascadeType
import javax.persistence.PrimaryKeyJoinColumns
import javax.persistence.PrimaryKeyJoinColumn
import javax.persistence.EntityListeners
import de.zib.gndms.model.util.LifecycleEventDispatcher
import javax.persistence.OneToMany
import org.apache.axis.components.uuid.UUIDGen
import org.apache.axis.components.uuid.UUIDGenFactory

/**
 *
 * Instances represent concrete subspaces in the local DSpace
 *
 */
@Entity(name="Subspaces") @EntityListeners([LifecycleEventDispatcher.class])
@Table(name="subspaces", schema="dspace")
class Subspace extends TimedGridResource {
	@Embedded
	@AttributeOverrides([
	      @AttributeOverride(name="unit", column=@Column(name="avail_unit", nullable=false)),
		  @AttributeOverride(name="amount", column=@Column(name="avail_amount", nullable=false))
	])
	StorageSize availableSize

	@Embedded
	@AttributeOverrides([
	      @AttributeOverride(name="unit", column=@Column(name="total_unit", nullable=false)),
		  @AttributeOverride(name="amount", column=@Column(name="total_amount", nullable=false))
	])
	StorageSize totalSize

	@OneToOne(targetEntity=MetaSubspace.class, optional=false, fetch=FetchType.LAZY, cascade=[CascadeType.REFRESH])
	@PrimaryKeyJoinColumns([@PrimaryKeyJoinColumn(name="schema_uri"),
	                        @PrimaryKeyJoinColumn(name="specifier")])
	MetaSubspace metaSubspace

	@Embedded
	@AttributeOverrides([
		@AttributeOverride(name="gridSiteId", column=@Column(name="dspace_site", nullable=true, updatable=false)),
	    @AttributeOverride(name="resourceKeyValue", column=@Column(name="dspace_uuid", nullable=false, updatable=false))])
	DSpaceRef dSpaceRef

    @OneToMany( targetEntity=Slice.class, mappedBy="uid", fetch=FetchType.LAZY, cascade=[CascadeType.REFRESH,CascadeType.PERSIST, CascaedType.REMOVE ] )
    // TODO add primary key join constraint
    Set<Slice> slices

    String path


    /** 
    * @brief creats a new Slice in this subspace.
    * 
    * Slice creation also creates a directory which is associated with 
    * The slice.
    *
    * @param knd The kind of the new slice
    * 
    * @returns The new slice, or null if sth went wrong.
    */
    def public static Slice createSlice( SliceKind knd ) {

        if( ! getMetaSubspace( ).getCreateableSliceKinds( ).contains( knd ) )
            return null 

        String lp = path +  "/" + knd.getMode( ).toString( ) + "/"
        File f
        String did
        while ( f.exists() ) {
            UUIDGen uuidgen = UUIDGenFactory.getUUIDGen()
            did = uuidgen.nextUUID()
            f = new File( lp + did )
        }

        try {
            // this also creats the dir for the namespace if it
            // doesn't exist yet.
            f.mkdirs( )
        } catch (SecurityException e) {
            return null
        }

        Slice sl = new Slice( did, knd )
        slices.add( sl )

        return  sl
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
    def public static boolean DestroySlice( Slice sl ) {

        if(! slices.contains( sl ) )
            return false 

        if ( deleteDirectory( getPathForSlice( sl ) ) ) {
            slices.remove( sl )
            return true
        }

        return false
    }


    /** 
     * @brief Delivers the absolute path to a slice sl.
     */
    def public String getPathForSlice( Slice sl )  {
        path + sl.getSliceKind( ).getMode().toString( ) + sl.getAssociatedPath( ) 
    }

    /**
     * Little helper which delets a direcotry and its contents.
     *
     * @param pth The complete Path to the directory.
     * @return The success of the operation.
     */
    def private static boolean deleteDirectory( String pth ) {

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

        return false
    }
}
}
