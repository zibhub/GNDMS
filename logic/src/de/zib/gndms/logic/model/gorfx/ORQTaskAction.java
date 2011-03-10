package de.zib.gndms.logic.model.gorfx;

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



import de.zib.gndms.kit.access.CredentialProvider;
import de.zib.gndms.kit.access.RequiresCredentialProvider;
import de.zib.gndms.logic.model.DefaultTaskAction;
import de.zib.gndms.model.common.types.factory.KeyFactory;
import de.zib.gndms.model.common.types.factory.KeyFactoryInstance;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.OfferType;
import de.zib.gndms.neomodel.gorfx.Task;
import de.zib.gndms.neomodel.gorfx.Taskling;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.util.Map;


/**
 * Adds some type-safety to TaskActions
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 02.10.2008 Time: 13:00:56
 */
public abstract class ORQTaskAction<K extends AbstractORQ> extends DefaultTaskAction
    implements KeyFactoryInstance<String, ORQTaskAction<?>>, RequiresCredentialProvider
{
    private KeyFactory<String, ORQTaskAction<?>> factory;
    private String offerTypeId;
    private CredentialProvider credentialProvider;
    private K orq;


    protected ORQTaskAction() {
        super();
    }


    public ORQTaskAction(@NotNull EntityManager em, @NotNull Dao dao, @NotNull Taskling model) {
        super(em, dao, model);
    }


    @Override
    protected void onCreated(@NotNull String wid,
                             @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState) throws Exception {
        if (! isRestartedTask) {
            final Session session = getDao().beginSession();
            try {
                final OfferType ot = session.findOfferType(offerTypeId);
                final Task task = getModel().getTask(session);
                task.setOfferType(ot);
                task.setWID(wid);
                task.setORQ(orq);
                session.success();
            }
            finally { session.finish(); }
            super.onCreated(wid, state, isRestartedTask, altTaskState);
        }
    }


    public KeyFactory<String, ORQTaskAction<?>> getFactory() {
        return factory;
    }


    public String getOfferTypeId() {
        return offerTypeId;
    }


    public Map<String, String> getOfferTypeConfigMapData() {
        final Session session = getDao().beginSession();
        try {
            final OfferType ot = session.findOfferType(getOfferTypeId());
            final Map<String,String> configMapData = ot.getConfigMapData();
            session.finish();
            return configMapData;
        }
        finally { session.success(); }
    }

    public void setFactory(@NotNull final KeyFactory<String, ORQTaskAction<?>> factoryParam) {
        factory = factoryParam;
    }


    public void setOfferTypeId(@NotNull final String keyParam) {
        offerTypeId = keyParam;
    }


    public CredentialProvider getCredentialProvider() {
        return credentialProvider;
    }


    public void setCredentialProvider( CredentialProvider credentialProvider ) {
        this.credentialProvider = credentialProvider;
    }


    public String getKey() {
        return offerTypeId;
    }

    public void setKey(@NotNull String keyParam) {
        setOfferTypeId(keyParam);
    }

    public K getORQ() {
        return orq;
    }

    public void setORQ(K newORQ) {
        orq = newORQ;
    }

    public abstract Class<K> getOrqClass();
}
