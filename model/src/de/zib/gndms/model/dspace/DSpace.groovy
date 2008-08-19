package de.zib.gndms.model.dspace

import de.zib.gndms.model.common.GridResource
import de.zib.gndms.model.util.LifecycleEventDispatcher
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
@NamedQuery(name="findDSpaceInstances", query="SELECT instance FROM DSpaces instance WHERE instance.systemId = :systemId"),
@NamedQuery(name="listPublicSubspaces", query="SELECT subspace FROM Subspaces subspace WHERE subspace.isPublicSubspace = true AND WHERE instance.systemId = :systemId"),
@NamedQuery(name="listAllSubspaces", query="SELECT instance FROM Subspaces instance WHERE instance.systemId = :systemId"),
@NamedQuery(name="listAllSlices", query="SELECT instance FROM Slices instance WHERE instance.systemId = :systemId")
])
@Entity(name="DSpaces")
@Table(name="dspace", schema="dspace")
class DSpace extends SingletonGridResource {

	@NotNull DSpaceRef createRef() {
		DSpaceRef ref = new DSpaceRef()
		ref.setGridSiteId(null)
		ref.setResourceKeyValue(getId())
		return ref
	}
}