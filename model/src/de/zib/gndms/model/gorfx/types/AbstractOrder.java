package de.zib.gndms.model.gorfx.types;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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



import de.zib.gndms.common.model.gorfx.types.Order;
import de.zib.gndms.stuff.copy.Copyable.CopyMode;
import de.zib.gndms.stuff.copy.Copyable;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * An base class for model classes representing Order's.
 *
 * @author  try ma ik jo rr a zib
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 3:38:17 PM
 */
@Copyable(CopyMode.SERIALIZE)
public abstract class AbstractOrder implements Order {
    private static final long serialVersionUID = 5782532835559987893L;
    private String taskFlowType;  ///< Type of the requested task.
    private transient boolean justEstimate; ///< Flag for the contract calculation


    public String getTaskFlowType() {
        return taskFlowType;
    }


    protected void setTaskFlowType( String URI ) {
	    taskFlowType = URI;
    }


    public void setJustEstimate( boolean flag ) {
        justEstimate = flag;
    }


    public boolean isJustEstimate() {
        return justEstimate;
    }


    public @NotNull
    abstract String getDescription();
}
