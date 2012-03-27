package de.zib.gndms.taskflows.failure.client.model;

import de.zib.gndms.common.model.gorfx.types.AbstractOrder;
import de.zib.gndms.taskflows.failure.client.FailureTaskFlowMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author try ma ik jo rr a zib
 * @date 14.02.11  14:58
 * @brief The order class for a dummy taskflow.
 *
 * The dummy taskflow echos a given message a given number of times ^^ with some delay.
 * It can also fail on demand;
 */
public class FailureOrder extends AbstractOrder {

    private static final long serialVersionUID = -1925988463350176270L;

    public enum FailurePlace {
        CREATED, INITIALIZED, INPROGRESS, FAILED, FINISHED, NOWHERE,
    }
    
    private Map< FailurePlace, Integer > sleep_before_super;
    private Map< FailurePlace, Integer > sleep_after_super;
    private FailurePlace where;
    private boolean beforeSuper;
    private boolean throwInSession;
    private String exception;
    private String message;

    public FailureOrder() {
        super( );
        super.setTaskFlowType( FailureTaskFlowMeta.TASK_FLOW_TYPE_KEY );
        
        sleep_before_super = new HashMap< FailurePlace, Integer >();
        sleep_after_super = new HashMap< FailurePlace, Integer >();
        where = FailurePlace.INPROGRESS;
        beforeSuper = false;
        throwInSession = false;
        exception = "java.lang.RuntimeException";
        message = "Default test exception";
    }
    

    public FailureOrder setThrowInSession( boolean inSession ) {
        throwInSession = inSession;
        return this;
    }

    public boolean isThrowInSession() {
        return throwInSession;
    }

    public FailureOrder setException( String exception ) {
        this.exception = exception;
        return this;
    }

    public String getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FailurePlace getWhere() {
        return where;
    }

    public FailureOrder setWhere(FailurePlace where) {
        this.where = where;
        return this;
    }

    public boolean isBeforeSuper() {
        return beforeSuper;
    }

    public FailureOrder setBeforeSuper(boolean beforeSuper) {
        this.beforeSuper = beforeSuper;
        return this;
    }

    public FailureOrder setSleep( FailurePlace failurePlace, int beforeSuper, int afterSuper ) {
        sleep_before_super.put( failurePlace, beforeSuper );
        sleep_after_super.put( failurePlace, afterSuper );
        return this;
    }

    public FailureOrder setSleepBefore( final Map< FailurePlace, Integer > map ) {
        sleep_before_super = map;
        return this;
    }

    public FailureOrder setSleepAfter( final Map< FailurePlace, Integer > map ) {
        sleep_after_super = map;
        return this;
    }

    public int getSleepBeforeSuper( FailurePlace failurePlace ) {
        if( sleep_before_super.containsKey( failurePlace ) )
            return sleep_before_super.get( failurePlace );
        else
            return 0;
    }
    
    public int getSleepAfterSuper( FailurePlace failurePlace ) {
        if( sleep_after_super.containsKey( failurePlace ) )
            return sleep_after_super.get( failurePlace );
        else
            return 0;
    }

    @Override
    public String getTaskFlowType() {
        return FailureTaskFlowMeta.TASK_FLOW_TYPE_KEY;
    }


    @NotNull
    @Override
    public String getDescription() {
        String s = "Testing failures. Throwing " + exception.toString() + " in " + where.toString();
        if( beforeSuper )
            s += " before super.";
        else
            s += " after super.";
        return s;
    }
}
