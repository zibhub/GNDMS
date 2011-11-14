package de.zib.gndms.neomodel.common;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.gorfx.TaskFlowType;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.TaskFlowType;
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
 * Session
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 * User: stepn Date: 05.09.2008 Time: 14:48:36
 */
public class Session {
    private static final String OFFER_TYPE_T = classNickName(TaskFlowType.class);
    private static final String TASK_T = classNickName(Task.class);

    private final @NotNull GraphDatabaseService gdb;
    private final @NotNull Transaction tx;
    private final @NotNull String gridName;
    private final @NotNull
    ReprSession reprSession;

    public Session(@NotNull Dao dao, @NotNull String gridName, @NotNull GraphDatabaseService gdb) {
        this.gdb          = gdb;
        this.tx           = gdb.beginTx();
        this.gridName     = gridName;
        this.reprSession  = new ReprSession(dao, this);
    }


    public TaskFlowType createTaskFlowType() {
        final Node node = gdb.createNode();
        final TaskFlowType offerType = new TaskFlowType(reprSession, OFFER_TYPE_T, node);
        offerType.onCreate(reprSession);
        return offerType;
    }

    @NotNull public TaskFlowType findTaskFlowType( @NotNull String offerTypeId ) {
        return new TaskFlowType(reprSession, OFFER_TYPE_T,
                getTypeIndex(OFFER_TYPE_T).get(gridName, offerTypeId).getSingle());
    }
    
    public Task createTask() {
        final Node node = gdb.createNode();
        final Task task = taskFromNode( node );
        task.onCreate(reprSession);
        return task;
    }

    @NotNull public Task findTask(@NotNull String taskId) {
        return taskFromNode( getTypeIndex(TASK_T).get(gridName, taskId).getSingle() );
    }


    private Task taskFromNode( Node node ) {
        if( node != null )
            return new Task(reprSession, TASK_T, node );
        else
            return null;
    }


    public Task findTaskForResource ( @NotNull String resourceId ) {
        Index<Node> idx = getTypeIndex( typeIndexNickName( TASK_T, Task.RESOURCE_ID_IDX ) );
        return taskFromNode( idx.get( gridName, resourceId ).getSingle() );
    }


    public Iterable<Task> listTasksByState(@NotNull TaskState state) {
        final Index<Node> index = gdb.index().forNodes(typeIndexNickName(TASK_T, Task.TASK_STATE_IDX));
        final IndexHits<Node> query = index.get(getGridName(), state.name());
        final List<Task> list = new LinkedList<Task>();
        for (Node node: query)
            list.add(new Task(reprSession, getGridName(), node));
        return list;
    }

    public Iterable<Task> listTasks() {
        final Index<Node> index = gdb.index().forNodes(ModelNode.TYPE_INDEX_IDX);
        final IndexHits<Node> query = index.get(getGridName(), classNickName(Task.class));
        final List<Task> list = new LinkedList<Task>();
        for (Node node: query)
            list.add(new Task(reprSession, getGridName(), node));
        return list;
    }

    @SuppressWarnings({"UnnecessaryLocalVariable"})
    public Iterable<Task> listTasksDeadBeforeTimeout(@NotNull Calendar timeout) {
        final Index<Node> index = gdb.index().forNodes(typeIndexNickName(TASK_T, Task.TERMINATION_TIME_IDX));
        final IndexHits<Node> query =
                index.query(NumericRangeQuery.newLongRange(getGridName(), 0L, timeout.getTimeInMillis(), true, false));
        final List<Task> list = new LinkedList<Task>();
        for (Node node: query)
            list.add(new Task(reprSession, getGridName(), node));
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
        return ModelElement.getTypeNickIndexName(typeNick, names);
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
