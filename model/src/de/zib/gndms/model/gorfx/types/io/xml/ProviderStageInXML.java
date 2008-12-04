package de.zib.gndms.model.gorfx.types.io.xml;

import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.types.AbstractORQ;

import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 17.11.2008, Time: 14:01:37
 */
public interface ProviderStageInXML {

    /**
     * Converts the given object to a suitable XML represenation.
     * 
     * The orq must allways be provided, the contract can be null
     *
     * @return An XML representation of the in parameters.
     */
    String toDocument( AbstractORQ orq,  TransientContract con ) throws IOException;

    /**
     * Writes XML document using the provided writer.
     *
     * The orq must allways be provided, the contract can be null
     */
    void toDocument( Writer w, AbstractORQ orq, TransientContract con ) throws IOException;


    /**
     * Reads orq information from a given XML document.
     *
     * @return The orq and contract.
     */
    ORQWrapper fromDocument( String doc ) throws Exception;


    /**
     * Reads orq information using a given stream.
     *
     * @return The orq and contract.
     */
    ORQWrapper fromDocument( InputStream r ) throws Exception;
}
