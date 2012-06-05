package de.zib.gndms.kit.security.test;
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

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 25.05.12  16:50
 * @brief
 */
public class CertStuffHolder {


    private ArrayList<X509Certificate> chain;
    private KeyPair keyPair;


    public void setChain( final ArrayList<X509Certificate> chain ) {

        this.chain = chain;
    }


    public void setKeyPair( final KeyPair keyPair ) {

        this.keyPair = keyPair;
    }


    public ArrayList<X509Certificate> getChain() {

        return chain;
    }


    public KeyPair getKeyPair() {

        return keyPair;
    }
}
