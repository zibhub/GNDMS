package de.zib.gndms.kit.config;

import org.jetbrains.annotations.NotNull;
import org.codehaus.jackson.JsonNode;

import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

/**
 * TreeConfig
 *
 * Construct an AbstractConfig atop a JsonNode
 *
 * @see AbstractConfig
 * @see ConfigProvider
 *
 * @author try ste fan pla nti kow zib
 *         <p/>
 *         User stepn Date: 28.03.11 TIME: 10:59
 */
public class TreeConfig extends AbstractConfig {
    private final JsonNode tree;

    public TreeConfig(JsonNode tree) {
        this.tree = tree;
    }

    @Override
    public @NotNull ConfigProvider getDynArrayOption(@NotNull String name)
            throws ParseException, MandatoryOptionMissingException {
        // Traverse tree and create new TreeConfig atop child node
        final Object[] path = keyAsPath(name);
        JsonNode ptr = tree;
        for (Object cursor : path)
            ptr = step(ptr, cursor);
        return new TreeConfig(ptr);
    }

    public boolean hasOption(@NotNull String name) {
        try {
            // ConfigProvider doesnt allow null values while json does, we choose to ignore them
            return getConcreteNonMandatoryOption(name) != null;
        }
        catch (KeyNotFoundException knfe) {
            return false;
        }
    }

    @Override
    public String getConcreteNonMandatoryOption(String name) throws KeyNotFoundException {
        // build path from name
        final Object[] path = keyAsPath(name);
        try {
            // empty path: select root node
            if (path == null)
                return toText(tree);
            else {
                // traverse path except for last element
                JsonNode ptr = tree;
                for (int i = 0; i < path.length-1; i++)
                    ptr = step(ptr,  path[i]);

                // for last element, type-dependently check and retrieve value
                final Object lastCursor = path[path.length-1];
                // Integer: Array-Deref
                if (lastCursor instanceof Integer) {
                    final int intCursor = (Integer) lastCursor;
                    if (ptr.has(intCursor))
                        return toText(ptr.get((Integer) lastCursor));
                    else
                        throw new KeyNotFoundException(lastCursor.toString());
                }
                else {
                    // If Array: Check if user is asking for virtual count ("size") attribute
                    if (ptr.isArray()) {
                        if ("count".equals(lastCursor))
                            return Integer.toString(ptr.size());
                        else
                            throw new KeyNotFoundException("Can't resolve key in array");
                    }
                    // Otherwise: Object-Field-Deref
                    else if (ptr.isObject()) {
                        final String strCursor = (String) lastCursor;
                        if (ptr.has(strCursor))
                            return toText(ptr.get(strCursor));
                        else
                            throw new KeyNotFoundException(strCursor);
                    }
                    else
                        throw new KeyNotFoundException("Invalid tree node type encountered");
                }
            }
        }
        catch (KeyNotFoundException knfe) {
            throw knfe;
        }
        catch (RuntimeException re) {
            throw new KeyNotFoundException(re);
        }
    }

    // Convert dot-separated key string to path array of cursor components
    // (handles null and parses ints)
    private Object[] keyAsPath(String key) throws InvalidKeyException {
        if (key == null)
            return null;
        else {
            try {
                final String[] parts = key.split("\\.");
                final Object[] path  = new Object[parts.length];
                if (parts.length > 0) {
                    for(int i = 0; i < parts.length; i++) {
                        final String part = parts[i];
                        if (part.length() == 0)
                            return null;
                        else {
                            try {
                                path[i] = Integer.parseInt(part);
                            }
                            catch (NumberFormatException nfe) {
                                path[i] = part;
                            }
                        }

                    }
                    return path;
                }
                else
                    throw new InvalidKeyException("Key must contain at least one part");
            }
            catch (InvalidKeyException ike) {
                throw ike;
            }
            catch (RuntimeException re) {
                    throw new InvalidKeyException(re);
            }
        }
    }


    // json node to string (handles null and doesnt quote json string values)
    private static String toText(JsonNode node) {
        if (node == null || node.isNull())
            return null;
        else {
            if (node.isTextual())
                return node.getTextValue();
            else
                return node.toString();
        }
    }

    // traverse single step in json tree from ptr via cursor
    private @NotNull JsonNode step(@NotNull JsonNode ptr, @NotNull Object cursor) throws KeyNotFoundException {
        if (cursor instanceof Integer) {
            final Integer intCursor = (Integer) cursor;
            if (ptr.has(intCursor))
                return ptr.get(intCursor);
            else
                throw new KeyNotFoundException("Integer key not found");
        }
        else {
            String strCursor = (String) cursor;
            if (ptr.has(strCursor))
                return ptr.get(strCursor);
            else
                throw new KeyNotFoundException("String key not found");
        }
    }

    // Collect all keys
    public Iterator<String> iterator() {
        final Collection<String> results = new LinkedList<String>();
        final Stack<StackElement> work   = new Stack<StackElement>();

        work.push(new StackElement(null, tree, null));

        // DFS of json tree structure
        while (! work.isEmpty()) {
            final StackElement elem = work.pop();

            // type-dependingly add keys to results or push nested objects on work stack
            if (elem.node.isObject()) {
                for(final Iterator<String> iter = elem.node.getFieldNames(); iter.hasNext(); ) {
                    final String fieldName = iter.next();
                    final JsonNode child = elem.node.get(fieldName);
                    final StackElement childElem = new StackElement(elem, child, fieldName);
                    if (child.isValueNode())
                        results.add(childElem.getKey(null));
                    else
                        work.push(childElem);
                }
            }
            else if (elem.node.isArray()) {
                final int size = elem.node.size();
                // special treatment of virtual "count" attribute
                results.add(elem.getKey("count"));
                for (int i = 0; i < size; i++) {
                    final JsonNode child = elem.node.get(i);
                    final StackElement childElem = new StackElement(elem, child, Integer.toString(i));
                    if (child.isValueNode())
                        results.add(childElem.getKey(null));
                    else
                        work.push(childElem);
                }
            }
            else throw new IllegalStateException("Unexpected json node type");
        }
        return results.iterator();
    }

    /**
     * StackElement is used by TreeConfig#iterator when traversing the json tree structure
     *
     * @see TreeConfig#iterator
     */
    private static class StackElement {
        private StackElement parent;
        private JsonNode node;
        private String cursor;

        private StackElement(StackElement parent, JsonNode node, String cursor) {
            this.parent = parent;
            this.node = node;
            this.cursor = cursor;
        }

        private String getKey(String suffix) {
            final StringBuilder builder = new StringBuilder();
            buildKey(builder);
            if (suffix != null) {
                if (builder.length() > 0)
                    builder.append('.');
                builder.append(suffix);
            }
            return builder.toString();
        }

        private void buildKey(StringBuilder builder) {
            if (parent != null) {
                parent.buildKey(builder);
                if (parent.cursor != null)
                    builder.append('.');
                builder.append(cursor);
            }
        }
    }

    /**
     * Thrown to indicate an invalid (string) key argument used with TreeConfig
     *
     * @see TreeConfig
     *
     * @author try ste fan pla nti kow zib
     *         <p/>
     *         User stepn Date: 28.03.11 TIME: 10:59
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static class InvalidKeyException extends IllegalArgumentException {
        public InvalidKeyException() {
        }

        public InvalidKeyException(String s) {
            super(s);
        }

        public InvalidKeyException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public InvalidKeyException(Throwable throwable) {
            super(throwable);
        }
    }

    /**
     * Thrown to indicate a missing json node when traversing a json using a (string-based) key in TreeConfig
     *
     * @see TreeConfig
     *
     * @author try ste fan pla nti kow zib
     *         <p/>
     *         User stepn Date: 28.03.11 TIME: 10:59
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static class KeyNotFoundException extends IllegalArgumentException {

        public KeyNotFoundException() {
        }

        public KeyNotFoundException(String s) {
            super(s);
        }

        public KeyNotFoundException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public KeyNotFoundException(Throwable throwable) {
            super(throwable);
        }
    }
}


