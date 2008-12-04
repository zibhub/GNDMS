package de.zib.gndms.typecon.util;

import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.SerializationContext;

import javax.xml.namespace.QName;
import java.util.IdentityHashMap;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.io.Writer;
import java.io.IOException;
import org.apache.axis.encoding.DeserializationContext;
import org.apache.axis.encoding.DeserializerFactory;
import org.apache.axis.encoding.TypeMapping;
import org.apache.axis.encoding.TypeMappingRegistry;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.message.RPCElement;
import org.apache.axis.MessageContext;
import org.apache.axis.Message;
import org.apache.axis.Constants;
import org.apache.axis.handlers.ErrorHandler;
import org.apache.axis.server.AxisServer;
import org.xml.sax.InputSource;

import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.Reader;

/**
 * @author: Maik Jorra <jorra@zib.de>
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
        SOAPBodyElement body = new SOAPBodyElement(inStream);
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


    private static Method findGetTypeDesc( Class c ) throws NoSuchMethodException {

        Method m = c.getMethod( "getTypeDesc" );
        if( m.getReturnType() == TypeDesc.class )
            return m;
        else
            throw new NoSuchMethodException( );
    }
}
