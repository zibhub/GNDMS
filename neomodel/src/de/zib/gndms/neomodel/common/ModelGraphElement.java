package de.zib.gndms.neomodel.common;

import de.zib.gndms.model.ModelObject;
import org.jetbrains.annotations.NotNull;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.index.Index;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 03.02.11
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */
public abstract class ModelGraphElement<U extends PropertyContainer> extends ModelObject {
    private static final String TYPE_KEY = "type";

    final @NotNull U representation;
    final @NotNull NeoSession session;
    final @NotNull private String typeNick;

    protected ModelGraphElement(@NotNull NeoSession session, @NotNull String typeNick, @NotNull U underlying) {
        this.session        = session;
        this.representation = underlying;
        this.typeNick       = typeNick;
        repr().setProperty(TYPE_KEY, getTypeNick());
    }

    @NotNull protected U getRepresentation() {
        return representation;
    }

    final @NotNull protected U repr() { return getRepresentation(); }

    @NotNull protected NeoSession getSession() {
        return session;
    }

    final @NotNull protected NeoSession session() { return getSession(); }

    protected @NotNull String getTypeNick() {
        return typeNick;
    }

    protected abstract Index<U> getTypeNickIndex();


    public final void delete(@NotNull NeoSession session) {
        if (session != session())
            throw new IllegalArgumentException("Attempt to delete domain object from another session");
    }

    protected void delete() {
        throw new UnsupportedOperationException("delete() of this domain object is unsupported");
    }
}
