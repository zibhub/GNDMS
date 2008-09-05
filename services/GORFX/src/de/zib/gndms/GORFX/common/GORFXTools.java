package de.zib.gndms.GORFX.common;

import de.zib.gndms.model.gorfx.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.GORFXConstantURIs;
import types.ProviderStageInORQT;
import types.DynamicOfferDataSeqT;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 5:08:47 PM
 */
public class GORFXTools {

    public static ProviderStageInORQ convertFromORQT( DynamicOfferDataSeqT orq ) throws IllegalArgumentException {

        if(! orq.getOfferType().toString().equals( GORFXConstantURIs.PROVIDER_STAGE_IN_URI ) )
            throw new IllegalArgumentException( );

        return convertFromORQT( (ProviderStageInORQT) orq );
    }


    public static ProviderStageInORQ convertFromORQT( ProviderStageInORQT orq ) {
        // todo implement me
        return null;
    }
}
