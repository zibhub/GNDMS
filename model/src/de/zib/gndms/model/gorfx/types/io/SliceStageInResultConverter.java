package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.SliceStageInResult;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
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