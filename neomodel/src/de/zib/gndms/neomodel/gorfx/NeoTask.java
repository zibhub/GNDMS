package de.zib.gndms.neomodel.gorfx;

import de.zib.gndms.model.common.PermissionInfo;
import de.zib.gndms.model.common.PersistentContract;
import de.zib.gndms.model.common.TimedGridResourceItf;
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
public class NeoTask extends NodeGridResource implements TimedGridResourceItf {
    private static final String TASK_STATE_P = "TASK_STATE_P";
    private static final String WID_P = "WID_P";
    private static final String PERMISSION_INFO_P = "PERMISSION_INFO_P";
    private static final String PROGRESS_P = "PROGRESS_P";
    private static final String MAX_PROGRESS_P = "MAX_PROGRESS_P";
    private static final String FAULT_STRING_P = "FAULT_STRING_P";
    private static final String DONE_P = "DONE_P";
    private static final String BROKEN_P = "BROKEN_P";
    private static final String DESCRIPTION_P = "DESCRIPTION_P";
    private static final String ORQ_P = "ORQ_P";
    private static final String CONTRACT_P = "CONTRACT_P";
    private static final String SERIALIZED_CREDENTIAL_P = "SERIALIZED_CREDENTIAL_P";
    private static final String POST_MORTEM_P = "POST_MORTEM_P";
    private static final String TERMINATION_TIME_IDX = "terminationTimeIdx";
    private static final String TERMINATION_TIME_P = "TERMINATION_TIME_P";


    public static enum TaskRelationships implements RelationshipType {
        OFFER_TYPE_REL,
        SUB_TASK_TYPE_REL
    }

    public NeoTask(@NotNull NeoReprSession session, @NotNull String typeNick, @NotNull Node underlying) {
        super(session, typeNick, underlying);
    }

    @Override
    final public @NotNull String getId() {
        return super.getId();
    }

    @Override
    final public void setId(@NotNull String id) {
        super.setId(id);
        session().setSingleIndex(getTypeNickIndex(), repr(), session().getGridName(), getId(), id);
    }

    public @NotNull TaskState getTaskState() {
        return Enum.valueOf(TaskState.class, (String) getProperty(TASK_STATE_P));
    }

    public void setTaskState(@NotNull TaskState taskState) {
        setProperty(TASK_STATE_P, taskState.name());
    }

    public @NotNull byte[] getSerializedCredential() {
        return (byte[]) getProperty(SERIALIZED_CREDENTIAL_P);
    }

    public void setSerializedCredential(@NotNull byte[] serializedCredential) {
        setProperty(SERIALIZED_CREDENTIAL_P, serializedCredential);
    }

    public @NotNull PermissionInfo getPermissionInfo() {
        return getProperty(PermissionInfo.class, PERMISSION_INFO_P);
    }

    public void setPermissionInfo(@NotNull PermissionInfo permInfo) {
        setProperty(PermissionInfo.class, PERMISSION_INFO_P, permInfo);
    }

    public @NotNull String getWID() {
        return (String) getProperty(WID_P);
    }

    public void setWID(@NotNull String wid){
        setProperty(WID_P, wid);
    }

    public Serializable getORQ() {
        return getProperty(Serializable.class, ORQ_P);
    }

    public void setORQ(Serializable orq) {
        setProperty(Serializable.class, ORQ_P, orq);
    }

    public @NotNull PersistentContract getContract() {
        return getProperty(PersistentContract.class, CONTRACT_P);
    }


    public void setContract(@NotNull PersistentContract contract) {
        setProperty(PersistentContract.class, CONTRACT_P, contract);
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

    public void setMaxProgress(int maxProgress) {
        if (maxProgress <= 0)
            throw new IllegalArgumentException("maxProgress needs to be positive");

        setProperty(MAX_PROGRESS_P, maxProgress);
    }
    public int getMaxProgress() {
        return (Integer) getProperty(MAX_PROGRESS_P, 100);
    }

    public @Nullable String getFaultString() {
        return (String) getProperty(FAULT_STRING_P, null);
    }

    public void setFaultString(@Nullable String faultString) {
        if (faultString == null)
            repr().removeProperty(FAULT_STRING_P);
        else
            setProperty(FAULT_STRING_P, faultString);
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


    public @NotNull String getDescription() {
        return (String) getProperty(DESCRIPTION_P, "");
    }

    public void setDescription(@NotNull String description) {
        setProperty(DESCRIPTION_P, description);
    }

    public boolean isPostMortem() {
        return (Boolean) getProperty(POST_MORTEM_P);
    }

    public void setPostMortem(boolean postMortem) {
        setProperty(POST_MORTEM_P, postMortem);
    }

    public @Nullable NeoOfferType getOfferType() {
        final Relationship rel = repr().getSingleRelationship(TaskRelationships.OFFER_TYPE_REL, Direction.OUTGOING);
        return (rel == null) ? null : new NeoOfferType(getReprSession(), session().getGridName(), rel.getEndNode());
    }

    public void setOfferType(@Nullable NeoOfferType offerType) {
        final Relationship rel = repr().getSingleRelationship(TaskRelationships.OFFER_TYPE_REL, Direction.OUTGOING);
        if (rel != null)
            rel.delete();
        if (offerType != null)
            repr().createRelationshipTo(offerType.repr(getReprSession()), TaskRelationships.OFFER_TYPE_REL);
    }


    public @NotNull Iterable<NeoTask> getSubTasks() {
        final List<NeoTask> subTasks = new LinkedList<NeoTask>();
        for (Relationship rel : repr().getRelationships(TaskRelationships.SUB_TASK_TYPE_REL, Direction.OUTGOING))
            subTasks.add( new NeoTask(getReprSession(), getTypeNick(), rel.getEndNode()) );
        return subTasks;
    }

    public @Nullable NeoTask getParent() {
        final Relationship rel = repr().getSingleRelationship(TaskRelationships.SUB_TASK_TYPE_REL, Direction.INCOMING);
        if (rel == null)
            return null;
        else
            return new NeoTask(getReprSession(), getTypeNick(), rel.getEndNode());
    }

    public void setParent(@Nullable NeoTask task) {
        final Relationship rel = repr().getSingleRelationship(TaskRelationships.SUB_TASK_TYPE_REL, Direction.INCOMING);
        if (rel != null)
            rel.delete();
        if (task != null)
            repr().createRelationshipTo(task.repr(getReprSession()), TaskRelationships.SUB_TASK_TYPE_REL);
    }

    public @Nullable Calendar getTerminationTime() {
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
            index.remove(repr(), session().getGridName(), oldTerminationTime.getTimeInMillis());
        }
        if (terminationTime == null){
            repr().removeProperty(TERMINATION_TIME_IDX);
        }
        else {
            setProperty(TERMINATION_TIME_P, terminationTime.getTimeInMillis());
            final Index<Node> index = getTypeNickIndex(TERMINATION_TIME_IDX);
            index.add(repr(), session().getGridName(), terminationTime.getTimeInMillis());

        }
    }

    @Override
    public void delete() {
        getTypeNickIndex().remove(repr(), session().getGridName(), getId());
        super.delete();
    }
}




