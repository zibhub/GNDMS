package de.zib.gndms.model.gorfx.types;

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



import de.zib.gndms.model.dspace.types.SliceRef;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 14.11.2008, Time: 15:32:37
 */
public class SliceStageInResult extends CommonSliceResult {

    public SliceStageInResult( ) {
        super( GORFXConstantURIs.SLICE_STAGE_IN_URI );
    }

    public SliceStageInResult( SliceRef sr ) {
        super( GORFXConstantURIs.SLICE_STAGE_IN_URI );
        setSliceRef( sr );
    }
}
