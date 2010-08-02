package de.zib.gndms.infra.action;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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



import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.infra.system.SystemHolder;
import de.zib.gndms.logic.model.config.ConfigAction;
import org.jetbrains.annotations.NotNull;


/**
 * A SystemAction is a ConfigAction with a <tt>GNDMSystem</tt> instance.
 *
 * The template parameter is the return type.
 *
 * @see GNDMSystem
 * @see ConfigAction
 * @author: try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 22.08.2008 Time: 16:33:53
 */
public abstract class SystemAction<R> extends ConfigAction<R> implements SystemHolder {
    private GNDMSystem system;

    protected SystemAction() {
    }


    @Override
    public void initialize() {
        super.initialize();    // Overridden method
        if (system == null)
            throw new IllegalStateException("Missing system");
    }


    @NotNull
    public GNDMSystem getSystem() {
        return system;
    }

    public void setSystem(@NotNull final GNDMSystem systemParam) {
        system = systemParam;
    }
}
