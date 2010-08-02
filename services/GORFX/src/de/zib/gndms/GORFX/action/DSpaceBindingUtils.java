package de.zib.gndms.GORFX.action;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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



import de.zib.gndms.dspace.slice.client.SliceClient;
import de.zib.gndms.model.dspace.types.SliceRef;
import de.zib.gndms.gritserv.typecon.types.SliceRefXSDTypeWriter;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;

import java.rmi.RemoteException;
import java.util.GregorianCalendar;

/**
 * This class provides methods which are clients of the DSpace service.
 * @author: try ma ik jo rr a zib
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
