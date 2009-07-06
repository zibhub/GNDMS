package de.zib.gndms.typecon.common.type;

import de.zib.gndms.model.gorfx.types.AbstractORQ;
import types.ContextT;

/**
 * An AbstractORQXSDReader is used to created an AbstractORQ instance and load the ContextT into the object.
 * See {@link #read(Class, types.ContextT)} 
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 14.10.2008, Time: 13:16:31
 */
public class AbstractORQXSDReader {

    /**
     * Creates an instance for the given class {@code clazz} and sets its attribute
     * {@link de.zib.gndms.model.gorfx.types.AbstractORQ#actContext} according to {@code ctx}.
     * The instance is then returned.
     *
     * @param clazz the class object of an AbstractORQ class
     * @param ctx the value of the parameter {@code ContextT} in the corresponding xml-entry
     * @param <M> a subclass of AbstractORQ, specifying {@code clazz}
     * @return an AbstractORQ of the given class {@code clazz}
     *       and sets its parameter {@code actContext} according to {@code ctx}
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <M extends AbstractORQ> M read( Class<M> clazz, ContextT ctx ) throws IllegalAccessException, InstantiationException {

        M orq = clazz.newInstance();
        if( ctx != null )
            orq.setActContext( ContextXSDReader.readContext( ctx ) );

        return orq;
    }


}
