package de.zib.gndms.GORFX.service.globus.resource;

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



import de.zib.gndms.model.gorfx.types.GORFXConstantURIs;
import org.apache.axis.types.URI;

import java.util.ArrayList;


/** 
 * The implementation of this GORFXResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class GORFXResource extends GORFXResourceBase {

    private ExtGORFXResourceHome home;
    private URI[] supportedOfferTypes = null;

    public URI[] getSupportedOfferTypes() {

        if ( supportedOfferTypes == null ) {
            ArrayList<URI> al = new ArrayList<URI>( GORFXConstantURIs.ALL_URIS.size( ) );
            for( String s : GORFXConstantURIs.ALL_URIS )
                try {
                    al.add( new URI( s ) );
                } catch ( URI.MalformedURIException e ) {
                    // not supposed to happen
                    e.printStackTrace();
                }
            supportedOfferTypes = al.toArray( new URI[0] );
        }

        return supportedOfferTypes;
    }


    public ExtGORFXResourceHome getHome() {
        return home;
    }


    public void setHome( ExtGORFXResourceHome extGORFXResourceHome ) {
        home = extGORFXResourceHome;
    }
}
