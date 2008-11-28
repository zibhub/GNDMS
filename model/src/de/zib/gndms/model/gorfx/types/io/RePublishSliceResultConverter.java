package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.RePublishSliceResult;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
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
