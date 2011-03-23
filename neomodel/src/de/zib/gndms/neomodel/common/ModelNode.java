package de.zib.gndms.neomodel.common;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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

import org.jetbrains.annotations.NotNull;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;

/**
 * ModelNode
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 * User: stepn Date: 05.09.2008 Time: 14:48:36
 */
public class ModelNode extends ModelElement<Node> {
    public static final String TYPE_INDEX_IDX = INDEX_SEPARATOR + TYPE_P;

    protected ModelNode(@NotNull ReprSession session, @NotNull String typeNick, @NotNull Node underlying) {
        super(session, typeNick, underlying);
    }

    public void onCreate(ReprSession reprSession) {
        assert reprSession() == reprSession;
        final Index<Node> index = repr().getGraphDatabase().index().forNodes(TYPE_INDEX_IDX);
        index.add(repr(), session().getGridName(), getTypeNick());
    }

    @Override
    protected void delete() {
        for (Relationship rel : repr().getRelationships())
            rel.delete();
        repr().delete();
        final Index<Node> index = repr().getGraphDatabase().index().forNodes(TYPE_INDEX_IDX);
        index.remove(repr(), session().getGridName(), getTypeNick());
    }

    @NotNull protected Index<Node> getTypeNickIndex() {
        return repr().getGraphDatabase().index().forNodes(getTypeNick());
    }

    @NotNull protected Index<Node> getTypeNickIndex(@NotNull String... names) {
        final String typeNickIndexName = getTypeNickIndexName(getTypeNick(), names);
        return repr().getGraphDatabase().index().forNodes(typeNickIndexName);
    }

    protected long getReprId() {
        return repr().getId();
    }
}
