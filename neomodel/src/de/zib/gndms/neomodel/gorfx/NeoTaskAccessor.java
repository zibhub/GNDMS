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

import de.zib.gndms.model.common.GridResourceItf;
import de.zib.gndms.model.common.PermissionInfo;
import de.zib.gndms.model.common.PersistentContract;
import de.zib.gndms.model.common.TimedGridResourceItf;
import de.zib.gndms.model.gorfx.types.TaskState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * NeoTaskAccessor
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 * User: stepn Date: 05.09.2008 Time: 14:48:36
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

    @Nullable Iterable<Exception> getCause();

    boolean isDone();

    @Nullable String getFaultString();
}
