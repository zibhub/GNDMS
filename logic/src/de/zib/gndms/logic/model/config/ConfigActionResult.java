package de.zib.gndms.logic.model.config;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 23.10.2008 Time: 16:27:45
 */
public abstract class ConfigActionResult {
    private final String details;


    protected ConfigActionResult(final String detailsParam) {
        details = detailsParam;
    }


    protected ConfigActionResult() {
        this(null);
    }


    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        final String details = getDetails();
        if (details == null)
            return getResultTypeNick()+"()";
        else
            return getResultTypeNick()+"(" + details +")";
    }


    protected abstract String getResultTypeNick();
}


