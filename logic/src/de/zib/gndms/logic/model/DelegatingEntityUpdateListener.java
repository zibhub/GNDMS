package de.zib.gndms.logic.model;

import de.zib.gndms.model.common.GridResource;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;


/**
 * Delegating EntityUpdateListener.  Used to avoid leaking of GNDMSystem.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
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
