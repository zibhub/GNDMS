package de.zib.gndms.neomodel.common;

import de.zib.gndms.model.ModelObject;
import org.apache.commons.lang.SerializationUtils;
import org.jetbrains.annotations.NotNull;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.index.Index;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 03.02.11
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */
public abstract class ModelGraphElement<U extends PropertyContainer> extends ModelObject {
    public static final String INDEX_SEPARATOR = "@@";
    public static final String TYPE_P = "TYPE_P";

    final @NotNull U representation;
    final @NotNull NeoSession session;
    final @NotNull private String typeNick;

    protected ModelGraphElement(@NotNull NeoReprSession session, @NotNull String typeNick, @NotNull U underlying) {
        if (typeNick.contains(INDEX_SEPARATOR))
            throw new IllegalArgumentException("typeNick must not contain " + INDEX_SEPARATOR);
        this.reprSession = session;
        this.representation = underlying;
        this.typeNick       = typeNick;
        if (! underlying.hasProperty(TYPE_P))
            underlying.setProperty(TYPE_P, getTypeNick());
        repr().setProperty(TYPE_P, getTypeNick());
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

    protected abstract Index<U> getTypeNickIndex(final @NotNull String... names);

    protected boolean hasProperty(String s) {
        return representation.hasProperty(s);
    }

    protected Object getProperty(String s) {
        return representation.getProperty(s);
    }

    protected Object getProperty(String s, Object o) {
        return representation.getProperty(s, o);
    }

    protected void setProperty(String s, Object o) {
        representation.setProperty(s, o);
    }

    protected Object removeProperty(String s) {
        return representation.removeProperty(s);
    }

    protected Iterable<String> getPropertyKeys() {
        return representation.getPropertyKeys();
    }

    protected <S extends Serializable> S getProperty(@NotNull Class<S> clazz, String key) {
        return clazz.cast(SerializationUtils.deserialize((byte[]) repr().getProperty(key)));
    }

    protected <S extends Serializable> S getProperty(@NotNull Class<S> clazz, String key, Object value) {
        return clazz.cast(SerializationUtils.deserialize((byte[]) repr().getProperty(key, value)));
    }

    protected <S extends Serializable> void setProperty(@NotNull Class<S> clazz, String key, S value) {
        repr().setProperty(key, SerializationUtils.serialize(value));
    }

    public final void delete(@NotNull NeoSession session) {
        if (session != session())
            throw new IllegalArgumentException("Attempt to delete domain object from another session");
        else
            this.delete();
    }

    protected void delete() {
        throw new UnsupportedOperationException("delete() of this domain object is unsupported");
    }
}
