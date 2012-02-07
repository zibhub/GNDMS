package de.zib.gndms.logic.model;

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



import de.zib.gndms.stuff.GNDMSInjector;
import de.zib.gndms.kit.access.GNDMSBinding;
import de.zib.gndms.model.ModelEntity;
import de.zib.gndms.model.common.GridEntity;
import de.zib.gndms.logic.action.Action;
import de.zib.gndms.logic.action.ActionInitializationException;
import de.zib.gndms.model.common.GridResourceItf;
import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * A BatchUpdateAction using delegation.
 * Note: An instance can't change it's delegation object.
 * Therefore its BatchUpdateAction can be denoted only by the constructor and #setListener() is not supported. 
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 04.09.2008 Time: 13:01:25
 */
public class DelegatingBatchUpdateAction<M extends ModelEntity & GridResourceItf, R>
        implements BatchUpdateAction<M, R> {
    private final BatchUpdateAction<M, R> delegate;
    private GNDMSInjector injector;


    public DelegatingBatchUpdateAction(final BatchUpdateAction<M, R> delegateParam) {
        delegate = delegateParam;
    }


    public ModelUpdateListener<M> getListener() {
        return delegate.getListener();
    }


    public void setListener(final @NotNull ModelUpdateListener<M> mEntityUpdateListenerParam) {
        throw new UnsupportedOperationException();
    }


    public void addAction(final Action<Void> voidActionParam) {delegate.addAction(voidActionParam);}


    public void initialize() {delegate.initialize();}


    public R call() throws ActionInitializationException {return delegate.call();}


    public void cleanUp() {delegate.cleanUp();}


    public Action<?> getParent() {
        return delegate.getParent();
    }


    public void setParent(final Action<?> parent) {delegate.setParent(parent);}


    public <V> V nextParentOfType(final @NotNull Class<V> interfaceClass) {
        return delegate.nextParentOfType(interfaceClass);
    }

    @NotNull
    public <V extends Action<?>> List<V> getParentChain(final @NotNull Class<V> interfaceClass) {
        return delegate.getParentChain(interfaceClass);
    }

    public void setInjector(GNDMSInjector anInjector) {
        injector = anInjector;
    }

    @NotNull
    public GNDMSInjector getInjector() {
        if (injector == null) {
            final Action<?> theParent = getParent();
            if (theParent == null)
                return GNDMSBinding.getInjector();
            else
                return theParent.getInjector();
        }
        else
            return injector;
    }
}
