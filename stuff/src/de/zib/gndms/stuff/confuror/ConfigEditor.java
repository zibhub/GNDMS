package de.zib.gndms.stuff.confuror;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;

/**
 * ConfigEditor
 *
 * Helper for applying updates to a json tree
 *
 * @see ConfigEditor#update(org.codehaus.jackson.JsonNode, org.codehaus.jackson.JsonNode)
 *
 * @author try ste fan pla nti kow zib
 */
public final class ConfigEditor {

    /**
     * Implemented by visitors that want to allow/deny/modify updates to the tree
     *
     * @see Update
     */
    public static interface Visitor {
        public ObjectMapper getObjectMapper();
        public void updateNode(@NotNull Update updater);
    }

    /**
     * Single update to a node
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static interface Update {
        /**
         * @return Original node to be updated
         */
        @NotNull JsonNode getOriginal();

        /**
         * Returns an Update! (i.e. field names may contain +, -)
         *
         * @return Replacement node
         */
        @NotNull JsonNode getUpdate();

        /**
         * @return Path to original from root (String and int cursors)
         */
        Object[] getPath();

        /**
         * @return true, if the update will be accepted
         */
        boolean isAccepted();
        void setAccepted(boolean acceptedState);

        /**
         * Accept and change update
         *
         * @param finalUpdate the replacement node
         */
        void replace(@NotNull JsonNode finalUpdate);

        void accept();
        void reject();

        /**
         * @return the update mode
         */
        @NotNull Mode getMode();

        enum Mode { OVERWRITE, APPEND, DELETE }
    }

    private final @NotNull Visitor visitor;

    public ConfigEditor(@NotNull Visitor visitor) {
        this.visitor = visitor;
    }

    /**
     * Apply the update diff to node subject to the following rules. For every singular node that is updated, the
     * ConfigEditor.Visitor of this ConfigEditor is called.
     *
     * The toplevel is expected to be an object or an array or null (initially only).
     *
     * Updates to the toplevel are always appended, i.e. the toplevel is never fully overwritten.
     *
     * Sublevels are always overwritten unless all keys on the path to the current level are all prefixed with '+'.
     * (However the final key to the value must not be prefixed with '+'!)
     *
     * To delete, prefix the key of the entry that is to be deleted with '-'
     *
     * @param node the root node to be updated
     * @param update the update to be applied to the root node
     * @return the updated node
     *
     * @throws UpdateRejectedException  if anything goes wrong or the visitor vetos
     */
    @SuppressWarnings({"UnnecessaryContinue"})
    public @NotNull JsonNode update(@NotNull JsonNode node, @NotNull JsonNode update) throws UpdateRejectedException {
        if (! (node.isObject() || node.isArray() || node.isNull()))
            throw new IllegalArgumentException("Toplevel node is neither an object nor an array nor null");

        final Stack<Record> records = new Stack<Record>();
        final Record topRecord = new Record(node, update, null, null, Update.Mode.APPEND);
        records.push(topRecord);

        try { while (! records.empty()) {
            final Record rec = records.pop();

            // handles nodes of different types and null snapshot causes by missing pieces
            if (rec.snapshot == null || rec.snapshot.getClass() != rec.update.getClass()) {
                rec.visit(records, visitor);
                continue;
            }

            // find differences and call visitor as necessary

            // (1) Handle value nodes
            if (rec.snapshot.isValueNode()) {
                if (! rec.snapshot.equals(rec.update))
                    // replace
                    rec.visit(records, visitor);
                continue;
            }

            // (2) Handle array nodes
            if (rec.snapshot.isArray()) {
                if ((rec.snapshot.size() != rec.update.size()) || (! Update.Mode.APPEND.equals(rec.mode)))
                    // replace if update is of different size or we're not in APPEND
                    rec.visit(records, visitor);
                else {
                    // update all childs
                    for (int i = 0; i < rec.update.size(); i++)
                        records.push(new Record(rec.snapshot.get(i), rec.update.get(i), rec, i,
                                                Update.Mode.OVERWRITE));
                }
                continue;
            }

            // (3) Handle object nodes
            if (rec.snapshot.isObject()) {
                if (! Update.Mode.APPEND.equals(rec.mode)) {
                    // replace if we're not in APPEND
                    rec.visit(records, visitor);
                }
                else {
                    // handle childs according to mode selected by key prefix
                    for (final Iterator<String> iter = rec.update.getFieldNames(); iter.hasNext(); ) {
                        final String fieldName = iter.next();
                        switch (fieldName.charAt(0)) {
                        case '+': {
                            final String snapshotFieldName = fieldName.substring(1);
                            final JsonNode snapshot        = rec.snapshot.get(snapshotFieldName);
                            if (snapshot != null && snapshot.isObject())
                                records.push(new Record(snapshot, rec.update.get(fieldName),
                                                        rec, snapshotFieldName, Update.Mode.APPEND));
                            else
                                throw new IllegalArgumentException("No updatable hash found");
                            }
                            break;
                        case '-': {
                            final String snapshotFieldName = fieldName.substring(1);
                            new Record(rec.snapshot.get(snapshotFieldName), null,
                                       rec, snapshotFieldName, Update.Mode.DELETE).visit(records, visitor);
                            }
                            break;
                        default:
                        records.push(new Record(rec.snapshot.get(fieldName), rec.update.get(fieldName),
                                                rec, fieldName, Update.Mode.OVERWRITE));
                        }
                    }
                }
                continue;
            }
            throw new IllegalArgumentException("Unknown node type encountered");
        } }
        catch (IllegalArgumentException iae) { throw new UpdateRejectedException(iae); }
        catch (IOException ioe) { throw new UpdateRejectedException(ioe);}

        return topRecord.snapshot;
    }

    /**
     * Internal stack record
     *
     * @see ConfigEditor#update(org.codehaus.jackson.JsonNode, org.codehaus.jackson.JsonNode)
     */
    private static class Record {

        private JsonNode snapshot;
        private JsonNode update;
        private Record parent;
        private Object cursor;
        private int depth = 0;
        private Update.Mode mode = Update.Mode.OVERWRITE;

        Record(JsonNode snapshot, JsonNode update, Record parent, Object cursor, Update.Mode mode) {
            this.snapshot = snapshot;
            this.update = update;
            this.parent = parent;
            this.cursor = cursor;
            if (parent != null)
                this.depth = parent.depth + 1;
            this.mode  = mode;
        }

        /**
         * Ask visitor to verify the update corresponding to this record and if the visitor doesn't veto, apply it.
         *
         * @param records record stack used by current update call
         * @param visitor the visitor used to verify updates
         *
         * @throws UpdateRejectedException if the visitor vetos
         * @throws IOException on parsing problems
         */
        void visit(Stack<Record> records, Visitor visitor) throws UpdateRejectedException, IOException {
            final Update updater = this.updater();
            visitor.updateNode(updater);
            if (updater.isAccepted()) {
                if (parent == null)
                    // toplevel neeeds special handling to create null node or replace
                    snapshot = Update.Mode.DELETE.equals(updater.getMode()) ?
                                    ConfigHolder.newNullNode(visitor.getObjectMapper()) : updater.getUpdate();
                else {
                    if (parent.snapshot.isArray())
                        ((ArrayNode)parent.snapshot).set((Integer)cursor,
                                    (Update.Mode.DELETE.equals(updater.getMode()) ?
                                          ConfigHolder.newNullNode(visitor.getObjectMapper()) : updater.getUpdate()));
                    else {
                        if (parent.snapshot.isObject()) {
                            switch(updater.getMode()) {
                            case OVERWRITE:
                                ((ObjectNode)parent.snapshot).put((String) cursor, updater.getUpdate());
                                break;
                            case APPEND:
                                for (final Iterator<String> iter = update.getFieldNames(); iter.hasNext(); ) {
                                    String fieldName = iter.next();
                                    ObjectNode objSnapshot = ((ObjectNode)snapshot);
                                    records.push(
                                            new Record(objSnapshot.get(fieldName), update.get(fieldName), this,
                                                       fieldName, Update.Mode.OVERWRITE));
                                }
                                break;
                            case DELETE:
                                ((ObjectNode)parent.snapshot).remove((String) cursor);
                                break;
                            default:
                                throw new IllegalStateException("Should never have been reached");
                            }
                        } else
                            throw new IllegalStateException("Update of non-container node");
                    }
                }
            }
            else
                throw new UpdateRejectedException();
        }

        /**
         *
         * @return new updater suitable for use with this record only
         */
        Update updater() {
            return new DefaultNodeUpdater() {
                @NotNull
                public JsonNode getOriginal() {
                    return snapshot;
                }

                @NotNull
                public JsonNode getUpdate() {
                    if (Mode.DELETE.equals(getMode()))
                        throw new IllegalStateException("Can't get update for delete operation");
                    return update;
                }

                public Object[] getPath() {
                    final Object[] path = new Object[depth];
                    Record cur = Record.this;
                    for (int i = depth; i > 0; i--) {
                        path[i-1] = cur.cursor;
                        cur = cur.parent;
                    }
                    return path;
                }

                @Override
                public void setUpdate(@NotNull JsonNode finalUpdate) {
                    update = finalUpdate;
                    super.setUpdate(finalUpdate);
                }

                @NotNull
                public Mode getMode() {
                    return Record.this.mode;
                }
            };
        }
    }

    /**
     * Default implementation of ConfigEditor.Visitor
     *
     * @see Visitor
     *
     * @author Stefan Plantikow
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static class DefaultVisitor implements Visitor {
        private ObjectMapper objectMapper;

        public void updateNode(@NotNull Update updater) {
            updater.accept();
        }

        public ObjectMapper getObjectMapper() {
            return objectMapper;
        }

        public void setObjectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }
    }

    /**
     * Default implementation of an Update
     *
     * @see Update
     *
     * @author Stefan Plantikow
     */
    public static abstract class DefaultNodeUpdater implements Update {
        private boolean accepted = true;

        public boolean isAccepted() {
            return accepted;
        }

        public void setAccepted(boolean acceptedState) {
            accepted = acceptedState;
        }

        public void setUpdate(@NotNull JsonNode finalUpdate) {
            if (Mode.DELETE.equals(getMode()))
                throw new IllegalStateException("Can't set update for delete operation");
            setAccepted(true);
        }

        public final void replace(@NotNull JsonNode finalUpdate) {
            setUpdate(finalUpdate);
        }

        public final void accept() {
            setAccepted(true);
        }

        public final void reject() {
            setAccepted(false);
        }
    }

    /**
     * Thrown to indicate that an update could not be applied by ConfigEditor
     *
     * @author Stefan Plantikow
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static class UpdateRejectedException extends Exception {

        public UpdateRejectedException() {
        }

        public UpdateRejectedException(String s) {
            super(s);
        }

        public UpdateRejectedException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public UpdateRejectedException(Throwable throwable) {
            super(throwable);
        }
    }
}
