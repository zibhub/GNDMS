/**
 * OfferResourceProperties.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.GORFX.offer.stubs;

public class OfferResourceProperties  implements java.io.Serializable {
    private java.util.Calendar currentTime;
    private java.util.Calendar terminationTime;
    private types.OfferExecutionContractT offerExecutionContract;
    private types.DynamicOfferDataSeqT offerRequestArguments;

    public OfferResourceProperties() {
    }

    public OfferResourceProperties(
           java.util.Calendar currentTime,
           types.OfferExecutionContractT offerExecutionContract,
           types.DynamicOfferDataSeqT offerRequestArguments,
           java.util.Calendar terminationTime) {
           this.currentTime = currentTime;
           this.terminationTime = terminationTime;
           this.offerExecutionContract = offerExecutionContract;
           this.offerRequestArguments = offerRequestArguments;
    }


    /**
     * Gets the currentTime value for this OfferResourceProperties.
     * 
     * @return currentTime
     */
    public java.util.Calendar getCurrentTime() {
        return currentTime;
    }


    /**
     * Sets the currentTime value for this OfferResourceProperties.
     * 
     * @param currentTime
     */
    public void setCurrentTime(java.util.Calendar currentTime) {
        this.currentTime = currentTime;
    }


    /**
     * Gets the terminationTime value for this OfferResourceProperties.
     * 
     * @return terminationTime
     */
    public java.util.Calendar getTerminationTime() {
        return terminationTime;
    }


    /**
     * Sets the terminationTime value for this OfferResourceProperties.
     * 
     * @param terminationTime
     */
    public void setTerminationTime(java.util.Calendar terminationTime) {
        this.terminationTime = terminationTime;
    }


    /**
     * Gets the offerExecutionContract value for this OfferResourceProperties.
     * 
     * @return offerExecutionContract
     */
    public types.OfferExecutionContractT getOfferExecutionContract() {
        return offerExecutionContract;
    }


    /**
     * Sets the offerExecutionContract value for this OfferResourceProperties.
     * 
     * @param offerExecutionContract
     */
    public void setOfferExecutionContract(types.OfferExecutionContractT offerExecutionContract) {
        this.offerExecutionContract = offerExecutionContract;
    }


    /**
     * Gets the offerRequestArguments value for this OfferResourceProperties.
     * 
     * @return offerRequestArguments
     */
    public types.DynamicOfferDataSeqT getOfferRequestArguments() {
        return offerRequestArguments;
    }


    /**
     * Sets the offerRequestArguments value for this OfferResourceProperties.
     * 
     * @param offerRequestArguments
     */
    public void setOfferRequestArguments(types.DynamicOfferDataSeqT offerRequestArguments) {
        this.offerRequestArguments = offerRequestArguments;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OfferResourceProperties)) return false;
        OfferResourceProperties other = (OfferResourceProperties) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.currentTime==null && other.getCurrentTime()==null) || 
             (this.currentTime!=null &&
              this.currentTime.equals(other.getCurrentTime()))) &&
            ((this.terminationTime==null && other.getTerminationTime()==null) || 
             (this.terminationTime!=null &&
              this.terminationTime.equals(other.getTerminationTime()))) &&
            ((this.offerExecutionContract==null && other.getOfferExecutionContract()==null) || 
             (this.offerExecutionContract!=null &&
              this.offerExecutionContract.equals(other.getOfferExecutionContract()))) &&
            ((this.offerRequestArguments==null && other.getOfferRequestArguments()==null) || 
             (this.offerRequestArguments!=null &&
              this.offerRequestArguments.equals(other.getOfferRequestArguments())));
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
        if (getCurrentTime() != null) {
            _hashCode += getCurrentTime().hashCode();
        }
        if (getTerminationTime() != null) {
            _hashCode += getTerminationTime().hashCode();
        }
        if (getOfferExecutionContract() != null) {
            _hashCode += getOfferExecutionContract().hashCode();
        }
        if (getOfferRequestArguments() != null) {
            _hashCode += getOfferRequestArguments().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OfferResourceProperties.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://GORFX.gndms.zib.de/GORFX/Offer", ">OfferResourceProperties"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("currentTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "CurrentTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("terminationTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "TerminationTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("offerExecutionContract");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "OfferExecutionContract"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "OfferExecutionContractT"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("offerRequestArguments");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "OfferRequestArguments"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "DynamicOfferDataSeqT"));
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
