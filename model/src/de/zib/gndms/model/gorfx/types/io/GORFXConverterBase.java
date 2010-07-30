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



/**
 * A GORFXConverterBase uses a writer to convert a gndms class to a desired type, which can be the corresponding
 * axis type, a convertion to a Properties instance, or it will be written to Stdout.
 *
 *
 * The template parameter W specifies the writer and M the model, so a gndms class.
 * 
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 22.09.2008, Time: 18:09:02
 */
public abstract class GORFXConverterBase<W,M> {

    /**
     * A writer which is used to create a new axis type.
     */
    private W writer;

    /**
     * A gndms class instance, which shall be converted to an axis type.
     */
    private M model;


    public GORFXConverterBase() {
    }


    public GORFXConverterBase( W writer, M model ) {
        this.writer = writer;
        this.model = model;
    }


    /**
     * Performs the writing by calling the getters of the model and
     * the corresponding write* methods of the writer.
     *
     * Note: implementations of this method are required to ensure that
     * model and writer are provided and to call the open and done method of
     * the writer.
     */
    public abstract void convert( );


    public W getWriter() {
        return writer;
    }


    public void setWriter( W writer ) {
        this.writer = writer;
    }

    /**
     * Returns the original model, which shall be converted to an axis type
     * @return the original model, which shall be converted to an axis type
     */
    public M getModel() {
        return model;
    }


    public void setModel( M model ) {
        this.model = model;
    }


    public static String NotNullString( String s ) {
        return ( s == null ) ? new String( ) : s;
    }

    public static String[] NotNullStringArray( String[] sa ) {
        return ( sa == null ) ? new String[0] : sa;
    }
}
