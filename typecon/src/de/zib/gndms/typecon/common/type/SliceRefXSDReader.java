package de.zib.gndms.typecon.common.type;

import de.zib.gndms.model.dspace.types.SliceRef;
import types.SliceReference;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.message.MessageElement;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 14:30:28
 */
public class SliceRefXSDReader  {

    public static SliceRef read( SliceReference rf ) {

        return fromEPR( rf.getEndpointReference() );
    }


    public static SliceRef fromEPR( EndpointReferenceType epr  ) {

        SliceRef sr = new SliceRef();
        sr.setGridSiteId( epr.getAddress().toString( ) );
        MessageElement me = epr.getProperties().get( sr.getResourceKeyName() );
        try {
            sr.setResourceKeyValue( ( String ) me.getObjectValue( String.class ) );
        } catch ( Exception e ) {
            throw new IllegalArgumentException( e.getMessage(), e );
        }

        return sr;
    }
}
