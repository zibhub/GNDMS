package de.zib.gndms.model.common;

import org.joda.time.DateTime;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 03.02.11
 * Time: 15:26
 * To change this template use File | Settings | File Templates.
 */
public interface TimedGridResourceItf extends GridResourceItf {

    public DateTime getTerminationTime();
    public void setTerminationTime( DateTime terminationTime );
}
