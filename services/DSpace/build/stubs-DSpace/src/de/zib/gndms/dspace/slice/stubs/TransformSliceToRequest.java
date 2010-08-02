/**
 * TransformSliceToRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.slice.stubs;

public class TransformSliceToRequest  implements java.io.Serializable {
    private de.zib.gndms.dspace.slice.stubs.TransformSliceToRequestSliceTransformSpecifier sliceTransformSpecifier;
    private de.zib.gndms.dspace.slice.stubs.TransformSliceToRequestContext context;

    public TransformSliceToRequest() {
    }

    public TransformSliceToRequest(
           de.zib.gndms.dspace.slice.stubs.TransformSliceToRequestContext context,
           de.zib.gndms.dspace.slice.stubs.TransformSliceToRequestSliceTransformSpecifier sliceTransformSpecifier) {
           this.sliceTransformSpecifier = sliceTransformSpecifier;
           this.context = context;
    }


    /**
     * Gets the sliceTransformSpecifier value for this TransformSliceToRequest.
     * 
     * @return sliceTransformSpecifier
     */
    public de.zib.gndms.dspace.slice.stubs.TransformSliceToRequestSliceTransformSpecifier getSliceTransformSpecifier() {
        return sliceTransformSpecifier;
    }


    /**
     * Sets the sliceTransformSpecifier value for this TransformSliceToRequest.
     * 
     * @param sliceTransformSpecifier
     */
    public void setSliceTransformSpecifier(de.zib.gndms.dspace.slice.stubs.TransformSliceToRequestSliceTransformSpecifier sliceTransformSpecifier) {
        this.sliceTransformSpecifier = sliceTransformSpecifier;
    }


    /**
     * Gets the context value for this TransformSliceToRequest.
     * 
     * @return context
     */
    public de.zib.gndms.dspace.slice.stubs.TransformSliceToRequestContext getContext() {
        return context;
    }


    /**
     * Sets the context value for this TransformSliceToRequest.
     * 
     * @param context
     */
    public void setContext(de.zib.gndms.dspace.slice.stubs.TransformSliceToRequestContext context) {
        this.context = context;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TransformSliceToRequest)) return false;
        TransformSliceToRequest other = (TransformSliceToRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sliceTransformSpecifier==null && other.getSliceTransformSpecifier()==null) || 
             (this.sliceTransformSpecifier!=null &&
              this.sliceTransformSpecifier.equals(other.getSliceTransformSpecifier()))) &&
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
        if (getSliceTransformSpecifier() != null) {
            _hashCode += getSliceTransformSpecifier().hashCode();
        }
        if (getContext() != null) {
            _hashCode += getContext().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TransformSliceToRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Slice", ">TransformSliceToRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sliceTransformSpecifier");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Slice", "sliceTransformSpecifier"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Slice", ">>TransformSliceToRequest>sliceTransformSpecifier"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("context");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Slice", "context"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Slice", ">>TransformSliceToRequest>context"));
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
