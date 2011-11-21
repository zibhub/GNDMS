package de.zib.gndms.model.common;

import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 03.02.11
 * Time: 15:26
 * To change this template use File | Settings | File Templates.
 */
public interface TimedGridResourceItf extends GridResourceItf {
    public Calendar getTerminationTime();
    public void setTerminationTime( Calendar terminationTime );
}
