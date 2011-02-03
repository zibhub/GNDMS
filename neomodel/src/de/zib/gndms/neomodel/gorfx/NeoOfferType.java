package de.zib.gndms.neomodel.gorfx;

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



import de.zib.gndms.neomodel.common.NeoSession;
import de.zib.gndms.neomodel.common.NodeGridResource;
import org.apache.commons.lang.SerializationUtils;
import org.jetbrains.annotations.NotNull;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 * User: stepn Date: 05.09.2008 Time: 14:48:36
 */
public class NeoOfferType extends NodeGridResource {

    private static final String TASK_ACTION_FACTORY_CLASS_NAME_KEY = "taskActionFactoryClassName";
    private static final String CALCULATOR_FACTORY_CLASS_NAME_KEY = "calculatorFactoryClassName";
    private static final String OFFER_ARGUMENT_TYPE_KEY = "offerArgumentType";
    private static final String OFFER_RESULT_TYPE_KEY = "offerResultType";
    private static final String CONFIG_MAP_DATA_KEY = "configMapData";

    public NeoOfferType(@NotNull NeoSession session, @NotNull String typeNick, @NotNull Node underlying) {
        super(session, typeNick, underlying);
    }

    public String getOfferTypeKey() {
        return getId();
    }

    public void setOfferTypeKey(String offerTypeKey) {
        setId(offerTypeKey);
    }

    @Override
    final public String getId() {
        return super.getId();
    }

    @Override
    final public void setId(String id) {
        final Index<Node> nodeIndex = getTypeNickIndex();
        final String oldId = getId();
        if (oldId != null)
            nodeIndex.remove(repr(), session().getGridName(), oldId);

        if (nodeIndex.get(session().getGridName(), id).size() > 0) {
            session().failure();
            throw new IllegalArgumentException("Node already exists");
        }
        nodeIndex.add(repr(), session().getGridName(), id);
        super.setId(id);
    }

    public Map<String, String> getConfigMapData() {
        //noinspection unchecked
        return (Map<String, String>) SerializationUtils.deserialize((byte[]) repr().getProperty(CONFIG_MAP_DATA_KEY));
    }

    public void setConfigMapData(Map<String, String> configMapData) {
        repr().setProperty(CONFIG_MAP_DATA_KEY, SerializationUtils.serialize( (Serializable) configMapData));
    }

    public ImmutableScopedName getOfferResultType() {
        return (ImmutableScopedName) SerializationUtils.deserialize((byte[]) repr().getProperty(OFFER_RESULT_TYPE_KEY));
    }

    public void setOfferResultType(ImmutableScopedName offerResultType) {
        repr().setProperty(OFFER_RESULT_TYPE_KEY, SerializationUtils.serialize( offerResultType ));
    }

    public ImmutableScopedName getOfferArgumentType() {
        return
            (ImmutableScopedName) SerializationUtils.deserialize((byte[]) repr().getProperty(OFFER_ARGUMENT_TYPE_KEY));
    }

    public void setOfferArgumentType(ImmutableScopedName offerArgumentType) {
        repr().setProperty(OFFER_ARGUMENT_TYPE_KEY, SerializationUtils.serialize( offerArgumentType ));
    }

    public String getCalculatorFactoryClassName() {
        return (String) repr().getProperty(CALCULATOR_FACTORY_CLASS_NAME_KEY);
    }

    public void setCalculatorFactoryClassName(String calculatorFactoryClassName) {
        repr().setProperty(CALCULATOR_FACTORY_CLASS_NAME_KEY, calculatorFactoryClassName);
    }

    public String getTaskActionFactoryClassName() {
        return (String) repr().getProperty(TASK_ACTION_FACTORY_CLASS_NAME_KEY);
    }

    public void setTaskActionFactoryClassName(String taskActionFactoryClassName) {
        repr().setProperty(TASK_ACTION_FACTORY_CLASS_NAME_KEY, taskActionFactoryClassName);
    }

    @Override
    public void delete() {
        final Index<Node> nodeIndex = getTypeNickIndex();
        nodeIndex.remove(repr(), session().getGridName(), getId());
        repr().delete();
    }
}
