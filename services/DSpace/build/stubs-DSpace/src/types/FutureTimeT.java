/**
 * FutureTimeT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package types;

public class FutureTimeT  implements java.io.Serializable {
    private java.util.Calendar time;
    private java.lang.Long offset;

    public FutureTimeT() {
    }

    public FutureTimeT(
           java.lang.Long offset,
           java.util.Calendar time) {
           this.time = time;
           this.offset = offset;
    }


    /**
     * Gets the time value for this FutureTimeT.
     * 
     * @return time
     */
    public java.util.Calendar getTime() {
        return time;
    }


    /**
     * Sets the time value for this FutureTimeT.
     * 
     * @param time
     */
    public void setTime(java.util.Calendar time) {
        this.time = time;
    }


    /**
     * Gets the offset value for this FutureTimeT.
     * 
     * @return offset
     */
    public java.lang.Long getOffset() {
        return offset;
    }


    /**
     * Sets the offset value for this FutureTimeT.
     * 
     * @param offset
     */
    public void setOffset(java.lang.Long offset) {
        this.offset = offset;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FutureTimeT)) return false;
        FutureTimeT other = (FutureTimeT) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.time==null && other.getTime()==null) || 
             (this.time!=null &&
              this.time.equals(other.getTime()))) &&
            ((this.offset==null && other.getOffset()==null) || 
             (this.offset!=null &&
              this.offset.equals(other.getOffset())));
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
        if (getTime() != null) {
            _hashCode += getTime().hashCode();
        }
        if (getOffset() != null) {
            _hashCode += getOffset().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FutureTimeT.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "FutureTimeT"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("time");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "Time"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("offset");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "Offset"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
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
