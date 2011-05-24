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
import de.zib.gndms.logic.model.TaskAction;
import de.zib.gndms.model.common.types.factory.KeyFactory;
import de.zib.gndms.model.common.types.factory.KeyFactoryInstance;
import de.zib.gndms.model.gorfx.AbstractTask;
import de.zib.gndms.model.gorfx.OfferType;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
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
public abstract class ORQTaskAction<K extends AbstractORQ> extends TaskAction
    implements KeyFactoryInstance<OfferType, ORQTaskAction<?>>, RequiresCredentialProvider
{
    private KeyFactory<OfferType, ORQTaskAction<?>> factory;
    private OfferType key;
    private CredentialProvider credentialProvider;


    protected ORQTaskAction() {
        super();
    }


    public ORQTaskAction(final @NotNull EntityManager em, final @NotNull AbstractTask model) {
        super(em, model);
    }


    public ORQTaskAction( final @NotNull EntityManager em, final @NotNull String pk, Class<? extends AbstractTask> cls ) {
        super(em, pk, cls );
    }

    
    public ORQTaskAction( final @NotNull EntityManager em, final @NotNull String pk ) {
        super(em, pk, Task.class );
    }


    @SuppressWarnings({ "ThrowableInstanceNeverThrown" })
    @Override
    protected void onCreated(final AbstractTask model) {
        try {
            super.onCreated(model);    // Overridden method
        }
        catch (TransitException e) {
            if (e.isDemandingAbort()) throw e; // dont continue on failure

            EntityManager em = getOwnEntityManager();
            try {
                em.getTransaction().begin();
                final OfferType ot = em.find(OfferType.class, getOrq().getOfferType());
                if (ot == null)
                    fail(new NullPointerException("Unsupported OfferType"));
                getModel().setOfferType(ot);
                em.getTransaction().commit();
            }
            finally {
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
            }
            throw e; // accept state transition decision from super
        }
    }


    protected abstract @NotNull Class<K> getOrqClass();


    @Override
    protected final @NotNull Class<AbstractTask> getTaskClass() {
        return AbstractTask.class;
    }


    public void setOrq(final @NotNull K orq) {
        if (getModel().getOrq() != null)
            getModel().setOrq(orq);
        else
            throw new IllegalStateException("Illegal attempt to overwrite Task ORQ");
    }


    public @NotNull K getOrq() {
        final AbstractTask model = getModel();
        if (model == null)
            throw new IllegalStateException("Model missing");
        return getOrqClass().cast(getModel().getOrq());
    }


    public KeyFactory<OfferType, ORQTaskAction<?>> getFactory() {
        return factory;
    }


    public OfferType getKey() {
        return key;
    }


    public void setFactory(@NotNull final KeyFactory<OfferType, ORQTaskAction<?>> factoryParam) {
        factory = factoryParam;
    }


    public void setKey(@NotNull final OfferType keyParam) {
        key = keyParam;
    }


    public CredentialProvider getCredentialProvider() {
        return credentialProvider;
    }


    public void setCredentialProvider( CredentialProvider credentialProvider ) {
        this.credentialProvider = credentialProvider;
    }

    protected void failFrom( Exception e ) {
        fail( new IllegalStateException( getModel().getDescription() + " failure " +  e.getMessage(), e ) );
    }

    protected void traceFrom( Exception e ) {
        trace( getModel().getDescription() + " failure " +  e.getMessage(), e );
    }
}
