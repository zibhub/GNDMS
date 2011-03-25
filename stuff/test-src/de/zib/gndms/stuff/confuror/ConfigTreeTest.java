package de.zib.gndms.stuff.confuror;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class ConfigTreeTest {
    private volatile ConfigTree tree;
    private final @NotNull ObjectMapper objectMapper;
    private final @NotNull JsonFactory factory;
    private final @NotNull ConfigEditor.Visitor visitor;

    {
        objectMapper = new ObjectMapper();
        factory = objectMapper.getJsonFactory();
        visitor = new ConfigEditor.DefaultVisitor();
    }

    @BeforeMethod
    public void setup() {
        tree = new ConfigTree();
        tree.setObjectMapper(objectMapper);
        objectMapper.disableDefaultTyping();
    }

    @SuppressWarnings({"UnusedDeclaration"})
    String toSingle(String singleJson) {
        return singleJson.replace('"', '\'');
    }

    String toDouble(String singleJson) {
        return singleJson.replace('\'', '"');
    }
    JsonNode parseDouble(String input) throws IOException {
        return factory.createJsonParser(input).readValueAsTree();
    }

    JsonNode parseSingle(String single) throws IOException {
        return parseDouble(toDouble(single));
    }

    @Test
    public void replaceEmptyRoot() throws IOException, ConfigEditor.UpdateRejectedException {
        ConfigEditor editor = tree.newUpdater(visitor);
        JsonNode update = parseDouble("12");
        tree.update(editor, update);
        final JsonNode snapshot = tree.getSnapshotAsNode();
        Assert.assertTrue(snapshot.isValueNode());
        Assert.assertTrue(snapshot.equals(update));
    }


    @Test
    public void replaceObjectRoot() throws IOException, ConfigEditor.UpdateRejectedException {
        ConfigEditor editor = tree.newUpdater(visitor);
        JsonNode update = parseSingle("{ 'a': 12, 'b': 4 }");
        tree.update(editor, update);
        final JsonNode snapshot = tree.getSnapshotAsNode();
        Assert.assertTrue(snapshot.isObject());
        Assert.assertTrue(snapshot.equals(update));
    }

    @Test
    public void appendBelowRoot() throws IOException, ConfigEditor.UpdateRejectedException {
        ConfigEditor editor = tree.newUpdater(visitor);
        JsonNode init = parseSingle("{ 'a': { 'x': 4 }, 'b': 4 }");
        tree.update(editor, init);
        JsonNode update = parseSingle("{ '+a': { 'y': 2 } }");
        tree.update(editor, update);
        final JsonNode snapshot = tree.getSnapshotAsNode();
        Assert.assertTrue(snapshot.isObject());
        Assert.assertTrue(snapshot.equals(parseSingle("{ 'a': { 'x': 4, 'y': 2 }, 'b': 4 }")));
    }

    @Test
    public void appendDeepBelowRoot() throws IOException, ConfigEditor.UpdateRejectedException {
        ConfigEditor editor = tree.newUpdater(visitor);
        JsonNode init = parseSingle("{ 'a': { 'x': 4, 'y': { 'c': 7 }  }, 'b': 4 }");
        tree.update(editor, init);
        JsonNode update = parseSingle("{ '+a': { '+y': { 'd': 12 } } }");
        tree.update(editor, update);
        final JsonNode snapshot = tree.getSnapshotAsNode();
        Assert.assertTrue(snapshot.isObject());
        Assert.assertTrue(snapshot.equals(parseSingle("{ 'a': { 'x': 4, 'y': { 'c': 7, 'd': 12 } }, 'b': 4 }")));
    }

    @Test(expectedExceptions = { ConfigEditor.UpdateRejectedException.class })
    public void errorDeepBelowRoot1() throws IOException, ConfigEditor.UpdateRejectedException {
        ConfigEditor editor = tree.newUpdater(visitor);
        JsonNode init = parseSingle("{ 'a': { 'x': 4, 'y': [] }, 'b': 4 }");
        tree.update(editor, init);
        JsonNode update = parseSingle("{ '+a': { '+y': { 'd': 12 } } }");
        tree.update(editor, update);
    }

    @Test(expectedExceptions = { ConfigEditor.UpdateRejectedException.class })
    public void errorDeepBelowRoot2() throws IOException, ConfigEditor.UpdateRejectedException {
        ConfigEditor editor = tree.newUpdater(visitor);
        JsonNode init = parseSingle("{ 'a': { 'x': 4, 'y': { 'c': 7 }  }, 'b': 4 }");
        tree.update(editor, init);
        JsonNode update = parseSingle("{ '+a': { '+y': { '+d': 12 } } }");
        tree.update(editor, update);
        final JsonNode snapshot = tree.getSnapshotAsNode();
        Assert.assertTrue(snapshot.isObject());
        Assert.assertTrue(snapshot.equals(parseSingle("{ 'a': { 'x': 4, 'y': { 'c': 7, 'd': 12 } }, 'b': 4 }")));
    }

    @Test
    public void updateDeepBelowRoot() throws IOException, ConfigEditor.UpdateRejectedException {
        ConfigEditor editor = tree.newUpdater(visitor);
        JsonNode init = parseSingle("{ 'a': { 'x': 4, 'y': { 'c': 7, 'k': 4 }  }, 'b': 4 }");
        tree.update(editor, init);
        JsonNode update = parseSingle("{ '+a': { '+y': { '-d': null, 'c': 8 } } }");
        tree.update(editor, update);
        final JsonNode snapshot = tree.getSnapshotAsNode();
        Assert.assertTrue(snapshot.isObject());
        Assert.assertTrue(snapshot.equals(parseSingle("{ 'a': { 'x': 4, 'y': { 'k': 4, 'c': 8 } }, 'b': 4 }")));
    }

    @Test
    public void deleteBelowRoot() throws IOException, ConfigEditor.UpdateRejectedException {
        ConfigEditor editor = tree.newUpdater(visitor);
        JsonNode init = parseSingle("{ 'a': 12, 'b': 4 }");
        tree.update(editor, init);
        JsonNode update = parseSingle("{ '-a': null }");
        tree.update(editor, update);
        final JsonNode snapshot = tree.getSnapshotAsNode();
        Assert.assertTrue(snapshot.isObject());
        Assert.assertTrue(snapshot.equals(parseSingle("{ 'b': 4 }")));
    }

    @Test
    public void deleteMissingBelowRoot() throws IOException, ConfigEditor.UpdateRejectedException {
        ConfigEditor editor = tree.newUpdater(visitor);
        JsonNode init = parseSingle("{ 'a': 12, 'b': 4 }");
        tree.update(editor, init);
        JsonNode update = parseSingle("{ '-c': null }");
        tree.update(editor, update);
        final JsonNode snapshot = tree.getSnapshotAsNode();
        Assert.assertTrue(snapshot.isObject());
        Assert.assertTrue(snapshot.equals(init));
    }

    @Test
    public void updateRootObject() throws IOException, ConfigEditor.UpdateRejectedException {
        ConfigEditor editor = tree.newUpdater(visitor);
        JsonNode init = parseSingle("{ 'a': 12, 'b': 4 }");
        tree.update(editor, init);
        JsonNode update = parseSingle("{ 'a': 14 }");
        tree.update(editor, update);
        final JsonNode snapshot = tree.getSnapshotAsNode();
        Assert.assertTrue(snapshot.isObject());
        Assert.assertTrue(snapshot.equals(parseSingle("{ 'a': 14, 'b': 4 }")));
    }

    @Test
    public void updateRootArray() throws IOException, ConfigEditor.UpdateRejectedException {
        ConfigEditor editor = tree.newUpdater(visitor);
        JsonNode init = parseSingle("[ 'a', 'b' ]");
        tree.update(editor, init);
        JsonNode update = parseSingle("[ 'a', 'x' ]");
        tree.update(editor, update);
        final JsonNode snapshot = tree.getSnapshotAsNode();
        Assert.assertTrue(snapshot.isArray());
        Assert.assertTrue(snapshot.equals(update));
    }

    @Test
    public void updateNestedObject() throws IOException, ConfigEditor.UpdateRejectedException {
        ConfigEditor editor = tree.newUpdater(visitor);
        JsonNode init = parseSingle("{ 'a': 12, 'b': { 'x': 2 } }");
        tree.update(editor, init);
        JsonNode update = parseSingle("{ 'b': { 'y' : 4 } }");
        tree.update(editor, update);
        final JsonNode snapshot = tree.getSnapshotAsNode();
        Assert.assertTrue(snapshot.isObject());
        Assert.assertTrue(snapshot.equals(parseSingle("{ 'a': 12, 'b': { 'y': 4 } }")));
    }

    @Test
    public void addNestedObject() throws IOException, ConfigEditor.UpdateRejectedException {
        ConfigEditor editor = tree.newUpdater(visitor);
        JsonNode init = parseSingle("{ 'a': 12, 'b': { 'x': 2 } }");
        tree.update(editor, init);
        JsonNode update = parseSingle("{ 'c': { 'y' : 4 } }");
        tree.update(editor, update);
        final JsonNode snapshot = tree.getSnapshotAsNode();
        Assert.assertTrue(snapshot.isObject());
        Assert.assertTrue(snapshot.equals(parseSingle("{ 'a': 12, 'b' : { 'x': 2 }, 'c': { 'y': 4 } }")));
    }

    @Test
    public void updateDeeplyNestedObject() throws IOException, ConfigEditor.UpdateRejectedException {
        ConfigEditor editor = tree.newUpdater(visitor);
        JsonNode init = parseSingle("{ 'a': 12, 'b': { 'x': { 'c' : 4 } } }");
        tree.update(editor, init);
        JsonNode update = parseSingle("{ 'b': { 'x' : { 'c' : 7 } } }");
        tree.update(editor, update);
        final JsonNode snapshot = tree.getSnapshotAsNode();
        Assert.assertTrue(snapshot.isObject());
        Assert.assertTrue(snapshot.equals(parseSingle("{ 'a': 12, 'b': { 'x': { 'c' : 7 } } }")));
    }

    @Test
    public void updateNestedArray() throws IOException, ConfigEditor.UpdateRejectedException {
        ConfigEditor editor = tree.newUpdater(visitor);
        JsonNode init = parseSingle("{ 'a': 12, 'b': [ 2, 3, 4 ] }");
        tree.update(editor, init);
        JsonNode update = parseSingle("{ 'b': [ 2, 5, 7 ] }");
        tree.update(editor, update);
        final JsonNode snapshot = tree.getSnapshotAsNode();
        Assert.assertTrue(snapshot.isObject());
        Assert.assertTrue(snapshot.equals(parseSingle("{ 'a': 12, 'b': [ 2, 5, 7 ] }")));
    }

    @Test
    public void pathTest() throws IOException, ConfigEditor.UpdateRejectedException {
        ConfigEditor editor = tree.newUpdater(visitor);
        JsonNode init = parseSingle("{ 'a': 12, 'b': { 'x': { 'c' : 4 } } }");
        tree.update(editor, init);
        final AtomicReference<Object[]> ref = new AtomicReference<Object[]>(null);
        ConfigEditor reportingEditor = tree.newUpdater(new ConfigEditor.Visitor() {

            public ObjectMapper getObjectMapper() {
                return objectMapper;
            }

            public void updateNode(@NotNull ConfigEditor.Update updater) {
                ref.getAndSet(updater.getPath());
                updater.accept();
            }
        });
        tree.update(reportingEditor, parseSingle("{ '+b': { '+x': { 'q': 5 } } }"));
        final Object[] result = ref.get();
        Assert.assertTrue(result.length == 3);
        Assert.assertTrue(result[0].equals("b"));
        Assert.assertTrue(result[1].equals("x"));
        Assert.assertTrue(result[2].equals("q"));
    }

}