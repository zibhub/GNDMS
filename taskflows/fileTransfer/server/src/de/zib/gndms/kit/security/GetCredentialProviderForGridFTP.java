package de.zib.gndms.kit.security;
/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import de.zib.gndms.kit.access.MyProxyFactoryProvider;
import de.zib.gndms.model.gorfx.types.DelegatingOrder;
import de.zib.gndms.taskflows.filetransfer.server.kit.security.GridFTPCredentialInstaller;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 12.12.11  18:55
 * @brief
 */
public class GetCredentialProviderForGridFTP extends GetCredentialProviderFor {

    public GetCredentialProviderForGridFTP( final DelegatingOrder<?> order,
                                            final String requiredCredentialName,
                                            final MyProxyFactoryProvider myProxyFactoryProvider )
    {

        super( order, requiredCredentialName, myProxyFactoryProvider );
    }


    @Override
    public CredentialProvider invoke() {

        final CredentialProvider credentialProvider = super.invoke();
        credentialProvider.setInstaller( new GridFTPCredentialInstaller() );
        return credentialProvider;
    }
}
