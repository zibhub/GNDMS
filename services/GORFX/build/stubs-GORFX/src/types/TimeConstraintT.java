/**
 * TimeConstraintT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package types;

public class TimeConstraintT  implements java.io.Serializable {
    private java.lang.String minTime;
    private java.lang.String maxTime;

    public TimeConstraintT() {
    }

    public TimeConstraintT(
           java.lang.String maxTime,
           java.lang.String minTime) {
           this.minTime = minTime;
           this.maxTime = maxTime;
    }


    /**
     * Gets the minTime value for this TimeConstraintT.
     * 
     * @return minTime
     */
    public java.lang.String getMinTime() {
        return minTime;
    }


    /**
     * Sets the minTime value for this TimeConstraintT.
     * 
     * @param minTime
     */
    public void setMinTime(java.lang.String minTime) {
        this.minTime = minTime;
    }


    /**
     * Gets the maxTime value for this TimeConstraintT.
     * 
     * @return maxTime
     */
    public java.lang.String getMaxTime() {
        return maxTime;
    }


    /**
     * Sets the maxTime value for this TimeConstraintT.
     * 
     * @param maxTime
     */
    public void setMaxTime(java.lang.String maxTime) {
        this.maxTime = maxTime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TimeConstraintT)) return false;
        TimeConstraintT other = (TimeConstraintT) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.minTime==null && other.getMinTime()==null) || 
             (this.minTime!=null &&
              this.minTime.equals(other.getMinTime()))) &&
            ((this.maxTime==null && other.getMaxTime()==null) || 
             (this.maxTime!=null &&
              this.maxTime.equals(other.getMaxTime())));
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
        if (getMinTime() != null) {
            _hashCode += getMinTime().hashCode();
        }
        if (getMaxTime() != null) {
            _hashCode += getMaxTime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TimeConstraintT.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "TimeConstraintT"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "MinTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "MaxTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
