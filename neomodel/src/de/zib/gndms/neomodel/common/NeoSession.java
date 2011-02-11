package de.zib.gndms.neomodel.common;

import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.gorfx.NeoOfferType;
import de.zib.gndms.neomodel.gorfx.NeoTask;
import org.apache.lucene.search.NumericRangeQuery;
import org.jetbrains.annotations.NotNull;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 03.02.11
 * Time: 16:16
 * To change this template use File | Settings | File Templates.
 */
public class NeoSession {
    private static final String OFFER_TYPE_T = classNickName(NeoOfferType.class);
    private static final String TASK_T = classNickName(NeoTask.class);

    private final @NotNull GraphDatabaseService gdb;
    private final @NotNull Transaction tx;
    private final @NotNull String gridName;
    private final @NotNull NeoReprSession reprSession;

    public NeoSession(@NotNull NeoDao dao, @NotNull String gridName, @NotNull GraphDatabaseService gdb) {
        this.gdb          =    gdb;
        this.tx           = gdb.beginTx();
        this.gridName     = gridName;
        this.reprSession  = new NeoReprSession(dao, this);
    }


    public NeoOfferType createOfferType() {
        final Node node = gdb.createNode();
        final NeoOfferType offerType = new NeoOfferType(reprSession, OFFER_TYPE_T, node);
        offerType.onCreate(reprSession);
        return offerType;
    }

    @NotNull public NeoOfferType findOfferType(@NotNull String offerTypeId) {
        return new NeoOfferType(reprSession, OFFER_TYPE_T,
                getTypeIndex(OFFER_TYPE_T).get(gridName, offerTypeId).getSingle());
    }
    
    public NeoTask createTask() {
        final Node node = gdb.createNode();
        final NeoTask neoTask = new NeoTask(reprSession, TASK_T, node);
        neoTask.onCreate(reprSession);
        return neoTask;
    }

    @NotNull public NeoTask findTask(@NotNull String taskId) {
        return new NeoTask(reprSession, TASK_T,
                getTypeIndex(TASK_T).get(gridName, taskId).getSingle());
    }


    public Iterable<NeoTask> listTasksByState(@NotNull TaskState state) {
        final Index<Node> index = gdb.index().forNodes(typeIndexNickName(TASK_T, NeoTask.TASK_STATE_IDX));
        final IndexHits<Node> query = index.get(getGridName(), state.name());
        final List<NeoTask> list = new LinkedList<NeoTask>();
        for (Node node: query)
            list.add(new NeoTask(reprSession, getGridName(), node));
        return list;
    }

    public Iterable<NeoTask> listTasks() {
        final Index<Node> index = gdb.index().forNodes(ModelNode.TYPE_INDEX_IDX);
        final IndexHits<Node> query = index.get(getGridName(), classNickName(NeoTask.class));
        final List<NeoTask> list = new LinkedList<NeoTask>();
        for (Node node: query)
            list.add(new NeoTask(reprSession, getGridName(), node));
        return list;
    }

    @SuppressWarnings({"UnnecessaryLocalVariable"})
    public Iterable<NeoTask> listTasksDeadBeforeTimeout(@NotNull Calendar timeout) {
        final Index<Node> index = gdb.index().forNodes(typeIndexNickName(TASK_T, NeoTask.TERMINATION_TIME_IDX));
        final IndexHits<Node> query =
                index.query(NumericRangeQuery.newLongRange(getGridName(), 0L, timeout.getTimeInMillis(), true, false));
        final List<NeoTask> list = new LinkedList<NeoTask>();
        for (Node node: query)
            list.add(new NeoTask(reprSession, getGridName(), node));
        return list;
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

    @NotNull protected static String classNickName(Class c) {
        return c.getSimpleName();
    }

    @NotNull protected static String typeIndexNickName(String typeNick, String... names) {
        return ModelGraphElement.getTypeNickIndexName(typeNick, names);
    }

    public <U extends PropertyContainer> void setSingleIndex(@NotNull Index<U> index, @NotNull U repr,
                                                             String key, String oldVal, String newVal) {
        if (oldVal != null)
            index.remove(repr, key, oldVal);

        if (index.get(key, newVal).size() > 0) {
            this.failure();
            throw new IllegalArgumentException("Graph element already exists");
        }
        index.add(repr, key, newVal);
    }
}
