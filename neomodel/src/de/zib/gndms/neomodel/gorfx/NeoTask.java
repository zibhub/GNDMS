package de.zib.gndms.neomodel.gorfx;

import de.zib.gndms.model.common.PermissionInfo;
import de.zib.gndms.model.common.PersistentContract;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.NeoReprSession;
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
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 03.02.11
 * Time: 15:28
 * To change this template use File | Settings | File Templates.
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
    public static final String DONE_P = "DONE_P";
    public static final String BROKEN_P = "BROKEN_P";
    public static final String POST_MORTEM_P = "POST_MORTEM_P";
    public static final String FAULT_STRING_P = "FAULT_STRING_P";
    public static final String TASK_STATE_IDX = "taskStateIdx";
    public static final String TERMINATION_TIME_IDX = "terminationTimeIdx";

    public static enum TaskRelationships implements RelationshipType {
        OFFER_TYPE_REL,
        PARENT_REL
    }



    public NeoTask(@NotNull NeoReprSession session, @NotNull String typeNick, @NotNull Node underlying) {
        super(session, typeNick, underlying);
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

    public boolean isDone() {
        return (Boolean) getProperty(DONE_P, false);
    }

    public void setDone(boolean doneState) {
        setProperty(DONE_P, doneState);
    }

    public boolean isBroken() {
        return (Boolean) getProperty(BROKEN_P, false);
    }

    public void setBroken(boolean brokenState) {
        setProperty(BROKEN_P, brokenState);
    }

    public boolean isPostMortem() {
        return (Boolean) getProperty(POST_MORTEM_P);
    }

    public void setPostMortem(boolean postMortem) {
        setProperty(POST_MORTEM_P, postMortem);
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
}




