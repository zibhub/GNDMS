/**
 * PermitEstimateAndDestroyRequestRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.GORFX.ORQ.stubs;

public class PermitEstimateAndDestroyRequestRequest  implements java.io.Serializable {
    private de.zib.gndms.GORFX.ORQ.stubs.PermitEstimateAndDestroyRequestRequestOfferExecutionContract offerExecutionContract;
    private de.zib.gndms.GORFX.ORQ.stubs.PermitEstimateAndDestroyRequestRequestContext context;

    public PermitEstimateAndDestroyRequestRequest() {
    }

    public PermitEstimateAndDestroyRequestRequest(
           de.zib.gndms.GORFX.ORQ.stubs.PermitEstimateAndDestroyRequestRequestContext context,
           de.zib.gndms.GORFX.ORQ.stubs.PermitEstimateAndDestroyRequestRequestOfferExecutionContract offerExecutionContract) {
           this.offerExecutionContract = offerExecutionContract;
           this.context = context;
    }


    /**
     * Gets the offerExecutionContract value for this PermitEstimateAndDestroyRequestRequest.
     * 
     * @return offerExecutionContract
     */
    public de.zib.gndms.GORFX.ORQ.stubs.PermitEstimateAndDestroyRequestRequestOfferExecutionContract getOfferExecutionContract() {
        return offerExecutionContract;
    }


    /**
     * Sets the offerExecutionContract value for this PermitEstimateAndDestroyRequestRequest.
     * 
     * @param offerExecutionContract
     */
    public void setOfferExecutionContract(de.zib.gndms.GORFX.ORQ.stubs.PermitEstimateAndDestroyRequestRequestOfferExecutionContract offerExecutionContract) {
        this.offerExecutionContract = offerExecutionContract;
    }


    /**
     * Gets the context value for this PermitEstimateAndDestroyRequestRequest.
     * 
     * @return context
     */
    public de.zib.gndms.GORFX.ORQ.stubs.PermitEstimateAndDestroyRequestRequestContext getContext() {
        return context;
    }


    /**
     * Sets the context value for this PermitEstimateAndDestroyRequestRequest.
     * 
     * @param context
     */
    public void setContext(de.zib.gndms.GORFX.ORQ.stubs.PermitEstimateAndDestroyRequestRequestContext context) {
        this.context = context;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PermitEstimateAndDestroyRequestRequest)) return false;
        PermitEstimateAndDestroyRequestRequest other = (PermitEstimateAndDestroyRequestRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.offerExecutionContract==null && other.getOfferExecutionContract()==null) || 
             (this.offerExecutionContract!=null &&
              this.offerExecutionContract.equals(other.getOfferExecutionContract()))) &&
            ((this.context==null && other.getContext()==null) || 
             (this.context!=null &&
              this.context.equals(other.getContext())));
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
        if (getOfferExecutionContract() != null) {
            _hashCode += getOfferExecutionContract().hashCode();
        }
        if (getContext() != null) {
            _hashCode += getContext().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PermitEstimateAndDestroyRequestRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://GORFX.gndms.zib.de/GORFX/ORQ", ">PermitEstimateAndDestroyRequestRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("offerExecutionContract");
        elemField.setXmlName(new javax.xml.namespace.QName("http://GORFX.gndms.zib.de/GORFX/ORQ", "offerExecutionContract"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://GORFX.gndms.zib.de/GORFX/ORQ", ">>PermitEstimateAndDestroyRequestRequest>offerExecutionContract"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("context");
        elemField.setXmlName(new javax.xml.namespace.QName("http://GORFX.gndms.zib.de/GORFX/ORQ", "context"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://GORFX.gndms.zib.de/GORFX/ORQ", ">>PermitEstimateAndDestroyRequestRequest>context"));
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
