/**
 * StorageSizeT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package types;

public class StorageSizeT  implements java.io.Serializable {
    private org.apache.axis.types.PositiveInteger storageSizeValue;
    private java.lang.String storageSizeUnit;

    public StorageSizeT() {
    }

    public StorageSizeT(
           java.lang.String storageSizeUnit,
           org.apache.axis.types.PositiveInteger storageSizeValue) {
           this.storageSizeValue = storageSizeValue;
           this.storageSizeUnit = storageSizeUnit;
    }


    /**
     * Gets the storageSizeValue value for this StorageSizeT.
     * 
     * @return storageSizeValue
     */
    public org.apache.axis.types.PositiveInteger getStorageSizeValue() {
        return storageSizeValue;
    }


    /**
     * Sets the storageSizeValue value for this StorageSizeT.
     * 
     * @param storageSizeValue
     */
    public void setStorageSizeValue(org.apache.axis.types.PositiveInteger storageSizeValue) {
        this.storageSizeValue = storageSizeValue;
    }


    /**
     * Gets the storageSizeUnit value for this StorageSizeT.
     * 
     * @return storageSizeUnit
     */
    public java.lang.String getStorageSizeUnit() {
        return storageSizeUnit;
    }


    /**
     * Sets the storageSizeUnit value for this StorageSizeT.
     * 
     * @param storageSizeUnit
     */
    public void setStorageSizeUnit(java.lang.String storageSizeUnit) {
        this.storageSizeUnit = storageSizeUnit;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StorageSizeT)) return false;
        StorageSizeT other = (StorageSizeT) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.storageSizeValue==null && other.getStorageSizeValue()==null) || 
             (this.storageSizeValue!=null &&
              this.storageSizeValue.equals(other.getStorageSizeValue()))) &&
            ((this.storageSizeUnit==null && other.getStorageSizeUnit()==null) || 
             (this.storageSizeUnit!=null &&
              this.storageSizeUnit.equals(other.getStorageSizeUnit())));
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
        if (getStorageSizeValue() != null) {
            _hashCode += getStorageSizeValue().hashCode();
        }
        if (getStorageSizeUnit() != null) {
            _hashCode += getStorageSizeUnit().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StorageSizeT.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "StorageSizeT"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("storageSizeValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "StorageSizeValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "positiveInteger"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("storageSizeUnit");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "StorageSizeUnit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
