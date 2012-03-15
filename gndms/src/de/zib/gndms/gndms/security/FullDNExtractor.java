package de.zib.gndms.gndms.security;
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

import org.springframework.security.web.authentication.preauth.x509.X509PrincipalExtractor;

import java.security.cert.X509Certificate;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 02.03.12  14:41
 * @brief
 */
public class FullDNExtractor implements X509PrincipalExtractor {

    @Override
    public Object extractPrincipal( final X509Certificate x509Certificate ) {

        return x509Certificate.getSubjectDN().toString();
    }
}
