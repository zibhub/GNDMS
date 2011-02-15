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
import de.zib.gndms.neomodel.common.NeoReprSession;
import de.zib.gndms.neomodel.common.NeoSession;
import de.zib.gndms.neomodel.common.NodeGridResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.index.Index;
import org.neo4j.index.impl.lucene.ValueContext;

import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * NeoTask
 *
 * @author  try ste fan pla nti kow zib
 *
 * User: stepn Date: 05.09.2008 Time: 14:48:36
 */
public class NeoTask extends NodeGridResource<NeoTaskAccessor> implements NeoTaskAccessor {
    public static final String WID_P = "WID_P";
    public static final String DESCRIPTION_P = "DESCRIPTION_P";
    public static final String TERMINATION_TIME_P = "TERMINATION_TIME_P";
    public static final String TASK_STATE_P = "TASK_STATE_P";
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

    public static enum TaskRelationships implements RelationshipType {
        OFFER_TYPE_REL,
        PARENT_REL
    }



    public NeoTask(@NotNull NeoReprSession session, @NotNull String typeNick, @NotNull Node underlying) {
        super(session, typeNick, underlying);
    }


    @Override
    public void onCreate(NeoReprSession reprSession) {
        super.onCreate(reprSession);
        if (getProperty(TASK_STATE_P, null) == null)   {
            final @NotNull String newTaskStateName = TaskState.INITIALIZED.name();
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
        super.setId(id);
        session().setSingleIndex(getTypeNickIndex(), repr(), session().getGridName(), getId(), id);
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

    public NeoOfferType getOfferType() {
        final Relationship rel = repr().getSingleRelationship(TaskRelationships.OFFER_TYPE_REL, Direction.OUTGOING);
        if (rel == null)
            return null;
        else
            return new NeoOfferType(reprSession(), session().getGridName(), rel.getEndNode());
    }

    public void setOfferType(@Nullable NeoOfferType offerType) {
        final Relationship rel = repr().getSingleRelationship(TaskRelationships.OFFER_TYPE_REL, Direction.OUTGOING);
        if (rel != null)
            rel.delete();
        if (offerType != null)
            repr().createRelationshipTo(offerType.repr(getReprSession()), TaskRelationships.OFFER_TYPE_REL);
    }

    public @Nullable
    Calendar getTerminationTime() {
        final Long terminationTime = (Long) getProperty(TERMINATION_TIME_P, null);
        if (terminationTime == null)
            return null;
        else {
            final Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(terminationTime);
            return cal;
        }
    }

    public void setTerminationTime(@Nullable Calendar terminationTime) {
        final Calendar oldTerminationTime = getTerminationTime();
        if (oldTerminationTime != null) {
            final Index<Node> index = getTypeNickIndex(TERMINATION_TIME_IDX);
            index.remove(repr(), session().getGridName(),
                    new ValueContext( oldTerminationTime.getTimeInMillis() ).indexNumeric());
        }
        if (terminationTime == null && hasProperty(TERMINATION_TIME_P)) {
            removeProperty(TERMINATION_TIME_P);
        }
        else {
            setProperty(TERMINATION_TIME_P, terminationTime.getTimeInMillis());
            final Index<Node> index = getTypeNickIndex(TERMINATION_TIME_IDX);
            index.add(repr(), session().getGridName(),
                    new ValueContext( terminationTime.getTimeInMillis() ).indexNumeric());

        }
    }

    public @NotNull TaskState getTaskState() {
        return Enum.valueOf(TaskState.class, (String) getProperty(TASK_STATE_P));
    }

    public void setTaskState(@NotNull TaskState newTaskState) {
        final @NotNull String oldTaskStateName = getTaskState().name();
        final @NotNull String newTaskStateName = newTaskState.name();
        setProperty(TASK_STATE_P, newTaskStateName);
        final @NotNull Index<Node> typeNickIndex = getTypeNickIndex(TASK_STATE_IDX);
        typeNickIndex.remove(repr(), session().getGridName(), oldTaskStateName);
        typeNickIndex.add(repr(), session().getGridName(), newTaskStateName);
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

    public @NotNull NeoTask getRootTask() {
        final @Nullable NeoTask parent = getParent();
        if (parent == null) return this; else return parent.getRootTask();
    }

    public final boolean hasParent() {
        return getParent() != null;
    }


    public @Nullable NeoTask getParent() {
        final Relationship rel = repr().getSingleRelationship(TaskRelationships.PARENT_REL, Direction.OUTGOING);
        if (rel == null)
            return null;
        else
            return new NeoTask(getReprSession(), getTypeNick(), rel.getEndNode());
    }

    public void setParent(@Nullable NeoTask parent) {
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


    public boolean isBelowTask(@NotNull NeoTask task) {
        NeoTaskAccessor parent = this;
        while (parent != null) {
            if (task.equals(parent))
                return true;
            else
                parent = parent.getParent();

        }
        return false;
    }


    public @NotNull Iterable<NeoTask> getSubTasks() {
        final List<NeoTask> subTasks = new LinkedList<NeoTask>();
        for (Relationship rel : repr().getRelationships(TaskRelationships.PARENT_REL, Direction.INCOMING))
            subTasks.add( new NeoTask(getReprSession(), getTypeNick(), rel.getStartNode()) );
        return subTasks;
    }

    public @NotNull Iterable<NeoTask> getSubTasks(final NeoOfferType ot) {
        final List<NeoTask> subTasks = new LinkedList<NeoTask>();
        for (Relationship rel : repr().getRelationships(TaskRelationships.PARENT_REL, Direction.INCOMING)) {
            final NeoTask task        = new NeoTask(getReprSession(), getTypeNick(), rel.getStartNode());
            final NeoOfferType taskOT = task.getOfferType();
            if ((ot == null) ? taskOT == null : ot.equals(taskOT))
                subTasks.add(task);
        }
        return subTasks;
    }

    public Serializable getORQ() {
        return getProperty(Serializable.class, ORQ_P);
    }

    public void setORQ(Serializable orq) {
        setProperty(Serializable.class, ORQ_P, orq);
    }

    public @NotNull
    PersistentContract getContract() {
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

    public @Nullable LinkedList<RuntimeException> getCause() {
        return (LinkedList<RuntimeException>) getProperty(LinkedList.class, CAUSE_P, null);
    }

    public void setCause(final @Nullable LinkedList<RuntimeException> cause) {
        if (cause == null && hasProperty(CAUSE_P))
            removeProperty(CAUSE_P);
        else
            setProperty(LinkedList.class, CAUSE_P, cause);
    }


    public @NotNull LinkedList<RuntimeException> addCause(final @NotNull RuntimeException exception) {
        final LinkedList<RuntimeException> oldList = getCause();
        final LinkedList<RuntimeException> newList = oldList == null ? new LinkedList<RuntimeException>() : oldList;
        newList.add(exception);
        setCause(newList);
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
        if (faultString == null && hasProperty(FAULT_STRING_P))
            removeProperty(FAULT_STRING_P);
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
    
    public NeoTaskAccessor getSnapshot() {
        final NeoSession session = session();
        try {
            final String id = this.getId();
            final String WID = this.getWID();
            final String descr = this.getDescription();
            final Calendar terminationTime = this.getTerminationTime();
            final TaskState taskState = this.getTaskState();
            final int maxProgress = this.getMaxProgress();
            final int progress = this.getProgress();
            final boolean hasParent = this.hasParent();
            final boolean isBroken = this.isBroken();
            final boolean isRootTask = this.isRootTask();
            final boolean isSubTask = this.isSubTask();
            final boolean isDone = this.isDone();
            final Serializable orq = this.getORQ();
            final PermissionInfo permInfo = this.getPermissionInfo();
            final PersistentContract contract = this.getContract();
            final byte[] cred = this.getSerializedCredential();
            final String faultString = this.getFaultString();
            final Serializable payload = this.getPayload();
            final LinkedList<RuntimeException> causes = this.getCause();
            final NeoTaskAccessor snapshot = new NeoTaskAccessor() {
                @NotNull
                public String getId() {
                    return id;
                }

                @NotNull
                public String getWID() {
                    return WID;
                }

                @NotNull
                public String getDescription() {
                    return descr;
                }

                public NeoOfferType getOfferType() {
                    throw new UnsupportedOperationException();
                }

                public Calendar getTerminationTime() {
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
                public NeoTaskAccessor getRootTask() {
                    throw new UnsupportedOperationException();
                }

                public boolean hasParent() {
                    return hasParent;
                }

                public NeoTaskAccessor getParent() {
                    throw new UnsupportedOperationException();
                }

                public boolean isRootTask() {
                    return isRootTask;
                }

                public boolean isSubTask() {
                    return isSubTask;
                }

                @NotNull
                public Iterable<? extends NeoTaskAccessor> getSubTasks() {
                    throw new UnsupportedOperationException();
                }

                @NotNull
                public Iterable<? extends NeoTaskAccessor> getSubTasks(NeoOfferType ot) {
                    throw new UnsupportedOperationException();
                }

                public Serializable getORQ() {
                    return orq;
                }

                @NotNull
                public PersistentContract getContract() {
                    return contract;
                }

                public boolean isBroken() {
                    return isBroken();
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

                public Iterable<RuntimeException> getCause() {
                    return causes;
                }

                public boolean isDone() {
                    return isDone;
                }

                public String getFaultString() {
                    return faultString;
                }

                public void setTerminationTime(Calendar terminationTime) {
                    throw new UnsupportedOperationException();
                }

                public void setId(String id) {
                    throw new UnsupportedOperationException();
                }
            };
            session.finish();
            return snapshot;
        }
        finally { session.success(); }
    }
}




