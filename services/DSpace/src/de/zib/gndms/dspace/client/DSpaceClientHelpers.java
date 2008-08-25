package de.zib.gndms.dspace.client;

import de.zib.gndms.dspace.subspace.client.SubspaceClient;
import de.zib.gndms.dspace.subspace.stubs.types.UnknownOrInvalidSliceKind;
import de.zib.gndms.dspace.subspace.stubs.types.OutOfSpace;
import de.zib.gndms.dspace.subspace.stubs.types.SubspaceReference;
import de.zib.gndms.dspace.slice.client.SliceClient;
import de.zib.gndms.dspace.slice.stubs.types.SliceReference;
import de.zib.gndms.dspace.stubs.types.InternalFailure;
import de.zib.gndms.dspace.stubs.types.UnknownSubspace;
import de.zib.gndms.dspace.common.DSpaceTools;
import de.zib.gndms.model.dspace.StorageSize;
import de.zib.gndms.model.common.ImmutableScopedName;

import java.util.Calendar;
import java.rmi.RemoteException;

import types.SliceCreationSpecifier;
import org.apache.axis.types.URI;

import javax.xml.namespace.QName;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 22.08.2008, Time: 13:20:10
 */
public class DSpaceClientHelpers {


    public static SliceClient createSliceInSubspace( SubspaceClient spc,  String sliceKindURI,
                                              Calendar terminationTime, StorageSize totalStorageSize ) throws URI.MalformedURIException, RemoteException, OutOfSpace {

        SliceCreationSpecifier scs = new SliceCreationSpecifier( );
        //scs.setSliceKind( sliceKindURI );
        scs.setTerminationTime( terminationTime );
        scs.setTotalStorageSize( DSpaceTools.buildSizeT( totalStorageSize ));

        return createSliceInSubspace( spc, scs );
    }

    
    public static SliceClient createSliceInSubspace( SubspaceClient spc, SliceCreationSpecifier specifier ) throws RemoteException, UnknownOrInvalidSliceKind, OutOfSpace, URI.MalformedURIException {

        SliceReference sr = spc.createSlice( specifier, null );

        return new SliceClient( sr.getEndpointReference() );
    }


    public static SubspaceClient findSubspace( DSpaceClient client, String scopeName, String localName ) throws URI.MalformedURIException, RemoteException {

        return findSubspace( client, new ImmutableScopedName( scopeName, localName ) );
    }


    public static SubspaceClient findSubspace( DSpaceClient client, ImmutableScopedName name ) throws URI.MalformedURIException, RemoteException {

        return findSubspace( client, name.toQName() );
    }
    

    public static SubspaceClient findSubspace( DSpaceClient client, QName name ) throws RemoteException, URI.MalformedURIException {

        SubspaceReference sr = client.getSubspace( name );
        return new SubspaceClient( sr.getEndpointReference() );
    }


    public static SliceClient changeSliceSubspace( ) { }
}
