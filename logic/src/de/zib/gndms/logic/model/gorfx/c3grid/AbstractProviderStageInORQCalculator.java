package de.zib.gndms.logic.model.gorfx.c3grid;

import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
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
public abstract class AbstractProviderStageInORQCalculator extends AbstractORQCalculator<ProviderStageInORQ, AbstractProviderStageInORQCalculator> {
    public AbstractProviderStageInORQCalculator() {
        super( );
        super.setORQModelClass( ProviderStageInORQ.class );
    }



}
