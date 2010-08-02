/**
 * NameValEntryT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package types;

public class NameValEntryT  implements java.io.Serializable {
    private java.lang.String PName;
    private java.lang.String PVal;

    public NameValEntryT() {
    }

    public NameValEntryT(
           java.lang.String PName,
           java.lang.String PVal) {
           this.PName = PName;
           this.PVal = PVal;
    }


    /**
     * Gets the PName value for this NameValEntryT.
     * 
     * @return PName
     */
    public java.lang.String getPName() {
        return PName;
    }


    /**
     * Sets the PName value for this NameValEntryT.
     * 
     * @param PName
     */
    public void setPName(java.lang.String PName) {
        this.PName = PName;
    }


    /**
     * Gets the PVal value for this NameValEntryT.
     * 
     * @return PVal
     */
    public java.lang.String getPVal() {
        return PVal;
    }


    /**
     * Sets the PVal value for this NameValEntryT.
     * 
     * @param PVal
     */
    public void setPVal(java.lang.String PVal) {
        this.PVal = PVal;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof NameValEntryT)) return false;
        NameValEntryT other = (NameValEntryT) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.PName==null && other.getPName()==null) || 
             (this.PName!=null &&
              this.PName.equals(other.getPName()))) &&
            ((this.PVal==null && other.getPVal()==null) || 
             (this.PVal!=null &&
              this.PVal.equals(other.getPVal())));
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
        if (getPName() != null) {
            _hashCode += getPName().hashCode();
        }
        if (getPVal() != null) {
            _hashCode += getPVal().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(NameValEntryT.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "NameValEntryT"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "PName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PVal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "PVal"));
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
