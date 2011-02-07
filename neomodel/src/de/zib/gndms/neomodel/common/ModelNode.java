package de.zib.gndms.neomodel.common;

import org.jetbrains.annotations.NotNull;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 03.02.11
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public class ModelNode extends ModelGraphElement<Node> {
    protected ModelNode(@NotNull NeoReprSession session, @NotNull String typeNick, @NotNull Node underlying) {
        super(session, typeNick, underlying);
    }

    @Override
    protected void delete() {
        for (Relationship rel : repr().getRelationships())
            rel.delete();
        repr().delete();
    }

    @NotNull protected Index<Node> getTypeNickIndex() {
        return repr().getGraphDatabase().index().forNodes(getTypeNick());
    }

    @NotNull protected Index<Node> getTypeNickIndex(@NotNull String... names) {
        final StringBuffer indexName = new StringBuffer(getTypeNick());
        for (final String name : names)
            if (name == null)
                throw new IllegalArgumentException("(null) index name component");
            else if (name.contains(INDEX_SEPARATOR))
                throw new IllegalArgumentException("index name component must not contain " + INDEX_SEPARATOR);
            else {
                indexName.append(INDEX_SEPARATOR);
                indexName.append(names);
            }
        return repr().getGraphDatabase().index().forNodes(indexName.toString());
    }
}
