/*
 * Copyright 2008-2012 Zuse Institute Berlin (ZIB)
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
 *
 */

package de.zib.gndms.common.model.gorfx.types;

import de.zib.gndms.common.dspace.SliceConfiguration;

/**
 * @date: 28.08.12
 * @time: 09:03
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public abstract class SliceOrder extends AbstractOrder {

    private String sliceId;
    private SliceConfiguration sliceConfiguration;


    public boolean hasSliceId() {
        return getSliceId() != null;
    }


    public String getSliceId() {
        return sliceId;
    }


    public void setSliceId( String sliceId ) {
        this.sliceId = sliceId;
    }


    public boolean hasSliceConfiguration() {
        return getSliceConfiguration() != null;
    }


    public SliceConfiguration getSliceConfiguration() {
        return sliceConfiguration;
    }


    public void setSliceConfiguration(SliceConfiguration sliceConfiguration) {
        this.sliceConfiguration = sliceConfiguration;
    }
}
