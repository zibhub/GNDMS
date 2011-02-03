package de.zib.gndms.neomodel.common;

import de.zib.gndms.neomodel.gorfx.NeoOfferType;
import org.jetbrains.annotations.NotNull;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 03.02.11
 * Time: 16:16
 * To change this template use File | Settings | File Templates.
 */
public class NeoSession {
    private static final String OFFER_TYPE_T = classNick(NeoOfferType.class);

    final @NotNull GraphDatabaseService gdb;
    final @NotNull Transaction tx;
    final @NotNull String gridName;

    public NeoSession(@NotNull String gridName, @NotNull GraphDatabaseService gdb) {
        this.gdb      = gdb;
        this.tx       = gdb.beginTx();
        this.gridName = gridName;
    }


    public NeoOfferType createOfferType() {
        final Node node = gdb.createNode();
        return new NeoOfferType(this, OFFER_TYPE_T, node);
    }

    @NotNull public NeoOfferType findOfferType(@NotNull String offerTypeId) {
        return new NeoOfferType(this, OFFER_TYPE_T, getTypeIndex(OFFER_TYPE_T).get(gridName, offerTypeId).getSingle());
    }

    protected Index<Node> getTypeIndex(@NotNull String indexName) {
        return gdb.index().forNodes(indexName);
    }

    @NotNull public String getGridName() {
        return gridName;
    }

    public void failure() {
        tx.failure();
    }

    public void success() {
        tx.success();
    }

    public void finish() {
        tx.finish();
    }

    @NotNull protected static String classNick(Class c) {
        return c.getSimpleName();
    }
}
