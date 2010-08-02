/**
 * ReplicaSliceT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package types;

public class ReplicaSliceT  implements java.io.Serializable {
    private types.FileMappingSeqT lofiMap;
    private types.SliceReference sliceReference;

    public ReplicaSliceT() {
    }

    public ReplicaSliceT(
           types.FileMappingSeqT lofiMap,
           types.SliceReference sliceReference) {
           this.lofiMap = lofiMap;
           this.sliceReference = sliceReference;
    }


    /**
     * Gets the lofiMap value for this ReplicaSliceT.
     * 
     * @return lofiMap
     */
    public types.FileMappingSeqT getLofiMap() {
        return lofiMap;
    }


    /**
     * Sets the lofiMap value for this ReplicaSliceT.
     * 
     * @param lofiMap
     */
    public void setLofiMap(types.FileMappingSeqT lofiMap) {
        this.lofiMap = lofiMap;
    }


    /**
     * Gets the sliceReference value for this ReplicaSliceT.
     * 
     * @return sliceReference
     */
    public types.SliceReference getSliceReference() {
        return sliceReference;
    }


    /**
     * Sets the sliceReference value for this ReplicaSliceT.
     * 
     * @param sliceReference
     */
    public void setSliceReference(types.SliceReference sliceReference) {
        this.sliceReference = sliceReference;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReplicaSliceT)) return false;
        ReplicaSliceT other = (ReplicaSliceT) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.lofiMap==null && other.getLofiMap()==null) || 
             (this.lofiMap!=null &&
              this.lofiMap.equals(other.getLofiMap()))) &&
            ((this.sliceReference==null && other.getSliceReference()==null) || 
             (this.sliceReference!=null &&
              this.sliceReference.equals(other.getSliceReference())));
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
        if (getLofiMap() != null) {
            _hashCode += getLofiMap().hashCode();
        }
        if (getSliceReference() != null) {
            _hashCode += getSliceReference().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReplicaSliceT.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "ReplicaSliceT"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lofiMap");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "LofiMap"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "FileMappingSeqT"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sliceReference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Slice/types", "SliceReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Slice/types", ">SliceReference"));
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
