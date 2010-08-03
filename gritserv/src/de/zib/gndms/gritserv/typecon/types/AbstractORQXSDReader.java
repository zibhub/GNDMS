package de.zib.gndms.gritserv.typecon.types;

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



import de.zib.gndms.model.gorfx.types.AbstractORQ;
import types.ContextT;

/**
 * An AbstractORQXSDReader is used to created an AbstractORQ instance and load the ContextT into the object.
 * See {@link #read(Class, types.ContextT)} 
 *
 * @author  try ma ik jo rr a zib
 * @version  $Id$
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
