/**
 * CreateSliceRequestSliceCreationSpecifier.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.subspace.stubs;

public class CreateSliceRequestSliceCreationSpecifier  implements java.io.Serializable {
    private types.SliceCreationSpecifier sliceCreationSpecifier;

    public CreateSliceRequestSliceCreationSpecifier() {
    }

    public CreateSliceRequestSliceCreationSpecifier(
           types.SliceCreationSpecifier sliceCreationSpecifier) {
           this.sliceCreationSpecifier = sliceCreationSpecifier;
    }


    /**
     * Gets the sliceCreationSpecifier value for this CreateSliceRequestSliceCreationSpecifier.
     * 
     * @return sliceCreationSpecifier
     */
    public types.SliceCreationSpecifier getSliceCreationSpecifier() {
        return sliceCreationSpecifier;
    }


    /**
     * Sets the sliceCreationSpecifier value for this CreateSliceRequestSliceCreationSpecifier.
     * 
     * @param sliceCreationSpecifier
     */
    public void setSliceCreationSpecifier(types.SliceCreationSpecifier sliceCreationSpecifier) {
        this.sliceCreationSpecifier = sliceCreationSpecifier;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateSliceRequestSliceCreationSpecifier)) return false;
        CreateSliceRequestSliceCreationSpecifier other = (CreateSliceRequestSliceCreationSpecifier) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sliceCreationSpecifier==null && other.getSliceCreationSpecifier()==null) || 
             (this.sliceCreationSpecifier!=null &&
              this.sliceCreationSpecifier.equals(other.getSliceCreationSpecifier())));
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
        if (getSliceCreationSpecifier() != null) {
            _hashCode += getSliceCreationSpecifier().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateSliceRequestSliceCreationSpecifier.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace", ">>CreateSliceRequest>sliceCreationSpecifier"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sliceCreationSpecifier");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "SliceCreationSpecifier"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", ">SliceCreationSpecifier"));
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
