/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.zib.gndms.neomodel.common;

import de.zib.gndms.model.common.GridResourceItf;
import org.jetbrains.annotations.NotNull;
import org.neo4j.graphdb.Node;

/**
 * NodeGridResource
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 * User: stepn Date: 05.09.2008 Time: 14:48:36
 */
public class NodeGridResource<I> extends ModelNode implements GridResourceItf, ROType<I> {
    private static final String GRID_RESOURCE_ID_P = "ID_P";

    protected NodeGridResource(@NotNull ReprSession session, @NotNull String typeNick, @NotNull Node underlying) {
        super(session, typeNick, underlying);
    }

    public String getId() {
        return (String) repr().getProperty(GRID_RESOURCE_ID_P, null);
    }

    public void setId(String id) {
        repr().setProperty(GRID_RESOURCE_ID_P, id);
    }

    public I getReadOnly() {
        return (I) this;
    }
}
