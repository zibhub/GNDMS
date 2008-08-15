package de.zib.gndms.logic.model;

import de.zib.gndms.model.common.GridResource;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;


/**
 * TDelegating EntityUpdateListener.  Used to avoid leaking of GNDMSystem.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.08.2008 Time: 13:18:43
 */
public final class DelegatingEntityUpdateListener implements EntityUpdateListener {
    private static final @NotNull
    Map<EntityUpdateListener, WeakReference<DelegatingEntityUpdateListener>> map =
        new WeakHashMap<EntityUpdateListener, WeakReference<DelegatingEntityUpdateListener>>(8);

    public static DelegatingEntityUpdateListener getInstance(
            final @NotNull EntityUpdateListener listener)
    {
        if (listener instanceof DelegatingEntityUpdateListener)
            return (DelegatingEntityUpdateListener) listener;
        else {
            synchronized (map) {
                DelegatingEntityUpdateListener ret = map.get(listener).get();
                if (ret == null) {
                    ret = new DelegatingEntityUpdateListener(listener);
                    map.put(listener, new WeakReference<DelegatingEntityUpdateListener>(ret));
                }
                return ret;
            }
        }
    }

    private EntityUpdateListener delegate;

    private DelegatingEntityUpdateListener(final EntityUpdateListener delegateParam) {
        delegate = delegateParam;
    }


    public void onModelChange(final GridResource model) {
        delegate.onModelChange(model);
    }
}
