package de.zib.gndms.taskflows.republishslice.client.tools;

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



import de.zib.gndms.model.gorfx.types.RePublishSliceResult;
import de.zib.gndms.model.gorfx.types.io.CommonSliceResultConverter;
import de.zib.gndms.model.gorfx.types.io.CommonSliceResultWriter;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 17.11.2008, Time: 12:32:02
 */
public class RePublishSliceResultConverter extends
    CommonSliceResultConverter<CommonSliceResultWriter, RePublishSliceResult> {

    public RePublishSliceResultConverter( ) {

    }

    public RePublishSliceResultConverter( CommonSliceResultWriter writer, RePublishSliceResult model ) {
        super( writer, model );
    }

    public void convert() {

        checkedConvert();
        super.convert();    //To change body of overridden methods use File | Settings | File Templates.
        
        getWriter().done();
    }
}
