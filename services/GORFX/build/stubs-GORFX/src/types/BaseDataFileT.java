/**
 * BaseDataFileT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package types;

public class BaseDataFileT  implements java.io.Serializable {
    private boolean pinned;
    private org.apache.axis.types.URI file;

    public BaseDataFileT() {
    }

    public BaseDataFileT(
           org.apache.axis.types.URI file,
           boolean pinned) {
           this.pinned = pinned;
           this.file = file;
    }


    /**
     * Gets the pinned value for this BaseDataFileT.
     * 
     * @return pinned
     */
    public boolean isPinned() {
        return pinned;
    }


    /**
     * Sets the pinned value for this BaseDataFileT.
     * 
     * @param pinned
     */
    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }


    /**
     * Gets the file value for this BaseDataFileT.
     * 
     * @return file
     */
    public org.apache.axis.types.URI getFile() {
        return file;
    }


    /**
     * Sets the file value for this BaseDataFileT.
     * 
     * @param file
     */
    public void setFile(org.apache.axis.types.URI file) {
        this.file = file;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BaseDataFileT)) return false;
        BaseDataFileT other = (BaseDataFileT) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.pinned == other.isPinned() &&
            ((this.file==null && other.getFile()==null) || 
             (this.file!=null &&
              this.file.equals(other.getFile())));
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
        _hashCode += (isPinned() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getFile() != null) {
            _hashCode += getFile().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BaseDataFileT.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "BaseDataFileT"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pinned");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "Pinned"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("file");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "File"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
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
