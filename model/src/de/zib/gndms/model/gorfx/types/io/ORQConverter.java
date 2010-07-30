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



import de.zib.gndms.model.gorfx.types.AbstractORQ;

/**
 * An ORQConverter is used to convert {@link AbstractORQ}s to a desired type,
 * which can be their corresponding axis type, a convertion to a Properties instance, or it will be written to Stdout.
 *
 * It provices an implementation of {@link #convert()} which must be invoked to do the actuall convertion.
 *
 * An {@link ORQWriter} for the corresponding model must be provided.
 *
 * The template parameter W specifies the writer and M the model.
 *
 * @see de.zib.gndms.model.gorfx.types.io.ORQWriter
 * @see de.zib.gndms.model.gorfx.types.AbstractORQ
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 17:52:45
 */
public abstract class ORQConverter<W extends ORQWriter, M extends AbstractORQ> extends GORFXConverterBase<W, M> {

    protected ORQConverter() {
    }


    protected ORQConverter( W writer, M model ) {
        super( writer, model );
    }

    public void convert( ) {

        if( getWriter( ) == null || getModel() == null )
            throw new IllegalStateException( );

        getWriter().begin();
        getWriter().writeJustEstimate( getModel().isJustEstimate() );
        if( getModel( ).hasContext( ) ) 
            getWriter().writeContext( getModel().getActContext( ) );

        if( getModel( ).hasId( ) )
            getWriter().writeId( getModel().getActId( ) );
    }
}
