package de.zib.gndms.common.model.gorfx.types;

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



import de.zib.gndms.stuff.copy.Copyable;
import de.zib.gndms.stuff.copy.Copyable.CopyMode;
import org.jetbrains.annotations.NotNull;

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
    
    private String actId;
    private Map< String, String > actContext;


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


    public boolean hasContext() {
        return actContext != null;
    }


    public Map< String, String > getActContext() {
        return actContext;
    }


    public void setActContext( Map< String, String > actContext ) {
        this.actContext = actContext;
    }


    public boolean hasId() {
        return actId != null;
    }


    public String getActId() {
        return actId;
    }


    public void setActId( String actId ) {
        this.actId = actId;
    }


    public @NotNull
    abstract String getDescription();
}
