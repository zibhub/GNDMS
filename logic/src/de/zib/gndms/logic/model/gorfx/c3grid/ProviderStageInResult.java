package de.zib.gndms.logic.model.gorfx.c3grid;

import de.zib.gndms.logic.model.gorfx.TaskResult;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 13.10.2008 Time: 13:50:37
 */
public class ProviderStageInResult extends TaskResult {
    private static final long serialVersionUID = -5522719116591151843L;

    private final String sliceKey;


    public ProviderStageInResult(final String offerTypeParam, final String sliceKeyParam) {
        super(offerTypeParam);
        sliceKey = sliceKeyParam;
    }


    public String getSliceKey() {
        return sliceKey;
    }
}
