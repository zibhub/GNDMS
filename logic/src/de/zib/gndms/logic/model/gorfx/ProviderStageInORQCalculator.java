package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;

/**
 * Example class of provider state in calculator.
 *
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 4:12:20 PM
 *
 */
public class ProviderStageInORQCalculator extends AbstractORQCalculator<ProviderStageInORQ, ProviderStageInORQCalculator> {


    public ProviderStageInORQCalculator() {
        super( );
        super.setORQModelClass( ProviderStageInORQ.class );
    }


    @Override
    public Contract createOffer() throws Exception {
        // todo: implement me
        return null;
    }
}
