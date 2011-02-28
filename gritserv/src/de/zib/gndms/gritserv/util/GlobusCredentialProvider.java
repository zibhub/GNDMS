package de.zib.gndms.gritserv.util;

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



import de.zib.gndms.kit.access.CredentialProvider;
import org.globus.gsi.GlobusCredential;

import java.util.List;
import java.util.ArrayList;

/**
 * @author  try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 18.06.2010, Time: 10:42:22
 */
public abstract class GlobusCredentialProvider implements CredentialProvider {

    private GlobusCredential credential;


    public GlobusCredential getCredential() {
        return credential;
    }


    public void setCredential( GlobusCredential credential ) {
        this.credential = credential;
    }


    public List getCredentials() {
        return new ArrayList<GlobusCredential>() {{ add( getCredential() ); }};
    }
}
