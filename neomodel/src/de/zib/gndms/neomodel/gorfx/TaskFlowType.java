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

import de.zib.gndms.model.common.ImmutableScopedName;

import java.io.Serializable;
import java.util.Map;

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



import de.zib.gndms.neomodel.common.ReprSession;
import de.zib.gndms.neomodel.common.NodeGridResource;
import org.jetbrains.annotations.NotNull;
import org.neo4j.graphdb.Node;

/**
 * TaskFlowType
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 * User: stepn Date: 05.09.2008 Time: 14:48:36
 */
public class TaskFlowType extends NodeGridResource<TaskFlowTypeAccessor> implements TaskFlowTypeAccessor {
    public static final String TASK_ACTION_FACTORY_CLASS_NAME_P = "TASK_ACTION_FACTORY_CLASS_NAME_P";
    public static final String CALCULATOR_FACTORY_CLASS_NAME_P = "CALCULATOR_FACTORY_CLASS_NAME_P";
    public static final String CONFIG_MAP_DATA_P = "CONFIG_MAP_DATA_P";
    public static final String OFFER_ARGUMENT_TYPE_P = "OFFER_ARGUMENT_TYPE_P";
    public static final String OFFER_RESULT_TYPE_P = "OFFER_RESULT_TYPE_P";

    public TaskFlowType( @NotNull ReprSession session, @NotNull String typeNick, @NotNull Node underlying ) {
        super(session, typeNick, underlying);
    }

    final public @NotNull String getId() {
        return super.getId();
    }

    @Override final public void setId(@NotNull String id) {
        super.setId(id);
        session().setSingleIndex(getTypeNickIndex(), repr(), session().getGridName(), getId(), id);
    }

    public final @NotNull String getTaskFlowTypeKey() {
        return getId();
    }

    public void setTaskFlowTypeKey( @NotNull String offerTypeKey ) {
        setId(offerTypeKey);
    }

    public String getTaskActionFactoryClassName() {
        return (String) getProperty(TASK_ACTION_FACTORY_CLASS_NAME_P);
    }

    public void setTaskActionFactoryClassName(String taskActionFactoryClassName) {
        setProperty(TASK_ACTION_FACTORY_CLASS_NAME_P, taskActionFactoryClassName);
    }

    public String getCalculatorFactoryClassName() {
        return (String) getProperty(CALCULATOR_FACTORY_CLASS_NAME_P);
    }

    public void setCalculatorFactoryClassName(String calculatorFactoryClassName) {
        setProperty(CALCULATOR_FACTORY_CLASS_NAME_P, calculatorFactoryClassName);
    }

    public Map<String, String> getConfigMapData() {
        //noinspection unchecked
        return (Map<String, String>) getProperty(Serializable.class, CONFIG_MAP_DATA_P);
    }

    public void setConfigMapData(Map<String, String> configMapData) {
        setProperty(Serializable.class, CONFIG_MAP_DATA_P, (Serializable) configMapData);
    }

    public ImmutableScopedName getTaskFlowArgumentType() {
        return getProperty(ImmutableScopedName.class, OFFER_ARGUMENT_TYPE_P);
    }

    public void setTaskFlowArgumentType( ImmutableScopedName offerArgumentType ) {
        setProperty(ImmutableScopedName.class, OFFER_ARGUMENT_TYPE_P, offerArgumentType);
    }

    public ImmutableScopedName getTaskFlowResultType() {
        return getProperty(ImmutableScopedName.class, OFFER_RESULT_TYPE_P);
    }

    public void setTaskFlowResultType( ImmutableScopedName offerResultType ) {
        setProperty(ImmutableScopedName.class, OFFER_RESULT_TYPE_P, offerResultType);
    }

    @Override
    public void delete() {
        getTypeNickIndex().remove(repr(), session().getGridName(), getId());
        super.delete();
    }}
