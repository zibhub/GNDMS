package de.zib.gndms.model.dspace

import de.zib.gndms.model.common.GridResource

import javax.persistence.Id
import javax.persistence.Embedded
import javax.persistence.AttributeOverrides
import javax.persistence.AttributeOverride
import javax.persistence.Column
import java.io.File
import javax.persistence.ManyToOne
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.CascadeType
import javax.persistence.JoinColumn
import javax.persistence.Table
import de.zib.gndms.model.common.TimedGridResource
import javax.persistence.OneToMany

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 06.08.2008, Time: 16:26:37
 */
@Entity(name="Slices")
@Table(name="slices", schema="dspace")
public class Slice extends TimedGridResource {

    @Id @Column( name="directory_id", nullable=false, updatable=false, columnDefinition="CHAR", length=36 )
    private String directoryId

    @ManyToOne( targetEntity=SliceKind.class, fetch=FetchType.LAZY, cascade=[CascadeType.REFRESH] )
    @JoinColumn( name="kind_uri", nullable=false, referencedColumnName="uri", updatable=false )
    private SliceKind kind

    @ManyToOne( targetEntity=Subspace.class, fetch=FetchType.LAZY, cascade=[CascadeType.REFRESH,CascadeType.PERSIST] )
	@JoinColumn( name="subspace_id", nullable=false, referencedColumnName="id", updatable=false )
    private Subspace owner

    @Embedded
    @AttributeOverrides([
        @AttributeOverride( name="unit", column=@Column(name="storage_unit") ),
        @AttributeOverride( name="amount", column=@Column(name="storage_amount") ) 
    ])
    private StorageSize totalStorageSize

    protected Slice( ) { }

     Slice( String didParam, SliceKind kndParam, Subspace ownParam ) {
        directoryId = didParam
        kind = kndParam
        owner = ownParam
    }



    String[] getFileListing( ) {
        
        File f = new File ( owner.getPathForSlice( this ) )
        f.list( )
    }


    /** 
    * @brief Delivers the path associated with this slice.
    *
    * This path is relative to the path of the Owner.
    *
    * To obtain the absolute path use Subspace.getPathForSlice .
    */ 
    public String getAssociatedPath( ) {

        // TODO Later this should return a directory name mapped to
        // the UId
        directoryId
    }
}
