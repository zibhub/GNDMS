package de.zib.gndms.dspace.slice.service.globus;

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



import de.zib.gndms.dspace.slice.service.SliceImpl;

import java.rmi.RemoteException;

/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * This class implements each method in the portType of the service.  Each method call represented
 * in the port type will be then mapped into the unwrapped implementation which the user provides
 * in the DSpaceImpl class.  This class handles the boxing and unboxing of each method call
 * so that it can be correclty mapped in the unboxed interface that the developer has designed and 
 * has implemented.  Authorization callbacks are automatically made for each method based
 * on each methods authorization requirements.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class SliceProviderImpl{
	
	SliceImpl impl;
	
	public SliceProviderImpl() throws RemoteException {
		impl = new SliceImpl();
	}
	

    public de.zib.gndms.dspace.slice.stubs.TransformSliceToResponse transformSliceTo(de.zib.gndms.dspace.slice.stubs.TransformSliceToRequest params) throws RemoteException, de.zib.gndms.dspace.stubs.types.UnknownSubspace, de.zib.gndms.dspace.subspace.stubs.types.OutOfSpace, de.zib.gndms.dspace.subspace.stubs.types.UnknownOrInvalidSliceKind, de.zib.gndms.dspace.stubs.types.InternalFailure {
    de.zib.gndms.dspace.slice.stubs.TransformSliceToResponse boxedResult = new de.zib.gndms.dspace.slice.stubs.TransformSliceToResponse();
    boxedResult.setSliceReference(impl.transformSliceTo(params.getSliceTransformSpecifier().getSliceTransformSpecifier(),params.getContext().getContext()));
    return boxedResult;
  }

}
