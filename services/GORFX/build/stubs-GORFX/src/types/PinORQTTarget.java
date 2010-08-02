/**
 * PinORQTTarget.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package types;

public class PinORQTTarget  implements java.io.Serializable {
    private types.SliceReference sliceReference;
    private types.DSpaceReference DSpaceReference;

    public PinORQTTarget() {
    }

    public PinORQTTarget(
           types.DSpaceReference DSpaceReference,
           types.SliceReference sliceReference) {
           this.sliceReference = sliceReference;
           this.DSpaceReference = DSpaceReference;
    }


    /**
     * Gets the sliceReference value for this PinORQTTarget.
     * 
     * @return sliceReference
     */
    public types.SliceReference getSliceReference() {
        return sliceReference;
    }


    /**
     * Sets the sliceReference value for this PinORQTTarget.
     * 
     * @param sliceReference
     */
    public void setSliceReference(types.SliceReference sliceReference) {
        this.sliceReference = sliceReference;
    }


    /**
     * Gets the DSpaceReference value for this PinORQTTarget.
     * 
     * @return DSpaceReference
     */
    public types.DSpaceReference getDSpaceReference() {
        return DSpaceReference;
    }


    /**
     * Sets the DSpaceReference value for this PinORQTTarget.
     * 
     * @param DSpaceReference
     */
    public void setDSpaceReference(types.DSpaceReference DSpaceReference) {
        this.DSpaceReference = DSpaceReference;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PinORQTTarget)) return false;
        PinORQTTarget other = (PinORQTTarget) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sliceReference==null && other.getSliceReference()==null) || 
             (this.sliceReference!=null &&
              this.sliceReference.equals(other.getSliceReference()))) &&
            ((this.DSpaceReference==null && other.getDSpaceReference()==null) || 
             (this.DSpaceReference!=null &&
              this.DSpaceReference.equals(other.getDSpaceReference())));
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
        if (getSliceReference() != null) {
            _hashCode += getSliceReference().hashCode();
        }
        if (getDSpaceReference() != null) {
            _hashCode += getDSpaceReference().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PinORQTTarget.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", ">PinORQT>Target"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sliceReference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Slice/types", "SliceReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Slice/types", ">SliceReference"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("DSpaceReference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/types", "DSpaceReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/types", ">DSpaceReference"));
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
