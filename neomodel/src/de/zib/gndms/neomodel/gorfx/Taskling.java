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

import de.zib.gndms.model.common.GridEntity;
import de.zib.gndms.model.common.GridResourceItf;
import de.zib.gndms.neomodel.common.NeoDao;
import de.zib.gndms.neomodel.common.NeoSession;
import org.jetbrains.annotations.NotNull;

/**
 * Taskling
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 * User: stepn Date: 05.09.2008 Time: 14:48:36
 */
public class Taskling extends GridEntity implements GridResourceItf {
    final @NotNull NeoDao dao;
    @NotNull String id;

    public Taskling(@NotNull NeoDao dao, @NotNull String taskId) {
        this.id = taskId;
        this.dao    = dao;
    }

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    @NotNull NeoDao getDao() {
        return dao;
    }

    @NotNull
    public NeoTask getTask(@NotNull NeoSession neoSession) {
        return neoSession.findTask(getId());
    }
}
