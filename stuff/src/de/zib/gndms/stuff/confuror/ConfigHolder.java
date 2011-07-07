package de.zib.gndms.stuff.confuror;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * ConfigHolder
 *
 * @author try ste fan pla nti kow zib
 *         <p/>
 *         User stepn Date: 23.03.11 TIME: 16:17
 */
public class ConfigHolder {
    private @Autowired @NotNull ObjectMapper objectMapper;
    private @Nullable JsonNode node;

    public ConfigHolder() {
    }

    public ConfigHolder(@NotNull ObjectMapper mapper) {
        setObjectMapper(mapper);
    }

    public ConfigHolder(JsonNode node) {
        setNode(node);
    }

    public ConfigHolder(@NotNull ObjectMapper mapper, JsonNode node) {
        setObjectMapper(mapper);
        setNode(node);
    }


    public ConfigEditor newEditor( ConfigEditor.Visitor visitor ) {
        return new ConfigEditor(visitor);
    }


    public void update( ConfigEditor editor, JsonNode nodeUpdate )
            throws ConfigEditor.UpdateRejectedException, IOException {
        this.node = editor.update(getSnapshotAsNode(), nodeUpdate);
    }


    public JsonNode getSnapshotAsNode() throws IOException {
        return getObjectMapper().readTree(getSnapshotAsParser());
    }

    private JsonParser getSnapshotAsParser() throws IOException {
        if (node == null)
            node = newNullNode(getObjectMapper());
        return node.traverse();
    }



    public @NotNull ObjectMapper getObjectMapper() {
        return objectMapper;
    }


    public void setObjectMapper( @NotNull ObjectMapper objectMapper ) {
        this.objectMapper = objectMapper;
    }

    public static @NotNull JsonNode newNullNode(@NotNull ObjectMapper objectMapper) throws IOException {
        return objectMapper.getJsonFactory().createJsonParser("null").readValueAsTree();
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public static String toSingle(String singleJson) {
        return singleJson.replace('"', '\'');
    }

    public static String toDouble(String singleJson) {
        return singleJson.replace('\'', '"');
    }

    public static @NotNull JsonNode parseDouble(@NotNull JsonFactory factory, @NotNull String input)
            throws IOException {
        return factory.createJsonParser(input).readValueAsTree();
    }

    public static @NotNull JsonNode parseSingle(@NotNull JsonFactory factory, @NotNull String single)
            throws IOException {
        return parseDouble(factory, toDouble(single));
    }

	public static String createSingleEntry(String key, Object value) {
		if (value instanceof java.lang.String ||  value instanceof java.lang.Enum) {
			return "{ '" + key +"': '" + value + "' }";
		} else {
			return "{ '" + key +"': " + value + " }";
		}
	}


    @SuppressWarnings({"UnusedDeclaration"})
    public @Nullable JsonNode getNode() {
        return node;
    }


    @SuppressWarnings({"UnusedDeclaration"})
    public void setNode( @Nullable JsonNode node ) {
        this.node = node;
    }
}
