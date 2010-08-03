package de.zib.gndms.model.gorfx.types.io;

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



import de.zib.gndms.model.gorfx.types.SliceStageInResult;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 17.11.2008, Time: 13:28:26
 */
public class SliceStageInResultConverter extends
    CommonSliceResultConverter<CommonSliceResultWriter, SliceStageInResult> {

    public SliceStageInResultConverter( ) {
    }


    public SliceStageInResultConverter( CommonSliceResultWriter writer, SliceStageInResult model ) {
        super( writer, model );
    }

    public void convert() {

        checkedConvert();
        super.convert();    //To change body of overridden methods use File | Settings | File Templates.

        getWriter().done();
    }
}
