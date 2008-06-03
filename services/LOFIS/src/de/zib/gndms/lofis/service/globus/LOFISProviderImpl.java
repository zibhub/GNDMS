package de.zib.gndms.lofis.service.globus;

import de.zib.gndms.lofis.service.LOFISImpl;

import java.rmi.RemoteException;

/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * This class implements each method in the portType of the service.  Each method call represented
 * in the port type will be then mapped into the unwrapped implementation which the user provides
 * in the LOFISImpl class.  This class handles the boxing and unboxing of each method call
 * so that it can be correclty mapped in the unboxed interface that the developer has designed and 
 * has implemented.  Authorization callbacks are automatically made for each method based
 * on each methods authorization requirements.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class LOFISProviderImpl{
	
	LOFISImpl impl;
	
	public LOFISProviderImpl() throws RemoteException {
		impl = new LOFISImpl();
	}
	

    public de.zib.gndms.lofis.stubs.CreateLofiSetResponse createLofiSet(de.zib.gndms.lofis.stubs.CreateLofiSetRequest params) throws RemoteException, de.zib.gndms.lofis.stubs.types.ConflictingDestinationsInMap, de.zib.gndms.lofis.stubs.types.MissingSourceFiles, de.zib.gndms.lofis.stubs.types.ConflictResolutionFailed, de.zib.gndms.lofis.stubs.types.UnsupportedOrInvalidSlice {
    de.zib.gndms.lofis.stubs.CreateLofiSetResponse boxedResult = new de.zib.gndms.lofis.stubs.CreateLofiSetResponse();
    boxedResult.setLofiSetReference(impl.createLofiSet(params.getReplicaSlices().getReplicaSlices(),params.getSliceReference().getSliceReference()));
    return boxedResult;
  }

    public de.zib.gndms.lofis.stubs.MergeLofiSetsResponse mergeLofiSets(de.zib.gndms.lofis.stubs.MergeLofiSetsRequest params) throws RemoteException, de.zib.gndms.lofis.stubs.types.ConflictingLofiSetsInMerge {
    de.zib.gndms.lofis.stubs.MergeLofiSetsResponse boxedResult = new de.zib.gndms.lofis.stubs.MergeLofiSetsResponse();
    boxedResult.setLofiSetReference(impl.mergeLofiSets(params.getLofiSetRefs().getLofiSetReference()));
    return boxedResult;
  }

}