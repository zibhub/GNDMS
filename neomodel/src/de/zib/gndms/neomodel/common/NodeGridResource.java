package de.zib.gndms.neomodel.common;

import de.zib.gndms.model.common.GridResourceItf;
import org.jetbrains.annotations.NotNull;
import org.neo4j.graphdb.Node;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 03.02.11
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
 */
public class NodeGridResource extends ModelNode implements GridResourceItf {
    private static final String GRID_RESOURCE_ID_KEY = "gridResourceId";

    protected NodeGridResource(@NotNull NeoSession session, @NotNull String typeNick, @NotNull Node underlying) {
        super(session, typeNick, underlying);
    }

    public String getId() {
        return (String) repr().getProperty(GRID_RESOURCE_ID_KEY, null);
    }

    public void setId(String id) {
        repr().setProperty(GRID_RESOURCE_ID_KEY, id);
    }
}
