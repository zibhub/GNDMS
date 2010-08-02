/**
 * TransformSliceToRequestContext.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.slice.stubs;

public class TransformSliceToRequestContext  implements java.io.Serializable {
    private types.ContextT context;

    public TransformSliceToRequestContext() {
    }

    public TransformSliceToRequestContext(
           types.ContextT context) {
           this.context = context;
    }


    /**
     * Gets the context value for this TransformSliceToRequestContext.
     * 
     * @return context
     */
    public types.ContextT getContext() {
        return context;
    }


    /**
     * Sets the context value for this TransformSliceToRequestContext.
     * 
     * @param context
     */
    public void setContext(types.ContextT context) {
        this.context = context;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TransformSliceToRequestContext)) return false;
        TransformSliceToRequestContext other = (TransformSliceToRequestContext) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.context==null && other.getContext()==null) || 
             (this.context!=null &&
              this.context.equals(other.getContext())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getContext() != null) {
            _hashCode += getContext().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TransformSliceToRequestContext.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Slice", ">>TransformSliceToRequest>context"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("context");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "Context"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "ContextT"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
