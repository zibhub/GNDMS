/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.zib.gndms.kit.access.myproxyext;

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

import org.globus.myproxy.GetParams;
import org.globus.myproxy.MyProxy;

/**
 * @author Marcel Bardenhagen
 * @email Marcel.Bardenhagen@awi.de
 * @date 16.05.2012
 */
public class RetrieveParams extends GetParams{

    public RetrieveParams(String username, String passphrase) {
        super(username, passphrase);
        setCommand(MyProxy.RETRIEVE_CREDENTIAL);
    }

    public RetrieveParams() {
        super();
        setCommand(MyProxy.RETRIEVE_CREDENTIAL);
    }
    
}
