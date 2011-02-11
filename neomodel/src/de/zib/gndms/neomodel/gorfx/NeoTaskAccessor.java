package de.zib.gndms.neomodel.gorfx;

import de.zib.gndms.model.common.GridResourceItf;
import de.zib.gndms.model.common.PermissionInfo;
import de.zib.gndms.model.common.PersistentContract;
import de.zib.gndms.model.common.TimedGridResourceItf;
import de.zib.gndms.model.gorfx.types.TaskState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 11.02.11
 * Time: 14:02
 * To change this template use File | Settings | File Templates.
 */
public interface NeoTaskAccessor extends GridResourceItf, TimedGridResourceItf {

    @NotNull String getId();

    @NotNull String getWID();

    @NotNull String getDescription();

    @Nullable NeoOfferType getOfferType();

    @Nullable Calendar getTerminationTime();

    @NotNull TaskState getTaskState();

    int getMaxProgress();

    int getProgress();

    @NotNull NeoTaskAccessor getRootTask();

    boolean hasParent();

    @Nullable NeoTaskAccessor getParent();

    boolean isRootTask();

    boolean isSubTask();

    @NotNull Iterable<? extends NeoTaskAccessor> getSubTasks();

    @NotNull Iterable<? extends NeoTaskAccessor> getSubTasks(NeoOfferType ot);

    Serializable getORQ();

    @NotNull PersistentContract getContract();

    boolean isBroken();

    @NotNull PermissionInfo getPermissionInfo();

    @NotNull byte[] getSerializedCredential();

    @Nullable Serializable getPayload();

    boolean isDone();

    @Nullable String getFaultString();
}
