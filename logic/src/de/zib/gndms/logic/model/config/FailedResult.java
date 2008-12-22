package de.zib.gndms.logic.model.config;

import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 23.10.2008 Time: 16:36:32
 */
public final class FailedResult extends ConfigActionResult {
    public FailedResult(final @NotNull String detailsParam) {
        super(detailsParam);
    }


    @Override
    protected String getResultTypeNick() {
        return "FAILED";
    }
}
