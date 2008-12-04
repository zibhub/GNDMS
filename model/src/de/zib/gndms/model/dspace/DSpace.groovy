package de.zib.gndms.model.dspace

import javax.persistence.*
import org.jetbrains.annotations.NotNull
import de.zib.gndms.model.common.SingletonGridResource

/**
 * Instances represent an installations' DSpace singleton resource on the database model side
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 01.08.2008 Time: 16:02:46
 */
@NamedQueries([
@NamedQuery(name="findDSpaceInstances", query="SELECT instance FROM DSpaces instance"),
@NamedQuery(name="listAllSubspaceIds", query="SELECT instance.id FROM Subspaces instance"),
@NamedQuery(name="listAllSliceIds", query="SELECT instance.id FROM Slices instance"),
@NamedQuery(name="getSubspace", query="SELECT x FROM Subspaces x WHERE x.metaSubspace.scopedName = :uriParam"),
@NamedQuery(name="listPublicSubspaces", query="SELECT DISTINCT x FROM Subspaces x WHERE x.metaSubspace.scopedName.nameScope = :uriParam"),
@NamedQuery(name="listSupportedSchemas", query="SELECT DISTINCT x.scopedName.nameScope FROM MetaSubspaces x" )
])
@Entity(name="DSpaces")
@Table(name="dspace", schema="dspace")
@MappedSuperclass
class DSpace extends SingletonGridResource {

	@NotNull DSpaceRef createRef() {
		DSpaceRef ref = new DSpaceRef()
		ref.setGridSiteId(null)
		ref.setResourceKeyValue(getId())
		return ref
	}
}