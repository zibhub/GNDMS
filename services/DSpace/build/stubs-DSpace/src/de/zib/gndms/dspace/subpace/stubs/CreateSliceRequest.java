/**
 * CreateSliceRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.subpace.stubs;

public class CreateSliceRequest  implements java.io.Serializable {
    private de.zib.gndms.dspace.subpace.stubs.CreateSliceRequestSliceKind sliceKind;
    private de.zib.gndms.dspace.subpace.stubs.CreateSliceRequestSliceSize sliceSize;
    private de.zib.gndms.dspace.subpace.stubs.CreateSliceRequestTerminationTime terminationTime;

    public CreateSliceRequest() {
    }

    public CreateSliceRequest(
           de.zib.gndms.dspace.subpace.stubs.CreateSliceRequestSliceKind sliceKind,
           de.zib.gndms.dspace.subpace.stubs.CreateSliceRequestSliceSize sliceSize,
           de.zib.gndms.dspace.subpace.stubs.CreateSliceRequestTerminationTime terminationTime) {
           this.sliceKind = sliceKind;
           this.sliceSize = sliceSize;
           this.terminationTime = terminationTime;
    }


    /**
     * Gets the sliceKind value for this CreateSliceRequest.
     * 
     * @return sliceKind
     */
    public de.zib.gndms.dspace.subpace.stubs.CreateSliceRequestSliceKind getSliceKind() {
        return sliceKind;
    }


    /**
     * Sets the sliceKind value for this CreateSliceRequest.
     * 
     * @param sliceKind
     */
    public void setSliceKind(de.zib.gndms.dspace.subpace.stubs.CreateSliceRequestSliceKind sliceKind) {
        this.sliceKind = sliceKind;
    }


    /**
     * Gets the sliceSize value for this CreateSliceRequest.
     * 
     * @return sliceSize
     */
    public de.zib.gndms.dspace.subpace.stubs.CreateSliceRequestSliceSize getSliceSize() {
        return sliceSize;
    }


    /**
     * Sets the sliceSize value for this CreateSliceRequest.
     * 
     * @param sliceSize
     */
    public void setSliceSize(de.zib.gndms.dspace.subpace.stubs.CreateSliceRequestSliceSize sliceSize) {
        this.sliceSize = sliceSize;
    }


    /**
     * Gets the terminationTime value for this CreateSliceRequest.
     * 
     * @return terminationTime
     */
    public de.zib.gndms.dspace.subpace.stubs.CreateSliceRequestTerminationTime getTerminationTime() {
        return terminationTime;
    }


    /**
     * Sets the terminationTime value for this CreateSliceRequest.
     * 
     * @param terminationTime
     */
    public void setTerminationTime(de.zib.gndms.dspace.subpace.stubs.CreateSliceRequestTerminationTime terminationTime) {
        this.terminationTime = terminationTime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateSliceRequest)) return false;
        CreateSliceRequest other = (CreateSliceRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sliceKind==null && other.getSliceKind()==null) || 
             (this.sliceKind!=null &&
              this.sliceKind.equals(other.getSliceKind()))) &&
            ((this.sliceSize==null && other.getSliceSize()==null) || 
             (this.sliceSize!=null &&
              this.sliceSize.equals(other.getSliceSize()))) &&
            ((this.terminationTime==null && other.getTerminationTime()==null) || 
             (this.terminationTime!=null &&
              this.terminationTime.equals(other.getTerminationTime())));
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
        if (getSliceKind() != null) {
            _hashCode += getSliceKind().hashCode();
        }
        if (getSliceSize() != null) {
            _hashCode += getSliceSize().hashCode();
        }
        if (getTerminationTime() != null) {
            _hashCode += getTerminationTime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateSliceRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace", ">CreateSliceRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sliceKind");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace", "sliceKind"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace", ">>CreateSliceRequest>sliceKind"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sliceSize");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace", "sliceSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace", ">>CreateSliceRequest>sliceSize"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("terminationTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace", "terminationTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace", ">>CreateSliceRequest>terminationTime"));
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
