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



import de.zib.gndms.model.ModelEntity;
import de.zib.gndms.model.common.GridEntity;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.common.GridResourceItf;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;


/**
 * Delegating ModelUpdateListener.  Used to avoid leaking of GNDMSystem.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 15.08.2008 Time: 13:18:43
 */
public final class DelegatingModelUpdateListener<M extends ModelEntity & GridResourceItf> implements
        ModelUpdateListener<M> {
    private static final @NotNull
    Map<ModelUpdateListener<?>, WeakReference<DelegatingModelUpdateListener<?>>> map =
        new WeakHashMap<ModelUpdateListener<?>, WeakReference<DelegatingModelUpdateListener<?>>>
                (8);


    /**
     * @return A DelegatingModelUpdateListener that delegetas all #onModelChange calls to
     * its delegate.
     */
    @SuppressWarnings({ "unchecked" })
    public static <M extends GridResource> DelegatingModelUpdateListener<M> getInstance(
            final @NotNull ModelUpdateListener<M> listener) {
        // Type things correctly the easy way
        return (DelegatingModelUpdateListener<M>) privateGetInstance(listener);
    }

    private static <M extends GridResource> DelegatingModelUpdateListener<?> privateGetInstance(
            final @NotNull ModelUpdateListener<M> listener)
    {
        if (listener instanceof DelegatingModelUpdateListener)
            return (DelegatingModelUpdateListener<?>) listener;
        else {
            synchronized (map) {
                WeakReference<DelegatingModelUpdateListener<?>> ref = map.get(listener);
                DelegatingModelUpdateListener<?> ret = ref == null ? null : ref.get();
                if (ret == null) {
                    ret = new DelegatingModelUpdateListener<M>(listener);
                    ref = new WeakReference<DelegatingModelUpdateListener<?>>(ret);
                    map.put(listener, ref);
                }
                return ret;
            }
        }
    }

    private final ModelUpdateListener<M> delegate;


    /**
     * @see #getInstance(ModelUpdateListener)
     *
     * @param delegateParam
     */
    public DelegatingModelUpdateListener(final ModelUpdateListener<M> delegateParam) {
        delegate = delegateParam;
    }


    public void onModelChange(final M model) {
        delegate.onModelChange(model);
    }
}
