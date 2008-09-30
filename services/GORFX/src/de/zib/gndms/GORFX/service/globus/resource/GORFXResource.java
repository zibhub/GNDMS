package de.zib.gndms.GORFX.service.globus.resource;

import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;
import org.apache.axis.types.URI;

import java.util.ArrayList;

import de.zib.gndms.GORFX.common.GORFXClientTools;
import de.zib.gndms.model.gorfx.types.GORFXConstantURIs;


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
