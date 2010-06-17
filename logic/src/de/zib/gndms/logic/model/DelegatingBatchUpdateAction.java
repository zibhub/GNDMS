package de.zib.gndms.logic.model;

import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.logic.action.Action;
import de.zib.gndms.logic.action.ActionInitializationException;
import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * A BatchUpdateAction using delegation.
 * Note: An instance can't change it's delegation object.
 * Therefore its BatchUpdateAction can be denoted only by the constructor and #setListener() is not supported. 
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 04.09.2008 Time: 13:01:25
 */
public class DelegatingBatchUpdateAction<M extends GridResource, R>
        implements BatchUpdateAction<M, R> {
    private final BatchUpdateAction<M, R> delegate;


    public DelegatingBatchUpdateAction(final BatchUpdateAction<M, R> delegateParam) {
        delegate = delegateParam;
    }


    public EntityUpdateListener<M> getListener() {
        return delegate.getListener();
    }


    public void setListener(final @NotNull EntityUpdateListener<M> mEntityUpdateListenerParam) {
        throw new UnsupportedOperationException();
    }


    public void addAction(final Action<Void> voidActionParam) {delegate.addAction(voidActionParam);}


    public void initialize() {delegate.initialize();}


    public R call() throws ActionInitializationException, RuntimeException {return delegate.call();}


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
}
