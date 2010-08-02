/**
 * FileMapEntryT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package types;

public class FileMapEntryT  implements java.io.Serializable {
    private org.apache.axis.types.NormalizedString sourceFile;
    private org.apache.axis.types.NormalizedString destinationFile;

    public FileMapEntryT() {
    }

    public FileMapEntryT(
           org.apache.axis.types.NormalizedString destinationFile,
           org.apache.axis.types.NormalizedString sourceFile) {
           this.sourceFile = sourceFile;
           this.destinationFile = destinationFile;
    }


    /**
     * Gets the sourceFile value for this FileMapEntryT.
     * 
     * @return sourceFile
     */
    public org.apache.axis.types.NormalizedString getSourceFile() {
        return sourceFile;
    }


    /**
     * Sets the sourceFile value for this FileMapEntryT.
     * 
     * @param sourceFile
     */
    public void setSourceFile(org.apache.axis.types.NormalizedString sourceFile) {
        this.sourceFile = sourceFile;
    }


    /**
     * Gets the destinationFile value for this FileMapEntryT.
     * 
     * @return destinationFile
     */
    public org.apache.axis.types.NormalizedString getDestinationFile() {
        return destinationFile;
    }


    /**
     * Sets the destinationFile value for this FileMapEntryT.
     * 
     * @param destinationFile
     */
    public void setDestinationFile(org.apache.axis.types.NormalizedString destinationFile) {
        this.destinationFile = destinationFile;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FileMapEntryT)) return false;
        FileMapEntryT other = (FileMapEntryT) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sourceFile==null && other.getSourceFile()==null) || 
             (this.sourceFile!=null &&
              this.sourceFile.equals(other.getSourceFile()))) &&
            ((this.destinationFile==null && other.getDestinationFile()==null) || 
             (this.destinationFile!=null &&
              this.destinationFile.equals(other.getDestinationFile())));
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
        if (getSourceFile() != null) {
            _hashCode += getSourceFile().hashCode();
        }
        if (getDestinationFile() != null) {
            _hashCode += getDestinationFile().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FileMapEntryT.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "FileMapEntryT"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceFile");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "SourceFile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "normalizedString"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinationFile");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "DestinationFile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "normalizedString"));
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
