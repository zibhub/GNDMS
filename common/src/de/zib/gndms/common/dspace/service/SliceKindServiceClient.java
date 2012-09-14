package de.zib.gndms.common.dspace.service;

import de.zib.gndms.common.dspace.SliceKindConfiguration;
import de.zib.gndms.common.logic.config.Configuration;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.Specifier;
import org.springframework.http.ResponseEntity;

public interface SliceKindServiceClient extends SliceKindService {
    /**
     * Lists the slice kind representation.
     *
     * @param sliceKind
     *            The slice kind specifier.
     * @param dn
     *            The dn of the user invoking the method.
     * @return The representation of the slice kind.
     */
    ResponseEntity<Configuration> getSliceKindInfo( Specifier< Void > sliceKind, String dn );


    /**
     * Same as the setter just the other way 'round.
     *
     * @param sliceKind The slice kind specifier.
     * @param dn What the name ... ok it's the distinguished name.
     * @return The current config.
     */
    ResponseEntity< SliceKindConfiguration > getSliceKindConfig( Specifier< Void > sliceKind, String dn );

    /**
     * Sets a slice kind configuration.
     *
     * @param sliceKind
     *            The slice kind specifier.
     * @param config The configuration of the slice kind.
     * @param dn
     *            The dn of the user invoking the method.
     * @return A confirmation.
     */
    ResponseEntity<Void> setSliceKindConfig( Specifier< Void > sliceKind, Configuration config, String dn );

    /**
     * Deletes a slice kind.
     *
     * @param sliceKind
     *            The slice kind specifier.
     * @param dn
     *            The dn of the user invoking the method.
     * @return A confirmation.
     */
    ResponseEntity<Specifier<Facets>> deleteSliceKind( Specifier< Void > sliceKind, String dn );
}
