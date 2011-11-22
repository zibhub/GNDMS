/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
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

package de.zib.gndms.kit.access;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 22.11.11  18:43
 * @brief Provides MyProxyProviders.
 */
public class MyProxyProviderProvider {

    private Map<String, MyProxyProvider> providers;


    public Map<String, MyProxyProvider> getProviders() {
        return providers;
    }


    public void setProviders( Map<String, MyProxyProvider> providers ) {
        this.providers = providers;
    }


    public static MyProxyProviderProvider fromList( List <MyProxyProvider> providers ) {
        MyProxyProviderProvider providerProvider = new MyProxyProviderProvider();
        HashMap<String, MyProxyProvider> providerMap = new HashMap<String, MyProxyProvider>( providers.size() );

        for( MyProxyProvider mpp : providers )
            providerMap.put( mpp.getNickname(), mpp );

        providerProvider.setProviders( providerMap );

        return providerProvider;
    }


    public MyProxyProvider getProvider( String c3grid ) {
        return providers.get( c3grid );
    }
}
