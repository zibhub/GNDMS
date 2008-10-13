package de.zib.gndms.logic.model.gorfx;

import java.io.Serializable;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 13.10.2008 Time: 13:49:48
 */
public class TaskResult implements Serializable {
    public final String offerType;

    private static final long serialVersionUID = -5842487793979347336L;


    public TaskResult(final String offerTypeParam) {
        offerType = offerTypeParam;
    }


    public String getOfferType() {
        return offerType;
    }
}
