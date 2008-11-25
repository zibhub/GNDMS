package de.zib.gndms.typecon.util;

import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.SerializationContext;

import javax.xml.namespace.QName;
import java.util.IdentityHashMap;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.io.Writer;
import java.io.IOException;
import java.io.StringWriter;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 12.11.2008, Time: 14:39:36
 */
public class AxisTypeToXML {

    private static final IdentityHashMap<Class, Method> methodMap = new IdentityHashMap<Class, Method>( );

    /**
     * Delivers a string containing the XML doc for a  of a given instance.
     *
     * @param o The axis object, it must provide a getTypDesc method, which returns the xsd description
     *          of the object itself.
     * @param hide If true empty xml entries are omitted.
     * @param type If true xsd type information will be included in xml tags.
     * @return A string containing a xml doc, or a null string if Object isn't a valid axis object.
     */
    @SuppressWarnings( { "BooleanConstructorCall", "UnnecessaryBoxing" } )
    public static String convert( Object o, boolean hide, boolean type ) {

        try {
            Class c = o.getClass( );
            Method m;
            if( methodMap.containsKey( c ) ) {
                m = methodMap.get( c );
            } else {
                m = findGetTypeDesc( c );
            }

            TypeDesc td = (TypeDesc) m.invoke( o );
            // method seems to be okay
            methodMap.put( c, m );

            QName rootName = td.getXmlType();
            Writer w = new StringWriter( );
            SerializationContext ctx = new SerializationContext( w );
            ctx.setPretty( true );
            ctx.serialize( rootName, null, o, null, new Boolean( hide ), new Boolean( type ) );

            return w.toString( );

        } catch ( NoSuchMethodException e ) {
            e.printStackTrace();
            return null;
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
            return null;
        } catch ( InvocationTargetException e ) {
            e.printStackTrace();
            return null;
        } catch ( IOException e ) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * This method is provided for convenience and behaves exactly like the above method with
     * hide=false and type=false;
     * @param o The object
     * @returns s.a. 
     */
    public static String convert( Object o ) {

        return convert(  o, false, false );
    }


    private static Method findGetTypeDesc( Class c ) throws NoSuchMethodException {

        Method m = c.getMethod( "getTypeDesc" );
        if( m.getReturnType() == TypeDesc.class )
            return m;
        else
            throw new NoSuchMethodException( );
    }
}
