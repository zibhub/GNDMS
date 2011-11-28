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

package de.zib.gndms.infra.grams;

import java.io.IOException;

public class GNDMSJobManagerScriptBuilder {

    private String username;
    private String globusLocation;
    private String type;
    private String perlJobDescription;
    private String[] environment;


    public GNDMSJobManagerScriptBuilder setUsername( final String username ) {

        this.username = username;
        return this;
    }


    public GNDMSJobManagerScriptBuilder setGlobusLocation( final String globusLocation ) {

        this.globusLocation = globusLocation;
        return this;
    }


    public GNDMSJobManagerScriptBuilder setType( final String type ) {

        this.type = type;
        return this;
    }


    public GNDMSJobManagerScriptBuilder setPerlJobDescription( final String perlJobDescription ) {

        this.perlJobDescription = perlJobDescription;
        return this;
    }


    public GNDMSJobManagerScriptBuilder setEnvironment( final String[] environment ) {

        this.environment = environment;
        return this;
    }


    public GNDMSJobManagerScript createGNDMSJobManagerScript() {

        try {
            return new GNDMSJobManagerScriptImpl( username, globusLocation, type,
                    perlJobDescription, environment );
        } catch ( IOException e ) {
            throw new RuntimeException(e);
        }
    }
}