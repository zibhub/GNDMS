package de.zib.gndms.stuff.confuror;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * ConfigTree
 *
 * @author try ste fan pla nti kow zib
 *         <p/>
 *         User stepn Date: 23.03.11 TIME: 16:17
 */
public class ConfigTree {
    @Autowired
    private ObjectMapper objectMapper;
    private JsonNode node;


    public ConfigEditor newUpdater(ConfigEditor.Visitor visitor) {
        return new ConfigEditor(visitor);
    }

    public void update(ConfigEditor editor, JsonNode nodeUpdate)
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


    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static JsonNode newNullNode(@NotNull ObjectMapper objectMapper) throws IOException {
        return objectMapper.getJsonFactory().createJsonParser("null").readValueAsTree();
    }
}
