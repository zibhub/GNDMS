package de.zib.gndms.GORFX.action;

import de.zib.gndms.model.dspace.types.SliceRef;
import de.zib.gndms.model.gorfx.types.io.SliceRefConverter;
import de.zib.gndms.dspace.slice.client.SliceClient;
import de.zib.gndms.typecon.common.type.SliceRefXSDTypeWriter;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;

import java.rmi.RemoteException;
import java.util.GregorianCalendar;

/**
 * This class provides methods which are clients of the DSpace service.
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 17:02:53
 */
public class DSpaceBindingUtils {

    
    /**
     * Use dspace client calls
     * @param epr A endpoint reference which must reference a slice resource.
     *
     * @return A string containing the gsi ftp path to the slice.
     */
    public static String getFtpPathForSlice( EndpointReferenceType epr ) throws URI.MalformedURIException, RemoteException {

        SliceClient sc = new SliceClient( epr );
        return sc.getSliceLocation();
    }


    /**
     * Provided for convenience and behaves just like the above method.
     */
    public static String getFtpPathForSlice( SliceRef sr ) throws URI.MalformedURIException, RemoteException {
        return getFtpPathForSlice( SliceRefXSDTypeWriter.write( sr ).getEndpointReference() );
    }


    /**
     * Destroy the slice referenced by sr.
     *
     * Destruction will be performed by setting the slices termination time to now.
     */
    public static void destroySlice( SliceRef sr ) throws Exception {
        SliceClient sc = SliceClientForRef( sr );
        sc.setTerminationTime( new GregorianCalendar( ) );
    }


    /**
     * Constructs an epr form a slice reference and uses it to instantiate a sliceclient.
     */
    public static SliceClient SliceClientForRef ( SliceRef sr ) throws Exception {
        return new SliceClient( SliceRefXSDTypeWriter.getEPRforSliceRef( sr ) );
    }
}
