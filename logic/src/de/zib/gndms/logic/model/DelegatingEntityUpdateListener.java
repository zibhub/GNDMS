package de.zib.gndms.logic.model;

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



import de.zib.gndms.model.common.GridResource;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;


/**
 * Delegating EntityUpdateListener.  Used to avoid leaking of GNDMSystem.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 15.08.2008 Time: 13:18:43
 */
public final class DelegatingEntityUpdateListener<M extends GridResource> implements
        EntityUpdateListener<M> {
    private static final @NotNull
    Map<EntityUpdateListener<?>, WeakReference<DelegatingEntityUpdateListener<?>>> map =
        new WeakHashMap<EntityUpdateListener<?>, WeakReference<DelegatingEntityUpdateListener<?>>>
                (8);


    /**
     * @return A DelegatingEntityUpdateListener that delegetas all #onModelChange calls to
     * its delegate.
     */
    @SuppressWarnings({ "unchecked" })
    public static <M extends GridResource> DelegatingEntityUpdateListener<M> getInstance(
            final @NotNull EntityUpdateListener<M> listener) {
        // Type things correctly the easy way
        return (DelegatingEntityUpdateListener<M>) privateGetInstance(listener);
    }

    private static <M extends GridResource> DelegatingEntityUpdateListener<?> privateGetInstance(
            final @NotNull EntityUpdateListener<M> listener)
    {
        if (listener instanceof DelegatingEntityUpdateListener)
            return (DelegatingEntityUpdateListener<?>) listener;
        else {
            synchronized (map) {
                WeakReference<DelegatingEntityUpdateListener<?>> ref = map.get(listener);
                DelegatingEntityUpdateListener<?> ret = ref == null ? null : ref.get();
                if (ret == null) {
                    ret = new DelegatingEntityUpdateListener<M>(listener);
                    ref = new WeakReference<DelegatingEntityUpdateListener<?>>(ret);
                    map.put(listener, ref);
                }
                return ret;
            }
        }
    }

    private EntityUpdateListener<M> delegate;


    /**
     * @see #getInstance(EntityUpdateListener)
     *
     * @param delegateParam
     */
    public DelegatingEntityUpdateListener(final EntityUpdateListener<M> delegateParam) {
        delegate = delegateParam;
    }


    public void onModelChange(final M model) {
        delegate.onModelChange(model);
    }
}
