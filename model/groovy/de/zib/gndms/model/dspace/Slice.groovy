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
import javax.persistence.MappedSuperclass

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 06.08.2008, Time: 16:26:37
 */
@Entity(name="Slices")
@Table(name="slices", schema="dspace")
@MappedSuperclass
public class Slice extends TimedGridResource {
 
    @Column( name="directory_id", nullable=false, updatable=false, columnDefinition="CHAR", length=36 )
    private String directoryId

    @ManyToOne( targetEntity=SliceKind.class, cascade=[CascadeType.REFRESH], fetch=FetchType.EAGER )
    @JoinColumn( name="kind_uri", nullable=false, referencedColumnName="uri", updatable=false )
    SliceKind kind

    @ManyToOne( targetEntity=Subspace.class, cascade=[CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE], fetch=FetchType.EAGER )
	@JoinColumn( name="subspace_id", nullable=false, referencedColumnName="id", updatable=false )
    Subspace subspace

    @Column(name="owner", nullable=false, updatable=false)
    String owner

    @Column(name="total_size", nullable=false, updatable=false)
    long totalStorageSize

    protected Slice( ) { }

    Slice( String idParam, Calendar ttParam, String didParam, SliceKind kndParam, Subspace subsParam, String ownParam, long tssParam ) {
        super( )
        setId( idParam )
        setTerminationTime ( ttParam )
        directoryId = didParam
        kind = kndParam
        subspace = subsParam
        owner = ownParam
        totalStorageSize = tssParam
    }

    
    Slice( String didParam, SliceKind kndParam, Subspace subsParam, String ownParam ) {
        super( )
        directoryId = didParam
        kind = kndParam
        subspace = subsParam
        owner = ownParam
    }



    String[] getFileListing( ) {
        
        File f = new File ( subspace.getPathForSlice( this ) )
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

        directoryId
    }
}
