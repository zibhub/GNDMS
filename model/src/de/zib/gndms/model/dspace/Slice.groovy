package de.zib.gndms.model.dspace

import de.zib.gndms.model.common.GridResource

import javax.persistence.Id
import javax.persistence.Embedded
import javax.persistence.Column
import java.io.File

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 06.08.2008, Time: 16:26:37
 */
@Entity(name="SLICES")
public class Slice {

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
        
        File f = new File ( SliceManager.getPath( this, Owner ) )
        f.list( )
    }


    def public String getAssociatedPath( ) {

        // TODO Later this should return a directory name mapped to
        // the UId
        UId
    }
}
