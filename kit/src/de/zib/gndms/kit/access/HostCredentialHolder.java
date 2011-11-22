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
/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 22.11.11  18:32
 * @brief
 */
public class HostCredentialHolder extends PKCredentialHolder {

    // Below load host cert from for standard /etc/grid-security/host{key,cert}.pem
    public HostCredentialHolder( String base ) {
        super( base + "/hostcert.pem", base + "/hostkey.pem" );
    }
}
