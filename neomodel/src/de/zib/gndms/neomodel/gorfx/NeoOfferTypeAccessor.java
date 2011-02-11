package de.zib.gndms.neomodel.gorfx;

import de.zib.gndms.model.common.GridResourceItf;
import de.zib.gndms.model.common.ImmutableScopedName;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 11.02.11
 * Time: 14:06
 * To change this template use File | Settings | File Templates.
 */
public interface NeoOfferTypeAccessor extends GridResourceItf {
    @NotNull String getId();

    @NotNull String getOfferTypeKey();

    String getTaskActionFactoryClassName();

    String getCalculatorFactoryClassName();

    Map<String, String> getConfigMapData();

    ImmutableScopedName getOfferArgumentType();

    ImmutableScopedName getOfferResultType();

}
