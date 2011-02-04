package de.zib.gndms.neomodel.common;

import org.jetbrains.annotations.NotNull;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 03.02.11
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public class ModelNode extends ModelGraphElement<Node> {
    protected ModelNode(@NotNull NeoSession session, @NotNull String typeNick, @NotNull Node underlying) {
        super(session, typeNick, underlying);
    }

    protected Index<Node> getTypeNickIndex() {
        return repr().getGraphDatabase().index().forNodes(getTypeNick());
    }

    @Override
    protected void delete() {
        repr().delete();
    }
}
