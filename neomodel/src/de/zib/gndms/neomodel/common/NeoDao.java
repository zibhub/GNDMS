package de.zib.gndms.neomodel.common;

import de.zib.gndms.neomodel.gorfx.NeoOfferType;
import org.jetbrains.annotations.NotNull;
import org.neo4j.graphdb.GraphDatabaseService;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 03.02.11
 * Time: 16:07
 * To change this template use File | Settings | File Templates.
 */
public class NeoDao {
    @NotNull private GraphDatabaseService gdb;
    @NotNull private String gridName;

    public NeoDao(@NotNull String gridName, @NotNull GraphDatabaseService gdb) {
        this.gridName = gridName;
        this.gdb      = gdb;
    }

    public NeoSession beginSession() {
        return new NeoSession(gridName, gdb);
    }

    @NotNull
    public String getGridName() {
        return gridName;
    }
}
