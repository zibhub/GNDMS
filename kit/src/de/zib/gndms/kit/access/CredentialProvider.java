package de.zib.gndms.kit.access;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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



import java.util.List;

/**
 * Interface that hides the credential mechanism.
 *
 * Implementations of this interface should provided the ability to install credentials in a specific object.
 *
 * @author  try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 18.06.2010, Time: 10:12:35
 */
public interface CredentialProvider {

    /**
     * installs the credentials in the given object o.
     * @param o The object which accepts credentials, e.g. an instance of some client.
     */
    public void installCredentials( Object o );

    /**
     * Delivers the list of existing credentials.
     * @return A list of credentials which might be empty
     */
    public List getCredentials( );
}
