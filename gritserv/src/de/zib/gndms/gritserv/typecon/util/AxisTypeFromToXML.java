package de.zib.gndms.gritserv.typecon.util;

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



import org.apache.axis.Constants;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.*;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.message.RPCElement;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.server.AxisServer;
import org.xml.sax.InputSource;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.IdentityHashMap;

/**
 * @author: try ma ik jo rr a zib
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 12.11.2008, Time: 14:39:36
 */
public class AxisTypeFromToXML {

    private static final IdentityHashMap<Class, Method> methodMap = new IdentityHashMap<Class, Method>( );

    /**
     * Writes the XML doc for a given instance to a given writer.
     *
     * @param w The target writer.
     * @param o The axis object, it must provide a getTypDesc method, which returns the xsd description
     *          of the object itself.
     * @param hide If true empty xml entries are omitted.
     * @param type If true xsd type information will be included in xml tags.
     * @throws java.io.IOException If something gets wrong with the writer.
     */
    @SuppressWarnings( { "BooleanConstructorCall", "UnnecessaryBoxing" } )
    public static void toXML( Writer w, Object o, boolean hide, boolean type ) throws IOException {

            TypeDesc td = typeDescForClass( o.getClass() );
            QName rootName = td.getXmlType();
            SerializationContext ctx = new SerializationContext( w );
            ctx.setPretty( true );
            ctx.serialize( rootName, null, o, null, new Boolean( hide ), new Boolean( type ) );

    }


    /**
     * This method is provided for convenience and behaves exactly like the above method with
     * hide=false and type=false;
     * @param w The writer s.a.
     * @param o The object s.a.
     * @throws java.io.IOException If something gets wrong with the writer.
     */
    public static void toXML( Writer w, Object o ) throws IOException {

        toXML(  w, o, false, false );
    }

    /**
     * Reads the the first element of the SOAP body for a given XML-InputStream
     *  and returns its content.
     * element.
     *
     * @param inStream an XML-InputStream
     * @param cls specifies the class of the element's content
     * @param <M>
     * @return the content of the first element of the SOAP body
     * @throws Exception
     */
    public static <M> M fromXML( InputStream inStream, Class<M> cls ) throws Exception {

        TypeDesc td = typeDescForClass( cls );
        final QName qname = td.getXmlType();
        DeserializerFactory dserFactory = new BeanDeserializerFactory( cls, td.getXmlType());
        DeserializationContext context = createDeserializationContext(inStream, qname, cls, dserFactory);
        context.parse();
        SOAPEnvelope env = context.getEnvelope();
        RPCElement body = ( RPCElement )env.getFirstBody();
        return cls.cast( body.getValueAsType( qname, cls ) );
    }

    
    private static DeserializationContext createDeserializationContext(
        InputStream inStream,
        QName elementName,
        Class javaClass,
        DeserializerFactory factory)
    {
        DeserializationContext context;
        SOAPEnvelope env = new SOAPEnvelope();
        SOAPBodyElement body = new SOAPBodyElement( inStream );
        env.addBodyElement(body);

        MessageContext mctx = new MessageContext(new AxisServer());
        InputSource is = new InputSource(new StringReader(env.toString()));
        context = new DeserializationContext(is, mctx, Message.RESPONSE);

        TypeMappingRegistry reg = context.getTypeMappingRegistry();
        TypeMapping tm = (TypeMapping)reg.getTypeMapping("");
        if (tm == null) {
            tm = ( TypeMapping ) reg.createTypeMapping();
            reg.register( Constants.URI_DEFAULT_SOAP_ENC, tm);
        }
        tm.register(javaClass, elementName, null, factory);

        return context;
    }

    /**
     * Returns the xsd schema, as {@code TypeDesc} for a given class.
     *
     * Retrieves the static method "getTypeDesc" from the class {@code c} and puts it,
     * if not already done to the map {@link #methodMap}.
     * It invokes the method on the given class and returns the result.
     *
     * @see org.apache.axis.types.Schema#getTypeDesc() 
     * @param c a class, which must have inplemented the static method getTypeDesc
     * @return the xsd schema for the desired class
     */
    private static TypeDesc typeDescForClass( Class c ) {

        try {
            Method m;
            if( methodMap.containsKey( c ) ) {
                m = methodMap.get( c );
            } else {
                m = findGetTypeDesc( c );
            }

            TypeDesc td = (TypeDesc) m.invoke( c );
            // method seems to be okay
            methodMap.put( c, m );

            return td;
            
        } catch ( NoSuchMethodException e ) {
            throw new IllegalArgumentException( "Given object doesn't appear to be an axis xsd-type implementation.", e );
        } catch ( IllegalAccessException e ) {
            throw new IllegalArgumentException( "getTypeDesc method is inaccessible", e );
        } catch ( InvocationTargetException e ) {
            throw new IllegalArgumentException( "getTypeDesc invocation failed.", e );
        }
    }

    /**
     * Returns the method object corresponding to axis' method "getTypeDesc" which returns a TypeDesc object,
     * from the given class {@code c}.
     *
     * @param c a class which must have the axis method "getTypeDesc" implemented
     * @return the corresponding method object
     * @throws NoSuchMethodException
     */
    private static Method findGetTypeDesc( Class c ) throws NoSuchMethodException {

        Method m = c.getMethod( "getTypeDesc" );
        if( m.getReturnType() == TypeDesc.class )
            return m;
        else
            throw new NoSuchMethodException( );
    }
}
