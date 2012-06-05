package de.zib.gndms.neomodel.gorfx;

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

import de.zib.gndms.model.common.PermissionInfo;
import de.zib.gndms.model.common.PersistentContract;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.NodeGridResource;
import de.zib.gndms.neomodel.common.ReprSession;
import de.zib.gndms.neomodel.common.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.index.Index;
import org.neo4j.index.impl.lucene.ValueContext;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Task
 *
 * @author  try ste fan pla nti kow zib
 *
 * User: stepn Date: 05.09.2008 Time: 14:48:36
 */
public class Task extends NodeGridResource<TaskAccessor> implements TaskAccessor {

    public static final String RESOURCE_ID_P = "RESOURCE_ID_P";
    public static final String WID_P = "WID_P";
    public static final String DESCRIPTION_P = "DESCRIPTION_P";
    public static final String TERMINATION_TIME_P = "TERMINATION_TIME_P";
    public static final String TASK_STATE_P = "TASK_STATE_P";
    public static final String ALT_TASK_STATE_P = "ALT_TASK_STATE_P";
    public static final String MAX_PROGRESS_P = "MAX_PROGRESS_P";
    public static final String PROGRESS_P = "PROGRESS_P";
    public static final String ORQ_P = "ORQ_P";
    public static final String CONTRACT_P = "CONTRACT_P";
    public static final String PERMISSION_INFO_P = "PERMISSION_INFO_P";
    public static final String SERIALIZED_CREDENTIAL_P = "SERIALIZED_CREDENTIAL_P";
    public static final String PAYLOAD_P = "PAYLOAD_P";
    public static final String CAUSE_P = "CAUSE_P";
    public static final String DONE_P = "DONE_P";
    public static final String BROKEN_P = "BROKEN_P";
    public static final String FAULT_STRING_P = "FAULT_STRING_P";
    public static final String TASK_STATE_IDX = "taskStateIdx";
    public static final String TERMINATION_TIME_IDX = "terminationTimeIdx";
    public static final String RESOURCE_ID_IDX = "resourceIdIdx" ;



    public static enum TaskRelationships implements RelationshipType {
        OFFER_TYPE_REL,
        PARENT_REL
    }



    public Task(@NotNull ReprSession session, @NotNull String typeNick, @NotNull Node underlying) {
        super(session, typeNick, underlying);
    }


    @Override
    public void onCreate(ReprSession reprSession) {
        super.onCreate(reprSession);
        if (getProperty(TASK_STATE_P, null) == null)   {
            final @NotNull String newTaskStateName = TaskState.CREATED.name();
            setProperty(TASK_STATE_P, newTaskStateName);
            final @NotNull Index<Node> typeNickIndex = getTypeNickIndex(TASK_STATE_IDX);
            typeNickIndex.add(repr(), session().getGridName(), newTaskStateName);
        }
    }

    final public @NotNull String getId() {
        return super.getId();
    }

    @Override
    final public void setId(@NotNull String id) {
        super.setId( id );
        session().setSingleIndex( getTypeNickIndex(), repr(), session().getGridName(), getId(), id );
    }

    public String getResourceId() {
        return (String) repr().getProperty( RESOURCE_ID_P, null);
    }

    public void setResourceId(String id) {

        final @NotNull Index<Node> typeNickIndex = getTypeNickIndex(RESOURCE_ID_IDX);
        if( getResourceId() != null )
            typeNickIndex.remove(repr(), session().getGridName(), getResourceId() );
        typeNickIndex.add( repr(), session().getGridName(), id );

        repr().setProperty( RESOURCE_ID_P, id);
    }

    public @NotNull String getWID() {
        return (String) getProperty(WID_P);
    }

    public void setWID(@NotNull String wid){
        setProperty(WID_P, wid);
    }

    public @NotNull String getDescription() {
        return (String) getProperty(DESCRIPTION_P, "");
    }


    public void setDescription(@NotNull String description) {
        setProperty(DESCRIPTION_P, description);
    }

    public TaskFlowType getTaskFlowType() {
        final Relationship rel = repr().getSingleRelationship(TaskRelationships.OFFER_TYPE_REL, Direction.OUTGOING);
        if (rel == null)
            return null;
        else
            return new TaskFlowType(reprSession(), session().getGridName(), rel.getEndNode());
    }

    public void setTaskFlowType( @Nullable TaskFlowType taskFlowType ) {
        final Relationship rel = repr().getSingleRelationship(TaskRelationships.OFFER_TYPE_REL, Direction.OUTGOING);
        if (rel != null)
            rel.delete();
        if ( taskFlowType != null)
            repr().createRelationshipTo( taskFlowType.repr(getReprSession()), TaskRelationships.OFFER_TYPE_REL);
    }

    public @Nullable
    DateTime getTerminationTime() {
        final Long terminationTime = (Long) getProperty(TERMINATION_TIME_P, null);
        if (terminationTime == null)
            return null;
        else {
            return new DateTime( terminationTime );
        }
    }

    public void setTerminationTime(@Nullable DateTime terminationTime) {
        final DateTime oldTerminationTime = getTerminationTime();
        if (oldTerminationTime != null) {
            final Index<Node> index = getTypeNickIndex(TERMINATION_TIME_IDX);
            index.remove(repr(), session().getGridName(),
                    new ValueContext( oldTerminationTime.getMillis() ).indexNumeric());
        }

        if (terminationTime == null && hasProperty(TERMINATION_TIME_P)) {
            removeProperty(TERMINATION_TIME_P);
        }
        else if ( terminationTime != null ) {
            setProperty(TERMINATION_TIME_P, terminationTime.getMillis());
            final Index<Node> index = getTypeNickIndex(TERMINATION_TIME_IDX);
            index.add(repr(), session().getGridName(),
                    new ValueContext( terminationTime.getMillis() ).indexNumeric());
        }
    }

    public @NotNull TaskState getTaskState() {
        return Enum.valueOf(TaskState.class, (String) getProperty(TASK_STATE_P, TaskState.CREATED.name()));
    }

    public void setTaskState(@NotNull TaskState newTaskState) {
        final @NotNull String oldTaskStateName = getTaskState().name();
        final @NotNull String newTaskStateName = newTaskState.name();
        setProperty(TASK_STATE_P, newTaskStateName);
        final @NotNull Index<Node> typeNickIndex = getTypeNickIndex(TASK_STATE_IDX);
        typeNickIndex.remove(repr(), session().getGridName(), oldTaskStateName);
        typeNickIndex.add(repr(), session().getGridName(), newTaskStateName);
    }

    public @Nullable TaskState getAltTaskState() {
        final String property = (String) getProperty(ALT_TASK_STATE_P, null);
        return property == null ? null : Enum.valueOf(TaskState.class, property);
    }

    public void setAltTaskState(@Nullable TaskState newAltTaskState) {
        if (null == newAltTaskState) {
            if (hasProperty(ALT_TASK_STATE_P))
                removeProperty(ALT_TASK_STATE_P);
        }
        else
            setProperty(ALT_TASK_STATE_P, newAltTaskState.name());
    }

    public int getMaxProgress() {
        return (Integer) getProperty(MAX_PROGRESS_P, 100);
    }

    public void setMaxProgress(int maxProgress) {
        if (maxProgress <= 0)
            throw new IllegalArgumentException("maxProgress needs to be positive");

        setProperty(MAX_PROGRESS_P, maxProgress);
    }

    public int getProgress() {
        return (Integer) getProperty(PROGRESS_P, 0);
    }

    public void setProgress(int progress) {
        if (progress < 0 || progress > getMaxProgress())
            throw new IllegalArgumentException("Progress out of bounds");
        else
            setProperty(PROGRESS_P, progress);
    }

    public @NotNull Task getRootTask() {
        final @Nullable Task parent = getParent();
        if (parent == null) return this; else return parent.getRootTask();
    }

    public final boolean hasParent() {
        return getParent() != null;
    }


    public @Nullable Task getParent() {
        final Relationship rel = repr().getSingleRelationship(TaskRelationships.PARENT_REL, Direction.OUTGOING);
        if (rel == null)
            return null;
        else
            return new Task(getReprSession(), getTypeNick(), rel.getEndNode());
    }

    public void setParent(@Nullable Task parent) {
        if (parent != null && parent.isBelowTask(this))
            throw new IllegalArgumentException("New parent may not be a parent-chain child of this parent");
        final Relationship rel = repr().getSingleRelationship(TaskRelationships.PARENT_REL, Direction.OUTGOING);
        if (rel != null)
            rel.delete();
        if (parent != null)
            repr().createRelationshipTo(parent.repr(getReprSession()), TaskRelationships.PARENT_REL);
    }

    public boolean isRootTask() {
        return getParent() == null;
    }

    public final boolean isSubTask() {
        return hasParent();
    }


    public boolean isBelowTask(@NotNull Task task) {
        TaskAccessor parent = this;
        while (parent != null) {
            if (task.equals(parent))
                return true;
            else
                parent = parent.getParent();

        }
        return false;
    }


    public @NotNull Iterable<Task> getSubTasks() {
        final List<Task> subTasks = new LinkedList<Task>();
        for (Relationship rel : repr().getRelationships(TaskRelationships.PARENT_REL, Direction.INCOMING))
            subTasks.add( new Task(getReprSession(), getTypeNick(), rel.getStartNode()) );
        return subTasks;
    }

    public @NotNull Iterable<Task> getSubTasks(final TaskFlowType ot) {
        final List<Task> subTasks = new LinkedList<Task>();
        for (Relationship rel : repr().getRelationships(TaskRelationships.PARENT_REL, Direction.INCOMING)) {
            final Task task        = new Task(getReprSession(), getTypeNick(), rel.getStartNode());
            final TaskFlowType taskOT = task.getTaskFlowType();
            if ((ot == null) ? taskOT == null : ot.equals(taskOT))
                subTasks.add(task);
        }
        return subTasks;
    }

    // TODO: do not clone payload / order (?)
    public Task createSubTask() {
        Task subTask = session().createTask();
        Relationship rel = subTask.repr(reprSession()).createRelationshipTo(repr(),
                TaskRelationships.PARENT_REL);
        subTask.setTaskFlowType( getTaskFlowType() );
        subTask.setContract(getContract());
        subTask.setDescription(getDescription());
        subTask.setPayload(getPayload());
        subTask.setFaultString(getFaultString());
        subTask.setMaxProgress(getMaxProgress());
        subTask.setOrder(getOrder());
        subTask.setWID(getWID());
        subTask.setPermissionInfo(getPermissionInfo());
        return subTask;
    }


    public Serializable getOrder( ) {
        return getProperty(Serializable.class, ORQ_P);
    }

    public void setOrder(Serializable orq) {
        setProperty(Serializable.class, ORQ_P, orq);
    }

    public @NotNull PersistentContract getContract() {
        return getProperty(PersistentContract.class, CONTRACT_P);
    }


    public void setContract(@NotNull PersistentContract contract) {
        setProperty(PersistentContract.class, CONTRACT_P, contract);
    }

    public boolean isBroken() {
        return (Boolean) getProperty(BROKEN_P, false);
    }

    public void setBroken(boolean brokenState) {
        setProperty(BROKEN_P, brokenState);
    }

    public @NotNull PermissionInfo getPermissionInfo() {
        return getProperty(PermissionInfo.class, PERMISSION_INFO_P);
    }

    public void setPermissionInfo(@NotNull PermissionInfo permInfo) {
        setProperty(PermissionInfo.class, PERMISSION_INFO_P, permInfo);
    }

    public @NotNull byte[] getSerializedCredential() {
        return (byte[]) getProperty(SERIALIZED_CREDENTIAL_P);
    }

    public void setSerializedCredential(@NotNull byte[] serializedCredential) {
        setProperty(SERIALIZED_CREDENTIAL_P, serializedCredential);
    }

    public @Nullable Serializable getPayload() {
        return getProperty(Serializable.class, PAYLOAD_P, null);
    }
    public void setPayload(final @Nullable Serializable payload) {
        if (payload == null && hasProperty(PAYLOAD_P))
            removeProperty(PAYLOAD_P);
        else
            setProperty(Serializable.class, PAYLOAD_P, payload);
    }

    public @Nullable LinkedList<Exception> getCause() {
        return (LinkedList<Exception>) getProperty(LinkedList.class, CAUSE_P, null);
    }

    public void setCause(final @Nullable LinkedList<Exception> cause) {
        if (cause == null && hasProperty(CAUSE_P))
            removeProperty(CAUSE_P);
        else
            setProperty(LinkedList.class, CAUSE_P, cause);
    }


    public @NotNull LinkedList<Exception> addCause(final @NotNull Exception exception) {
        final LinkedList<Exception> oldList = getCause();
        final LinkedList<Exception> newList = oldList == null ? new LinkedList<Exception>() : oldList;
        newList.add(exception);
        try {
            setCause( newList );
        }
        // not serializable? serialize it by hand..
        catch( RuntimeException perhapsNotSerializable ) {
            if( null == perhapsNotSerializable.getCause() )
                throw perhapsNotSerializable;
            if( !( perhapsNotSerializable.getCause() instanceof NotSerializableException) )
                throw perhapsNotSerializable;

            StringWriter s = new StringWriter();
            PrintWriter w = new PrintWriter( s );

            Throwable throwable = exception;
            while( throwable != null ) {
                if( throwable != exception ) {
                    w.print( "Caused by: " );
                }

                w.print( throwable.getClass().getCanonicalName() );
                w.print( ": " );

                w.println(throwable.getMessage());

                throwable.printStackTrace( w );

                throwable = throwable.getCause();
            }

            w.flush();
            newList.remove( newList.size()-1 );
            newList.add( new Exception( s.toString() ) );

            setCause( newList );
        }

        return newList;
    }

    public boolean isDone() {
        return (Boolean) getProperty(DONE_P, false);
    }

    public void setDone(boolean doneState) {
        if (isDone())
            if (! doneState)
                throw new IllegalArgumentException("Illegal setDone(false) after setDone(true)");
        setProperty(DONE_P, doneState);
    }

    public @Nullable String getFaultString() {
        return (String) getProperty(FAULT_STRING_P, null);
    }

    public void setFaultString(@Nullable String faultString) {
        if( faultString == null ) {
            if( hasProperty(FAULT_STRING_P) )
                removeProperty(FAULT_STRING_P);
        }
        else
            setProperty(FAULT_STRING_P, faultString);
    }

    @Override public void delete() {
        getTypeNickIndex().remove(repr(), session().getGridName(), getId());
        super.delete();
    }

    public Taskling getTaskling() {
        return new Taskling(reprSession().getDao(), getId());
    }
    
    public TaskAccessor getSnapshot() {
        final Session session = session();
        try {
            final String id = this.getId();
            final String resourceId = this.getResourceId();
            final String WID = this.getWID();
            final String descr = this.getDescription();
            final DateTime terminationTime = this.getTerminationTime();
            final TaskState taskState = this.getTaskState();
            final int maxProgress = this.getMaxProgress();
            final int progress = this.getProgress();
            final boolean hasParent = this.hasParent();
            final boolean isBroken = this.isBroken();
            final boolean isRootTask = this.isRootTask();
            final boolean isSubTask = this.isSubTask();
            final boolean isDone = this.isDone();
            final Serializable orq = this.getOrder( );
            final PermissionInfo permInfo = this.getPermissionInfo();
            final PersistentContract contract = this.getContract();
            final byte[] cred = this.getSerializedCredential();
            final String faultString = this.getFaultString();
            final Serializable payload = this.getPayload();
            final LinkedList<Exception> causes = this.getCause();
            final TaskAccessor snapshot = new TaskAccessor() {
                @NotNull
                public String getId() {
                    return id;
                }


                public String getResourceId() {
                    return resourceId;
                }


                @NotNull
                public String getWID() {
                    return WID;
                }

                @NotNull
                public String getDescription() {
                    return descr;
                }

                public TaskFlowType getTaskFlowType() throws UnsupportedOperationException {
                    throw new UnsupportedOperationException();
                }

                public DateTime getTerminationTime() {
                    return terminationTime;
                }

                @NotNull
                public TaskState getTaskState() {
                    return taskState;
                }

                public int getMaxProgress() {
                    return maxProgress;
                }

                public int getProgress() {
                    return progress;
                }

                @NotNull
                public TaskAccessor getRootTask() throws UnsupportedOperationException  {
                    throw new UnsupportedOperationException();
                }

                public boolean hasParent() {
                    return hasParent;
                }

                public TaskAccessor getParent() throws UnsupportedOperationException  {
                    throw new UnsupportedOperationException();
                }

                public boolean isRootTask() {
                    return isRootTask;
                }

                public boolean isSubTask() {
                    return isSubTask;
                }

                @NotNull
                public Iterable<? extends TaskAccessor> getSubTasks() {
                    throw new UnsupportedOperationException();
                }

                @NotNull
                public Iterable<? extends TaskAccessor> getSubTasks(TaskFlowType ot) {
                    throw new UnsupportedOperationException();
                }

                public Serializable getOrder( ) {
                    return orq;
                }

                @NotNull
                public PersistentContract getContract() {
                    return contract;
                }

                public boolean isBroken() {
                    return isBroken;
                }

                @NotNull
                public PermissionInfo getPermissionInfo() {
                    return permInfo;
                }

                @NotNull
                public byte[] getSerializedCredential() {
                    return cred;
                }

                public Serializable getPayload() {
                    return payload;
                }

                public Iterable<Exception> getCause() {
                    return causes;
                }

                public boolean isDone() {
                    return isDone;
                }

                public String getFaultString() {
                    return faultString;
                }

                public void setTerminationTime( DateTime terminationTime) throws UnsupportedOperationException {
                    throw new UnsupportedOperationException();
                }

                public void setId(String id) throws UnsupportedOperationException {
                    throw new UnsupportedOperationException();
                }
            };
            session.finish();
            return snapshot;
        }
        finally { session.success(); }
    }


    /**
     * Deletes a task and all of his sub tasks
     * @param task
     * @param session
     */
    public static void fullDelete( Task task, Session session ) {

        ArrayList<Task> subtasks = new ArrayList<Task>( 10 );
        collectTasks( subtasks, task,  session );
        for ( Task subtask : subtasks )
            subtask.delete( session );

        task.delete( session );
    }


    private static void collectTasks( List<Task> subtasks, Task task, Session session ) {

        for ( Task subtask : task.getSubTasks() )  {
            if ( subtasks.contains( subtask ) )
                continue;
            subtasks.add( subtask );
            collectTasks( subtasks, subtask, session );
        }
    }
}




