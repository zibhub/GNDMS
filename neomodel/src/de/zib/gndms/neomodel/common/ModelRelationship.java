package de.zib.gndms.neomodel.common;

import org.jetbrains.annotations.NotNull;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 03.02.11
 * Time: 16:24
 * To change this template use File | Settings | File Templates.
 */
public class ModelRelationship extends ModelGraphElement<Relationship> {
    protected ModelRelationship(@NotNull NeoSession session, @NotNull String typeNick,
                                @NotNull Relationship underlying) {
        super(session, typeNick, underlying);
    }

    protected Index<Relationship> getTypeNickIndex() {
        return repr().getGraphDatabase().index().forRelationships(getTypeNick());
    }
}
