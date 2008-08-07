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
import de.zib.gndms.model.util.LifecycleEventDispatcher
import javax.persistence.OneToMany
import org.apache.axis.components.uuid.UUIDGen
import org.apache.axis.components.uuid.UUIDGenFactory
import java.nio.channels.FileChannel
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

    @OneToMany( targetEntity=Slice.class, mappedBy="owner", fetch=FetchType.LAZY, cascade=[CascadeType.REFRESH,CascadeType.PERSIST, CascadeType.REMOVE ] )
    // TODO add primary key join constraint if needed 
    Set<Slice> slices

    String path


    /**
     * Sets the path of the Subspace to pth.
     *
     * If pth doesn't exist it will be created.
     *
     * @note The read permission will be removed form pth.
     */
    def public boolean setPath( String pth ) {
        File f = new File( pth )

        try {
            // this also creats the dir for the namespace if it
            // doesn't exist yet.
            f.mkdirs( )
            Runtime.getRuntime().exec( "chmod 300 " +f.getAbsolutePath( ) )
        } catch (SecurityException e) {
            return false
        }

        return true
    }

    /**
    * @brief creats a new Slice in this subspace.
    * 
    * It also creates a directory which is associated with 
    * the created slice.
    *
    * @param knd The kind of the new slice
    * 
    * @returns The new slice, or null if sth went wrong.
    */
    def public Slice createSlice( SliceKind knd ) {

        if( ! getMetaSubspace( ).getCreatableSliceKinds( ).contains( knd ) )
            return null 

        String lp = path +  File.separator + knd.getMode( ).toString( ) + File.separator
        File f
        String did
        UUIDGen uuidgen = UUIDGenFactory.getUUIDGen()
        
        while ( f != null && f.exists() ) {
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

        // fix permissions
        String m="400" // default read only
        if( knd.getMode( ).equals( SliceKindMode.RW ) )
            m="600"

        Runtime.getRuntime().exec( "chmod " +m+ " " +f.getAbsolutePath( ) )


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
    def public boolean DestroySlice( Slice sl ) {

        if(! slices.contains( sl ) )
            return false 

        if ( deleteDirectory( getPathForSlice( sl ) ) ) {
            slices.remove( sl )
            return true
        }

        return false
    }



    /** 
     * @brief Converts a slice to another slice kind and addes it to
     * another subspace.
     *
     * @param sl The source slice.
     * @param knd The kind for the new slice.
     * @param tgt The target subspace must be unequal to this.
     *
     * @return true if the conversion was successful.
     * @note Maybe this should be done in a separate thread
     */
    def public boolean convertSlice( Slice sl, SliceKind knd, Subspace tgt ) {

        if ( this == tgt )
            return false

        if(! getMetaSubspace( ).getCreatableSliceKinds( ).contains( knd ) )
            return false 

        if(! slices.contains( sl ) )
            return false

        String uid = sl.getUId( )
        String pth = tgt.path + File.separator + knd.getMode( ).toString( ) + File.separator + sl.getAssociatedPath( )
        String cpth = getPathForSlice( sl )

        // create dir for new slice
        File f = new File ( pth + File.separator + uid )
        if( f.exists( ) )
            return false
        else {
            try {
                f.mkdirs( )
            } catch (SecurityException e) {
                return false
            }
        }

        // copy contents of old slice to the new one
        String[] ls = sl.getFileListing( )

        boolean suc = true
        for( i in 0..<ls.length ) {
            suc = suc && copyFile( cpth + File.separator + ls[i], pth + File.separator + ls[i] )
        }

        if( ! suc )
            return false

        // add the new slice to the target subspace
        Slice nsl = new Slice( uid, knd, tgt )
        tgt.slices.add( nsl )

        return true
    }

    

    /** 
     * @brief Delivers the absolute path to a slice sl.
     */
    def public String getPathForSlice( Slice sl )  {
        path + sl.getKind( ).getMode().toString( ) + sl.getAssociatedPath( ) 
    }


    def public static boolean copyFile( String src, String tgt )  {

        File sf = new File( src )
        File tf = new File( tgt )

        FileChannel inc;
        FileChannel outc;
        try {
            inc = new FileInputStream( sf ).getChannel( )
            outc = new FileOutputStream( tf ).getChannel( )
            inc.transferTo( 0, inc.size(), outc )
        } catch (IOException e) {
            return false
        } finally {
            if ( inc != null )
                inc.close( )
            if ( outc != null )
                outc.close( )
        }

        return true 
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
