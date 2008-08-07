package de.zib.gndms.model.dspace

import de.zib.gndms.model.common.GridResource
import de.zib.gndms.model.util.LifecycleEventDispatcher
import javax.persistence.*
import org.jetbrains.annotations.NotNull

/**
 * Instances represent an installations' DSpace singleton resource on the database model side
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 01.08.2008 Time: 16:02:46
 */
@NamedQueries([
@NamedQuery(name="findDSpaceInstance", query="SELECT instance FROM DSpace instance"),
@NamedQuery(name="listPublicSubspaces", query="SELECT subspace FROM Subspaces subspace WHERE subspace.isPublicSubspace() == true")
])
@Entity(name="DSpace") @EntityListeners([LifecycleEventDispatcher.class]) 
@Table(name="dspace", schema="dspace")
class DSpace extends GridResource {

	def public @NotNull DSpaceRef createRef() {
		DSpaceRef ref = new DSpaceRef()
		ref.setGridSiteId(null)
		ref.setResourceKeyValue(getId())
		return ref
	}
}