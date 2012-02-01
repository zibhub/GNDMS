package de.zib.gndms.taskflows.staging.client.model;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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


import de.zib.gndms.common.model.gorfx.types.AbstractTaskFlowResult;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.taskflows.staging.client.ProviderStageInMeta;


/**
 * Class for the result of the staging taskflow.
 *
 * The result is a specifier for the slice containing the staged data. The specifier can be used
 * to access the slice directly through the provided url, or to extract the url map and use it
 * as input for the {@link #SliceClient}
 *
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 14:10:06
 */
public class ProviderStageInResult extends AbstractTaskFlowResult<Specifier<Void>> {

    private static final long serialVersionUID = -3003504928510518008L;

    /**
     * The resulting specifier.
     *
     * Describes the slice containing the staged files.
     */
    private Specifier<Void> sliceKey;


    public ProviderStageInResult() {
        super( ProviderStageInMeta.PROVIDER_STAGING_KEY );
    }

    public ProviderStageInResult( Specifier<Void> sk ) {
        super(  ProviderStageInMeta.PROVIDER_STAGING_KEY );
        sliceKey = sk;
    }

    public Specifier<Void> getSliceKey() {
        return sliceKey;
    }


    public void setSliceKey( Specifier<Void> sk ) {
        this.sliceKey = sk;
    }


    @Override
    public Specifier<Void> getResult() {

        return getSliceKey();
    }
}
