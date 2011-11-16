package de.zib.gndms.model.dspace;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import de.zib.gndms.model.common.SingletonGridResource;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import javax.persistence.Table;

/**
 * Instances represent an installations' DSpace singleton resource on the database model side
 * 
 * @author  try ste fan pla nti kow zib
 * @version $Id$ 
 *
 * User: stepn Date: 01.08.2008 Time: 16:02:46
 */
@NamedQueries({
@NamedQuery(name="findDSpaceInstances", query="SELECT instance FROM DSpaces instance"),
@NamedQuery(name="listAllSubspaceIds", query="SELECT instance.id FROM Subspaces instance"),
@NamedQuery(name="listAllSliceIds", query="SELECT instance.id FROM Slices instance"),
@NamedQuery(name="getSubspace", query="SELECT x FROM Subspaces x WHERE x.name.nameScope = :uriScopeParam " +
    "AND x.name.localName = :uriLocalParam"),
@NamedQuery(name="getSubspaceKey", query="SELECT x.name FROM Subspaces x WHERE x.id = :idParam"),
@NamedQuery(name="listPublicSubspaces", query="SELECT DISTINCT x FROM Subspaces x WHERE x.name.nameScope = :uriParam"),
@NamedQuery(name="listSupportedSchemas", query="SELECT DISTINCT x.name.nameScope FROM Subspaces x" ),
@NamedQuery(name="listCreatableSliceKinds", query="SELECT sk FROM Subspaces x INNER JOIN x.creatableSliceKinds sk WHERE x.name.nameScope = (SELECT y.name.nameScope FROM Subspaces y WHERE y.id = :idParam) AND x.name.localName = (SELECT y.name.localName FROM Subspaces y WHERE y.id = :idParam)")
})
@Entity(name="DSpaces")
@Table(name="dspace", schema="dspace")
//@MappedSuperclass
public class DSpace extends SingletonGridResource {

    public @NotNull DSpaceRef createRef() {

        DSpaceRef ref = new DSpaceRef();
        ref.setGridSiteId(null);
        ref.setResourceKeyValue(getId());
        return ref;
    }
}
