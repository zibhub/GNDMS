package de.zib.gndms.dspace.subspace.client;

import java.io.InputStream;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.AxisClient;
import org.apache.axis.client.Stub;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import org.oasis.wsrf.properties.GetResourcePropertyResponse;

import org.globus.gsi.GlobusCredential;

import de.zib.gndms.dspace.subspace.stubs.SubspacePortType;
import de.zib.gndms.dspace.subspace.stubs.service.SubspaceServiceAddressingLocator;
import de.zib.gndms.dspace.subspace.common.SubspaceI;
import gov.nih.nci.cagrid.introduce.security.client.ServiceSecurityClient;

/**
 * This class is autogenerated, DO NOT EDIT GENERATED GRID SERVICE ACCESS METHODS.
 *
 * This client is generated automatically by Introduce to provide a clean unwrapped API to the
 * service.
 *
 * On construction the class instance will contact the remote service and retrieve it's security
 * metadata description which it will use to configure the Stub specifically for each method call.
 * 
 * @created by Introduce Toolkit version 1.2
 */
public class SubspaceClient extends SubspaceClientBase implements SubspaceI {	

	public SubspaceClient(String url) throws MalformedURIException, RemoteException {
		this(url,null);	
	}

	public SubspaceClient(String url, GlobusCredential proxy) throws MalformedURIException, RemoteException {
	   	super(url,proxy);
	}
	
	public SubspaceClient(EndpointReferenceType epr) throws MalformedURIException, RemoteException {
	   	this(epr,null);
	}
	
	public SubspaceClient(EndpointReferenceType epr, GlobusCredential proxy) throws MalformedURIException, RemoteException {
	   	super(epr,proxy);
	}

	public static void usage(){
		System.out.println(SubspaceClient.class.getName() + " -url <service url>");
	}
	
	public static void main(String [] args){
	    System.out.println("Running the Grid Service Client");
		try{
		if(!(args.length < 2)){
			if(args[0].equals("-url")){
			  SubspaceClient client = new SubspaceClient(args[1]);
			  // place client calls here if you want to use this main as a
			  // test....
			} else {
				usage();
				System.exit(1);
			}
		} else {
			usage();
			System.exit(1);
		}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

  public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getMultipleResourceProperties");
    return portType.getMultipleResourceProperties(params);
    }
  }

  public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getResourceProperty");
    return portType.getResourceProperty(params);
    }
  }

  public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"queryResourceProperties");
    return portType.queryResourceProperties(params);
    }
  }

  public de.zib.gndms.dspace.slice.stubs.types.SliceReference createSlice(types.SliceCreationSpecifier sliceCreationSpecifier,types.ContextT context) throws RemoteException, de.zib.gndms.dspace.subspace.stubs.types.OutOfSpace, de.zib.gndms.dspace.subspace.stubs.types.UnknownOrInvalidSliceKind, de.zib.gndms.dspace.stubs.types.InternalFailure {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"createSlice");
    de.zib.gndms.dspace.subspace.stubs.CreateSliceRequest params = new de.zib.gndms.dspace.subspace.stubs.CreateSliceRequest();
    de.zib.gndms.dspace.subspace.stubs.CreateSliceRequestSliceCreationSpecifier sliceCreationSpecifierContainer = new de.zib.gndms.dspace.subspace.stubs.CreateSliceRequestSliceCreationSpecifier();
    sliceCreationSpecifierContainer.setSliceCreationSpecifier(sliceCreationSpecifier);
    params.setSliceCreationSpecifier(sliceCreationSpecifierContainer);
    de.zib.gndms.dspace.subspace.stubs.CreateSliceRequestContext contextContainer = new de.zib.gndms.dspace.subspace.stubs.CreateSliceRequestContext();
    contextContainer.setContext(context);
    params.setContext(contextContainer);
    de.zib.gndms.dspace.subspace.stubs.CreateSliceResponse boxedResult = portType.createSlice(params);
    return boxedResult.getSliceReference();
    }
  }

  public org.apache.axis.types.URI[] listCreatableSliceKinds() throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"listCreatableSliceKinds");
    de.zib.gndms.dspace.subspace.stubs.ListCreatableSliceKindsRequest params = new de.zib.gndms.dspace.subspace.stubs.ListCreatableSliceKindsRequest();
    de.zib.gndms.dspace.subspace.stubs.ListCreatableSliceKindsResponse boxedResult = portType.listCreatableSliceKinds(params);
    return boxedResult.getSliceKind();
    }
  }

}
