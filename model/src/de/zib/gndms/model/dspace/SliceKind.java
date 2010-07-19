package de.zib.gndms.model.dspace

import de.zib.gndms.model.ModelEntity
import javax.persistence.Id
import javax.persistence.Column
import javax.persistence.Enumerated
import javax.persistence.EnumType
import javax.persistence.Inheritance
import javax.persistence.Table
import javax.persistence.DiscriminatorType
import javax.persistence.DiscriminatorColumn
import javax.persistence.ManyToMany
import javax.persistence.FetchType
import javax.persistence.CascadeType
//import de.zib.gndms.model.dspace.types.SliceKindMode
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.persistence.Embeddable
import de.zib.gndms.model.common.GridEntity
import javax.persistence.MappedSuperclass
import de.zib.gndms.model.common.AccessMask

/**
 * SliceKinds are identified by a kindURI
 * 
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 01.08.2008 Time: 16:17:45
 */
@Entity(name="SliceKinds")
// @Inheritance
// @DiscriminatorColumn(name="class", discriminatorType=DiscriminatorType.STRING, length=8)
// @DiscriminatorValue("PLAIN")
// @Table(name="slice_kinds", schema="dspace")
@MappedSuperclass
class SliceKind extends GridEntity {
	@Id @Column(name="uri", nullable=false, updatable=false, columnDefinition="VARCHAR")
    String URI

//    @Column(name="permission", nullable=false, updatable=false, columnDefinition="VARCHAR", length=15)
    @Column(name="permission", nullable=false, updatable=false )
    AccessMask permission

	//de.zib.gndms.model.dspace.types.SliceKindMode mode

    @Column( name="slice_directory", nullable=false, columnDefinition="VARCHAR" )
    String sliceDirectory

	@ManyToMany(mappedBy="creatableSliceKinds", cascade=[CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST], fetch=FetchType.EAGER)
	Set<MetaSubspace> metaSubspaces = new HashSet<MetaSubspace>();

/* void setURI( String uriParam ){
        this.uri = uriParam
    }

    void getURI( ){
        this.uri
    }
    */
}
