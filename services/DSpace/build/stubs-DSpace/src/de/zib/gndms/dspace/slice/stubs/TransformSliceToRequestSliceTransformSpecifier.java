/**
 * TransformSliceToRequestSliceTransformSpecifier.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.slice.stubs;

public class TransformSliceToRequestSliceTransformSpecifier  implements java.io.Serializable {
    private types.SliceTransformSpecifierT sliceTransformSpecifier;

    public TransformSliceToRequestSliceTransformSpecifier() {
    }

    public TransformSliceToRequestSliceTransformSpecifier(
           types.SliceTransformSpecifierT sliceTransformSpecifier) {
           this.sliceTransformSpecifier = sliceTransformSpecifier;
    }


    /**
     * Gets the sliceTransformSpecifier value for this TransformSliceToRequestSliceTransformSpecifier.
     * 
     * @return sliceTransformSpecifier
     */
    public types.SliceTransformSpecifierT getSliceTransformSpecifier() {
        return sliceTransformSpecifier;
    }


    /**
     * Sets the sliceTransformSpecifier value for this TransformSliceToRequestSliceTransformSpecifier.
     * 
     * @param sliceTransformSpecifier
     */
    public void setSliceTransformSpecifier(types.SliceTransformSpecifierT sliceTransformSpecifier) {
        this.sliceTransformSpecifier = sliceTransformSpecifier;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TransformSliceToRequestSliceTransformSpecifier)) return false;
        TransformSliceToRequestSliceTransformSpecifier other = (TransformSliceToRequestSliceTransformSpecifier) obj;
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
              this.sliceTransformSpecifier.equals(other.getSliceTransformSpecifier())));
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TransformSliceToRequestSliceTransformSpecifier.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Slice", ">>TransformSliceToRequest>sliceTransformSpecifier"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sliceTransformSpecifier");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "SliceTransformSpecifier"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "SliceTransformSpecifierT"));
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
