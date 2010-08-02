/**
 * ReplicaSliceFilterT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package types;

public class ReplicaSliceFilterT  implements java.io.Serializable {
    private types.FileSeqT lofiSeq;
    private boolean completeSlicesOnly;
    private org.apache.axis.types.PositiveInteger resultLimit;

    public ReplicaSliceFilterT() {
    }

    public ReplicaSliceFilterT(
           boolean completeSlicesOnly,
           types.FileSeqT lofiSeq,
           org.apache.axis.types.PositiveInteger resultLimit) {
           this.lofiSeq = lofiSeq;
           this.completeSlicesOnly = completeSlicesOnly;
           this.resultLimit = resultLimit;
    }


    /**
     * Gets the lofiSeq value for this ReplicaSliceFilterT.
     * 
     * @return lofiSeq
     */
    public types.FileSeqT getLofiSeq() {
        return lofiSeq;
    }


    /**
     * Sets the lofiSeq value for this ReplicaSliceFilterT.
     * 
     * @param lofiSeq
     */
    public void setLofiSeq(types.FileSeqT lofiSeq) {
        this.lofiSeq = lofiSeq;
    }


    /**
     * Gets the completeSlicesOnly value for this ReplicaSliceFilterT.
     * 
     * @return completeSlicesOnly
     */
    public boolean isCompleteSlicesOnly() {
        return completeSlicesOnly;
    }


    /**
     * Sets the completeSlicesOnly value for this ReplicaSliceFilterT.
     * 
     * @param completeSlicesOnly
     */
    public void setCompleteSlicesOnly(boolean completeSlicesOnly) {
        this.completeSlicesOnly = completeSlicesOnly;
    }


    /**
     * Gets the resultLimit value for this ReplicaSliceFilterT.
     * 
     * @return resultLimit
     */
    public org.apache.axis.types.PositiveInteger getResultLimit() {
        return resultLimit;
    }


    /**
     * Sets the resultLimit value for this ReplicaSliceFilterT.
     * 
     * @param resultLimit
     */
    public void setResultLimit(org.apache.axis.types.PositiveInteger resultLimit) {
        this.resultLimit = resultLimit;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReplicaSliceFilterT)) return false;
        ReplicaSliceFilterT other = (ReplicaSliceFilterT) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.lofiSeq==null && other.getLofiSeq()==null) || 
             (this.lofiSeq!=null &&
              this.lofiSeq.equals(other.getLofiSeq()))) &&
            this.completeSlicesOnly == other.isCompleteSlicesOnly() &&
            ((this.resultLimit==null && other.getResultLimit()==null) || 
             (this.resultLimit!=null &&
              this.resultLimit.equals(other.getResultLimit())));
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
        if (getLofiSeq() != null) {
            _hashCode += getLofiSeq().hashCode();
        }
        _hashCode += (isCompleteSlicesOnly() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getResultLimit() != null) {
            _hashCode += getResultLimit().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReplicaSliceFilterT.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "ReplicaSliceFilterT"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lofiSeq");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "LofiSeq"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "FileSeqT"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("completeSlicesOnly");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "CompleteSlicesOnly"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultLimit");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "ResultLimit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "positiveInteger"));
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
