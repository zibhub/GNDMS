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

import de.zib.gndms.common.rest.MyProxyToken;
import de.zib.gndms.kit.access.MyProxyFactory;
import de.zib.gndms.kit.access.MyProxyFactoryProvider;
import de.zib.gndms.model.gorfx.types.DelegatingOrder;

import java.util.Map;

/**
* @author Maik Jorra
* @email jorra@zib.de
* @date 12.12.11  18:52
* @brief
*/
public class GetCredentialProviderFor {

    final private DelegatingOrder<?> order;
    private final String requiredCredentialName;
    private final MyProxyFactoryProvider myProxyFactoryProvider;


    public GetCredentialProviderFor( final DelegatingOrder<?> order,
                                     final String requiredCredentialName,
                                     final MyProxyFactoryProvider myProxyFactoryProvider )
    {

        this.order = order;
        this.requiredCredentialName = requiredCredentialName;
        this.myProxyFactoryProvider = myProxyFactoryProvider;
    }


    public CredentialProvider invoke( ) {

        final Map<String, MyProxyToken> myProxyToken = getOrder().getMyProxyToken();
        MyProxyToken token;
        if ( myProxyToken.containsKey( getRequiredCredentialName() ) )
            token = myProxyToken.get( getRequiredCredentialName() );
        else
            throw new IllegalStateException( "no security token for: " + getRequiredCredentialName() );

        MyProxyFactory myProxyFactory = getMyProxyFactoryProvider().getFactory(
                getRequiredCredentialName() );
        if( myProxyFactory == null )
            throw new IllegalStateException( "no MyProxy-Server registered for "  +
                                             getRequiredCredentialName() );

        return new MyProxyCredentialProvider( myProxyFactory, token.getLogin(),
                token.getPassword() );
    }


    public DelegatingOrder<?> getOrder() {

        return order;
    }


    public String getRequiredCredentialName() {

        return requiredCredentialName;
    }


    public MyProxyFactoryProvider getMyProxyFactoryProvider() {

        return myProxyFactoryProvider;
    }
}
