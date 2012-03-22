package de.zib.gndms.gndmc.dspace;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import de.zib.gndms.common.dspace.service.SliceKindServiceClient;
import de.zib.gndms.common.logic.config.Configuration;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.gndmc.AbstractClient;
import org.springframework.http.ResponseEntity;

/**
 * The slice kind client implementing the slice kind service.
 * 
 * @author Ulrike Golas
 */

public class SliceKindClient extends AbstractClient implements SliceKindServiceClient {

	/**
	 * The constructor.
	 */
	public SliceKindClient() {
	}

    /**
	 * The constructor.
	 * 
	 * @param serviceURL The base url of the grid.
	 */
	public SliceKindClient(final String serviceURL) {
		this.setServiceURL(serviceURL);
	}

    @Override
    public final ResponseEntity<Configuration> getSliceKindInfo(final String subspace,
                                                                final String sliceKind, final String dn) {
        return unifiedGet(Configuration.class, getServiceURL() + "/dspace/_" + subspace
                + "/_" + sliceKind, dn);
    }


    @Override
    public final ResponseEntity<Configuration> getSliceKindInfo( final Specifier< Void > sliceKind, final String dn ) {
        return unifiedGet( Configuration.class, sliceKind.getUrl(), dn);
    }


    @Override
    public ResponseEntity<Configuration> getSliceKindConfig( final String subspace,
                                                             final String sliceKind,
                                                             final String dn )
    {
        // todo implement it, stupid.
        throw new UnsupportedOperationException( "implement me" );
    }

    @Override
    public ResponseEntity<Configuration> getSliceKindConfig( final Specifier< Void > sliceKind,
                                                             final String dn )
    {
        // todo implement it, stupid.
        throw new UnsupportedOperationException( "implement me" );
    }

    @Override
    public final ResponseEntity<Void> setSliceKindConfig(final String subspace,
                                                         final String sliceKind,
                                                         final Configuration config,
                                                         final String dn) {
        return unifiedPut(Void.class, config, getServiceURL() + "/dspace/_" + subspace + "/_"
                + sliceKind, dn);
    }

    @Override
    public final ResponseEntity<Void> setSliceKindConfig( final Specifier< Void > sliceKind,
                                                          final Configuration config, final String dn ) {
        return unifiedPut(Void.class, config, sliceKind.getUrl(), dn);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final ResponseEntity<Specifier<Void>> deleteSliceKind(final String subspace,
                                                                 final String sliceKind, final String dn) {
        return (ResponseEntity<Specifier<Void>>) (Object) unifiedDelete(Specifier.class,
                getServiceURL() + "/dspace/_" + subspace + "/_"
                        + sliceKind, dn);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final ResponseEntity<Specifier<Void>> deleteSliceKind(final Specifier< Void > sliceKind, final String dn) {
        return (ResponseEntity<Specifier<Void>>) (Object) unifiedDelete(Specifier.class,
                sliceKind.getUrl(), dn);
    }
}
