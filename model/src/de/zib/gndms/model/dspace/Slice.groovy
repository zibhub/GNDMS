package de.zib.gndms.model.dspace

import de.zib.gndms.model.common.GridResource

import javax.persistence.Id
import javax.persistence.Embedded
import javax.persistence.Column
import java.io.File
import javax.persistence.ManyToOne
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.CascadeType

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 06.08.2008, Time: 16:26:37
 */
@Entity(name="Slices")
public class Slice extends GridResource{

    @Id @Column( name="uid", nullable=false, updatable=false, columnDefinition="CHAR", length=36 )
    private String UId

    @Embedded
    private SliceKind Kind

	@ManyToOne( targetEntity=Subspace.class, fetch=FetchType.LAZY, cascade=[CascadeType.REFRESH,CascadeType.PERSIST] )
    // TODO Add join key constrain
    private Subspace Owner


    protected Slice( ) { }

    public Slice( String uid, SliceKind knd, Subspace own ) {
        UID = uid
        Kind = knd
        Owner = own
    }


    def public String[] getFileListing( ) {
        
        File f = new File ( Owner.getPathForSlice( this ) )
        f.list( )
    }


    /** 
    * @brief Delivers the path associated with this slice.
    *
    * This path is relative to the path of the Owner.
    *
    * To obtain the absolute path use Subspace.getPathForSlice .
    */ 
    def public String getAssociatedPath( ) {

        // TODO Later this should return a directory name mapped to
        // the UId
        UId
    }
}
