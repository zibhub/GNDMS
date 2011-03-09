package de.zib.gndms.logic.model.gorfx;

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



import de.zib.gndms.kit.access.CredentialProvider;
import de.zib.gndms.kit.access.RequiresCredentialProvider;
import de.zib.gndms.logic.model.DefaultTaskAction;
import de.zib.gndms.model.common.types.factory.KeyFactory;
import de.zib.gndms.model.common.types.factory.KeyFactoryInstance;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.model.gorfx.types.io.ORQConverter;
import de.zib.gndms.neomodel.common.NeoDao;
import de.zib.gndms.neomodel.common.NeoSession;
import de.zib.gndms.neomodel.gorfx.NeoOfferType;
import de.zib.gndms.neomodel.gorfx.NeoTask;
import de.zib.gndms.neomodel.gorfx.Taskling;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;


/**
 * Adds some type-safety to TaskActions
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 02.10.2008 Time: 13:00:56
 */
public abstract class ORQTaskAction<K extends AbstractORQ> extends DefaultTaskAction
    implements KeyFactoryInstance<String, ORQTaskAction<? super K>>, RequiresCredentialProvider
{
    private KeyFactory<String, ORQTaskAction<? super K>> factory;
    private String offerTypeId;
    private CredentialProvider credentialProvider;
    private K orq;


    protected ORQTaskAction() {
        super();
    }


    public ORQTaskAction(@NotNull EntityManager em, @NotNull NeoDao dao, @NotNull Taskling model) {
        super(em, dao, model);
    }


    @Override
    protected void onCreated(@NotNull String wid,
                             @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState) throws Exception {
        if (! isRestartedTask) {
            final NeoSession session = getDao().beginSession();
            try {
                final NeoOfferType ot = session.findOfferType(offerTypeId);
                final NeoTask task = getModel().getTask(session);
                task.setOfferType(ot);
                task.setWID(wid);
                task.setORQ(orq);
                session.success();
            }
            finally { session.finish(); }
            super.onCreated(wid, state, isRestartedTask, altTaskState);
        }
    }


    public KeyFactory<String, ORQTaskAction<? super K>> getFactory() {
        return factory;
    }


    public String getOfferTypeId() {
        return offerTypeId;
    }


    public void setFactory(@NotNull final KeyFactory<String, ORQTaskAction<? super K>> factoryParam) {
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
