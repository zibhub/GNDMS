package de.zib.gndms.neomodel.gorfx;

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

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 03.02.11
 * Time: 15:28
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractNeoTask extends NodeGridResource implements TimedGridResourceItf {
    private static final String TASK_STATE_P = "TASK_STATE_P";

    public static enum TaskRelationships implements RelationshipType {
        OFFER_TYPE_REL
    }

    private static final String PROGRESS_P = "PROGRESS_P";
    private static final String MAX_PROGRESS_P = "MAX_PROGRESS_P";
    private static final String FAULT_STRING_P = "FAULT_STRING_P";
    private static final String DONE_P = "DONE_P";
    private static final String BROKEN_P = "BROKEN_P";
    private static final String DESCRIPTION_P = "DESCRIPTION_P";
    private static final String ORQ_P = "ORQ_P";
    private static final String CONTRACT_P = "CONTRACT_P";
    private static final String SERIALIZED_CREDENTIAL_P = "SERIALIZED_CREDENTIAL_P";

    protected AbstractNeoTask(@NotNull NeoReprSession session, @NotNull String typeNick, @NotNull Node underlying) {
        super(session, typeNick, underlying);
    }

    @Override
    final public @NotNull String getId() {
        return super.getId();
    }

    @Override
    final public void setId(@NotNull String id) {
        session().setSingleIndex(getTypeNickIndex(), repr(), session().getGridName(), getId(), id);
        super.setId(id);
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

    public Boolean isDone() {
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

    public void setString(@NotNull String description) {
        setProperty(DESCRIPTION_P, description);
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


    @Override
    public void delete() {
        getTypeNickIndex().remove(repr(), session().getGridName(), getId());
        super.delete();
    }
}




