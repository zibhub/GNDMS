package de.zib.gndms.gritserv.rest;
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

import de.zib.gndms.stuff.devel.StreamConsumer;
import de.zib.gndms.stuff.devel.StreamCopyNIO;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author try ma ik jo rr a zib
 * @date 24.03.11  11:06
 * @brief Custom jackson message converter, workes around some jackson bugs.
 */
public class FixedMappingJacksonHttpMessageConverter extends MappingJacksonHttpMessageConverter {

    private ObjectMapper om = null;
    protected Logger logger = LoggerFactory.getLogger( this.getClass() );

    @Override
    public void setObjectMapper( ObjectMapper objectMapper ) {
        if( om != null )
            throw new IllegalStateException( "ObjectMapper already set" );

        super.setObjectMapper( objectMapper );    // overriden method implementation
        om = objectMapper;
    }


    public ObjectMapper getObjectMapper() {
        return om;
    }


    /**
     * This one messes up the jackson context.
     * @param clazz
     * @param mediaType
     * @return Always \c true;
     */
    @Override
    public boolean canRead( Class<?> clazz, MediaType mediaType ) {
        //return super.canRead( clazz, mediaType );    // overriden method implementation
        return true;
    }


    /**
     * This one messes up the jackson context, too.
     * @param clazz
     * @param mediaType
     * @return Always \c true;
     */
    @Override
    public boolean canWrite( Class<?> clazz, MediaType mediaType ) {
        // return super.canWrite( clazz, mediaType );    // overriden method implementation
        return true;
    }


    @Override
    protected Object readInternal( Class<?> clazz, HttpInputMessage inputMessage ) throws IOException, HttpMessageNotReadableException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream( 512*1024 );
        StreamCopyNIO.copyStream( inputMessage.getBody(), bos );
        byte[] ba = bos.toByteArray();
        logger.debug( "called with "+ new String( ba ) );
        ByteArrayInputStream bin = new ByteArrayInputStream( ba );
        return om.readValue( bin, clazz );
    }


    @Override
    protected void writeInternal( Object o, HttpOutputMessage outputMessage ) throws IOException, HttpMessageNotWritableException {
        logger.debug( "called with "+ o.toString() );
        ByteArrayOutputStream bout = new ByteArrayOutputStream( 512*1024 );
        om.writeValue( bout, o );
        byte[] ba = bout.toByteArray();
        logger.debug( "generated JSON "+ new String( ba ) );
        ByteArrayInputStream bin = new ByteArrayInputStream( ba );
        StreamCopyNIO.copyStream( bin, outputMessage.getBody() );
        // super.writeInternal( o, outputMessage );    // overriden method implementation
    }
}
