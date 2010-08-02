package de.zib.gndms.model.gorfx.types.io;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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



import org.jetbrains.annotations.NotNull;

import java.util.Properties;
import java.util.HashMap;

/**
 * Writes an ORQ as a Properties instance.
 * It should be used in conjunction with an ORQConverter.
 *
 * @see de.zib.gndms.model.gorfx.types.io.ORQConverter
 * @author: try ma ik jo rr a zib
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 17:23:58
 */
public abstract class ORQPropertyWriter extends AbstractPropertyIO implements ORQWriter {

    protected ORQPropertyWriter() {
    }


    protected ORQPropertyWriter( Properties properties ) {
        super( properties );
    }


    public void writeJustEstimate( boolean je ) {

        getProperties( ).setProperty( SfrProperty.JUST_ASK.key, Boolean.toString( je ) );
    }


    public void read() {
        
    }


    public void writeContext( @NotNull HashMap<String, String> ctx ) {
        PropertyReadWriteAux.writeMap( getProperties(), SfrProperty.CONTEXT.key,  ctx );
    }


    public void writeId( String id ) {
        getProperties( ).setProperty( SfrProperty.GORFX_ID.key, id );
    }
}
