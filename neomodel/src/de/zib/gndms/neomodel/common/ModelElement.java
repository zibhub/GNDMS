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

import de.zib.gndms.model.ModelEntity;
import org.apache.commons.lang.SerializationUtils;
import org.jetbrains.annotations.NotNull;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.index.Index;

import java.io.Serializable;

/**
 * ModelElement
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 * User: stepn Date: 05.09.2008 Time: 14:48:36
 */
public abstract class ModelElement<U extends PropertyContainer> extends ModelEntity {
    public static final String INDEX_SEPARATOR = "@@";
    public static final String TYPE_P = "TYPE_P";

    final @NotNull U representation;
    final @NotNull ReprSession reprSession;
    final @NotNull private String typeNick;

    protected ModelElement(@NotNull ReprSession session, @NotNull String typeNick, @NotNull U underlying) {
        if (typeNick.contains(INDEX_SEPARATOR))
            throw new IllegalArgumentException("typeNick must not contain " + INDEX_SEPARATOR);
        this.reprSession = session;
        this.representation = underlying;
        this.typeNick       = typeNick;
        if (! underlying.hasProperty(TYPE_P))
            underlying.setProperty(TYPE_P, getTypeNick());
        repr().setProperty(TYPE_P, getTypeNick());
        // todo throw exception if type_p exists and not equal
    }

    @NotNull protected U getRepresentation() {
        return representation;
    }

    final @NotNull protected U repr() { return getRepresentation(); }

    @NotNull protected Session getSession() {
        return reprSession.getSession();
    }

    final @NotNull protected Session session() { return getSession(); }

    protected @NotNull String getTypeNick() {
        return typeNick;
    }

    protected abstract Index<U> getTypeNickIndex();

    protected abstract Index<U> getTypeNickIndex(final @NotNull String... names);

    protected boolean hasProperty(String key) {
        return representation.hasProperty(key);
    }

    protected Object getProperty(String key) {
        return representation.getProperty(key);
    }

    protected Object getProperty(String key, Object defaultValue) {
        return representation.getProperty(key, defaultValue);
    }

    protected void setProperty(String key, Object value) {
        representation.setProperty(key, value);
    }

    protected Object removeProperty(String key) {
        return representation.removeProperty(key);
    }

    protected Iterable<String> getPropertyKeys() {
        return representation.getPropertyKeys();
    }

    protected <S extends Serializable> S getProperty(@NotNull Class<S> clazz, String key) {
        return clazz.cast(SerializationUtils.deserialize((byte[]) repr().getProperty(key)));
    }

    protected <S extends Serializable> S getProperty(@NotNull Class<S> clazz, String key, S deaultValue) {
        Object property = repr().getProperty(key, deaultValue);
        if (property == deaultValue)
            return deaultValue;
        else
            return clazz.cast(SerializationUtils.deserialize((byte[]) property));
    }

    protected <S extends Serializable> void setProperty(@NotNull Class<S> clazz, String key, S value) {
        repr().setProperty(key, SerializationUtils.serialize(value));
    }

    public final void delete(@NotNull Session session) {
        if (session != session())
            throw new IllegalArgumentException("Attempt to delete domain object from another session");
        else
            this.delete();
    }

    protected void delete() {
        throw new UnsupportedOperationException("delete() of this domain object is unsupported");
    }


    @NotNull protected ReprSession getReprSession() {
        return reprSession;
    }

    @NotNull final protected ReprSession reprSession() {
        return getReprSession();
    }

    public U repr(Object authenticator) {
        if (authenticator == getReprSession())
            return repr();
        else
            throw new IllegalArgumentException("May not access implementation object without involvement of the Dao");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (! getClass().equals(o.getClass())) return false;

        final ModelElement other = getClass().cast(o);
        return other.reprSession == other.reprSession &&
               repr().getClass().equals(other.repr().getClass()) &&
                getReprId() == other.getReprId();
    }


    @Override
    public int hashCode() {
        final long reprId = getReprId();
        return 851 + (int) (reprId ^ (reprId << 32));
    }

    public static String getTypeNickIndexName(String typeNick, String... names) {
        final StringBuilder indexName = new StringBuilder( typeNick );
        for (final String name : names)
            if (name == null)
                throw new IllegalArgumentException("(null) index name component");
            else if (name.contains(INDEX_SEPARATOR))
                throw new IllegalArgumentException("index name component must not contain " + INDEX_SEPARATOR);
            else {
                indexName.append(INDEX_SEPARATOR);
                indexName.append(name);
            }
        return indexName.toString();
    }

    protected abstract long getReprId();
}
