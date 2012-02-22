package de.zib.gndms.model.gorfx.types;
/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import de.zib.gndms.common.model.gorfx.types.AbstractTaskFlowResult;
import de.zib.gndms.common.rest.Specifier;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 14.02.12  14:08
 * @brief
 */
public class SliceResultImpl extends AbstractTaskFlowResult<Specifier<Void>>
        implements SliceResult
{

    private static final long serialVersionUID = -1349575047276838601L;
    /**
     * The resulting specifier.
     *
     * Describes the slice containing the staged files.
     */
    protected Specifier<Void> sliceSpecifier;


    public SliceResultImpl( final String taskFlowType ) {

        super( taskFlowType );
    }


    public SliceResultImpl( final String taskFlowType, final Specifier<Void> sliceSpecifier ) {

        super( taskFlowType );
        this.sliceSpecifier = sliceSpecifier;
    }


    @Override
    public Specifier<Void> getSliceSpecifier() {

        return sliceSpecifier;
    }


    @Override
    public void setSliceSpecifier( final Specifier<Void> sliceSpecifier ) {

        this.sliceSpecifier = sliceSpecifier;
    }


    @Override
    public Specifier<Void> getResult() {

        return getSliceSpecifier();
    }


    /**
     * @deprecated
     * @return
     */
    public Specifier<Void> getSliceKey() {
        return getSliceSpecifier();
    }
}
