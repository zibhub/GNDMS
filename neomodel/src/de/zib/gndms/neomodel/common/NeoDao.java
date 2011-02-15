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

import de.zib.gndms.neomodel.gorfx.NeoOfferType;
import org.jetbrains.annotations.NotNull;
import org.neo4j.graphdb.GraphDatabaseService;

/**
 * NeoDao
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 * User: stepn Date: 05.09.2008 Time: 14:48:36
 */
public class NeoDao {
    @NotNull private GraphDatabaseService gdb;
    @NotNull private String gridName;

    public NeoDao(@NotNull String gridName, @NotNull GraphDatabaseService gdb) {
        this.gridName = gridName;
        this.gdb      = gdb;
    }

    public NeoSession beginSession() {
        return new NeoSession(this, gridName, gdb);
    }

    @NotNull
    public String getGridName() {
        return gridName;
    }
}
