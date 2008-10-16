package de.zib.gndms.GORFX.common.type.io;

import types.ProviderStageInResultT;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInResultWriter;
import de.zib.gndms.GORFX.common.GORFXClientTools;

import javax.xml.soap.SOAPException;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 17:49:16
 */
public class ProviderStageInResultXSDTypeWriter extends AbstractXSDTypeWriter<ProviderStageInResultT>
    implements ProviderStageInResultWriter
{


    public void writeSliceReference( String srf ) {

        // todo convert uid-string to epr
    }


    public void begin() {
        try {
            setProduct( GORFXClientTools.createProviderStageInResultT( )  );
        } catch ( SOAPException e ) {
            e.printStackTrace();
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
        } catch ( InstantiationException e ) {
            e.printStackTrace();
        }
    }


    public void done() {
        // Not required here
    }
}
