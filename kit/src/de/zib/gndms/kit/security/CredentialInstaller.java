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

package de.zib.gndms.kit.security;

import org.ietf.jgss.GSSCredential;

/**
* @author Maik Jorra
* @email jorra@zib.de
* @date 01.12.11  18:53
* @brief installes gss credential to a given object
*/
public interface CredentialInstaller<C,T> {
    public void installCredentials( T credentialReceiver, C cred );

    Class<T> getReceiverClass();
}
