package de.zib.gndms.model.gorfx.types.io.xml;

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



import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.model.gorfx.types.AbstractOrder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 17.11.2008, Time: 14:01:37
 */
public interface ProviderStageInXML {

    /**
     * Converts the given object to a suitable XML represenation.
     * 
     * The order must allways be provided, the contract can be null
     *
     * @return An XML representation of the in parameters.
     */
    String toDocument( AbstractOrder order,  Quote con ) throws IOException;

    /**
     * Writes XML document using the provided writer.
     *
     * The order must allways be provided, the contract can be null
     */
    void toDocument( Writer w, AbstractOrder order, Quote con ) throws IOException;


    /**
     * Reads order information from a given XML document.
     *
     * @return The order and contract.
     */
    ORQWrapper fromDocument( String doc ) throws Exception;


    /**
     * Reads order information using a given stream.
     *
     * @return The order and contract.
     */
    ORQWrapper fromDocument( InputStream r ) throws Exception;
}
