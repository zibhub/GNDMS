package de.zib.gndms.model.gorfx.types;

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



/**
 * @author: try ma ik jo rr a zib
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 14:10:06
 */
public class ProviderStageInResult extends AbstractTaskResult {

    private static final long serialVersionUID = -3003504928510518008L;

    private String sliceKey;


    public ProviderStageInResult() {
        super( GORFXConstantURIs.PROVIDER_STAGE_IN_URI );
    }

    public ProviderStageInResult( String sk ) {
        super( GORFXConstantURIs.PROVIDER_STAGE_IN_URI );
        sliceKey = sk;
    }

    public String getSliceKey() {
        return sliceKey;
    }


    public void setSliceKey( String sk ) {
        this.sliceKey = sk;
    }
}
