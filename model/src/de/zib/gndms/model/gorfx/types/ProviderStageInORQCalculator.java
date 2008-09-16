package de.zib.gndms.model.gorfx.types;

import de.zib.gndms.model.gorfx.Contract;

/**
 * Example class of provider state in calculator.
 *
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 4:12:20 PM
 *
 */
public class ProviderStageInORQCalculator extends AbstractORQCalculator<ProviderStageInORQ> {


    public ProviderStageInORQCalculator() {
        super( );
        super.setORQModelClass( ProviderStageInORQ.class );
    }


    @Override
    public Contract createOffer() {
        // todo: implement me
        return null;
    }
}
