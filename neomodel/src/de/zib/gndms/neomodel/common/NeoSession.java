package de.zib.gndms.neomodel.common;

import de.zib.gndms.neomodel.gorfx.NeoOfferType;
import org.jetbrains.annotations.NotNull;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
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

    private final @NotNull GraphDatabaseService gdb;
    private final @NotNull Transaction tx;
    private final @NotNull String gridName;
    private final @NotNull
    NeoReprSession reprSession;

    public NeoSession(@NotNull String gridName, @NotNull GraphDatabaseService gdb) {
        this.gdb          =    gdb;
        this.tx           = gdb.beginTx();
        this.gridName     = gridName;
        this.reprSession = new NeoReprSession(this);
    }


    public NeoOfferType createOfferType() {
        final Node node = gdb.createNode();
        return new NeoOfferType(reprSession, OFFER_TYPE_T, node);
    }

    @NotNull public NeoOfferType findOfferType(@NotNull String offerTypeId) {
        return new NeoOfferType(reprSession, OFFER_TYPE_T,
                getTypeIndex(OFFER_TYPE_T).get(gridName, offerTypeId).getSingle());
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

    public <U extends PropertyContainer> void setSingleIndex(@NotNull Index<U> index, @NotNull U repr,
                                                             String key, String oldVal, String newVal) {
        if (oldVal != null)
            index.remove(repr, key, oldVal);

        if (index.get(key, newVal).size() > 0) {
            this.failure();
            throw new IllegalArgumentException("Node already exists");
        }
        index.add(repr, key, newVal);
    }
}
