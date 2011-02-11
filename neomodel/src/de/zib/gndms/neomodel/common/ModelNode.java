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
    public static final String TYPE_INDEX_IDX = INDEX_SEPARATOR + TYPE_P;

    protected ModelNode(@NotNull NeoReprSession session, @NotNull String typeNick, @NotNull Node underlying) {
        super(session, typeNick, underlying);
    }

    public void onCreate(NeoReprSession reprSession) {
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
