package de.zib.gndms.logic.model.config;

import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
*
* @author Stefan Plantikow<plantikow@zib.de>
* @version $Id$
*
*          User: stepn Date: 23.10.2008 Time: 16:35:54
*/
public final class OkResult extends ConfigActionResult {
    public OkResult(final @NotNull String detailsParam) {
        super(detailsParam);
    }


    public OkResult() {
        super();
    }


    @Override
    protected String getResultTypeNick() {
        return "OK";
    }
}


