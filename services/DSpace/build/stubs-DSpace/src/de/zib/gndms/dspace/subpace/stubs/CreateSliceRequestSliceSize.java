/**
 * CreateSliceRequestSliceSize.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.subpace.stubs;

public class CreateSliceRequestSliceSize  implements java.io.Serializable {
    private types.StorageSizeT totalStorageSize;

    public CreateSliceRequestSliceSize() {
    }

    public CreateSliceRequestSliceSize(
           types.StorageSizeT totalStorageSize) {
           this.totalStorageSize = totalStorageSize;
    }


    /**
     * Gets the totalStorageSize value for this CreateSliceRequestSliceSize.
     * 
     * @return totalStorageSize
     */
    public types.StorageSizeT getTotalStorageSize() {
        return totalStorageSize;
    }


    /**
     * Sets the totalStorageSize value for this CreateSliceRequestSliceSize.
     * 
     * @param totalStorageSize
     */
    public void setTotalStorageSize(types.StorageSizeT totalStorageSize) {
        this.totalStorageSize = totalStorageSize;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateSliceRequestSliceSize)) return false;
        CreateSliceRequestSliceSize other = (CreateSliceRequestSliceSize) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.totalStorageSize==null && other.getTotalStorageSize()==null) || 
             (this.totalStorageSize!=null &&
              this.totalStorageSize.equals(other.getTotalStorageSize())));
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
        if (getTotalStorageSize() != null) {
            _hashCode += getTotalStorageSize().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateSliceRequestSliceSize.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace", ">>CreateSliceRequest>sliceSize"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalStorageSize");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "TotalStorageSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "StorageSizeT"));
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
