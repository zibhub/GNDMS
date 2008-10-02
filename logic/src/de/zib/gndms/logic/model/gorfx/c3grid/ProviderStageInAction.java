package de.zib.gndms.logic.model.gorfx.c3grid;

import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 02.10.2008 Time: 15:04:39
 */
public class ProviderStageInAction extends ORQTaskAction<ProviderStageInORQ> {

    protected void onInProgress(final @NotNull Task model) {
    }


    protected @NotNull Contract createInitialContract(final @NotNull ProviderStageInORQ orq) {
        return;
    }


    @Override
    protected @NotNull Class<ProviderStageInORQ> getOrqClass() {
        return ProviderStageInORQ.class;
    }
}
