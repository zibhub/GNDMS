package de.zib.gndms.dspace.subspace.common;

import java.rmi.RemoteException;

/** 
 * This class is autogenerated, DO NOT EDIT.
 * 
 * This interface represents the API which is accessable on the grid service from the client. 
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public interface SubspaceI {

  public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element params) throws RemoteException ;

  public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName params) throws RemoteException ;

  public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element params) throws RemoteException ;

  /**
   * Creates a new slice with the given kind in this subspace
   *
   * @param sliceCreationSpecifier
   * @param context
   * @throws OutOfSpace
   *	
   * @throws UnknownOrInvalidSliceKind
   *	
   * @throws InternalFailure
   *	
   */
  public de.zib.gndms.dspace.slice.stubs.types.SliceReference createSlice(types.SliceCreationSpecifier sliceCreationSpecifier,types.ContextT context) throws RemoteException, de.zib.gndms.dspace.subspace.stubs.types.OutOfSpace, de.zib.gndms.dspace.subspace.stubs.types.UnknownOrInvalidSliceKind, de.zib.gndms.dspace.stubs.types.InternalFailure ;

  /**
   * Returns a (possibly incompletet) list of all slice kinds supported by this subspaces CreateSlice() method
   *
   */
  public org.apache.axis.types.URI[] listCreatableSliceKinds() throws RemoteException ;

}

