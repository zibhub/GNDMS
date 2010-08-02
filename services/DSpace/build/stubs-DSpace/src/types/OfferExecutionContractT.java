/**
 * OfferExecutionContractT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package types;

public class OfferExecutionContractT  implements java.io.Serializable {
    private java.util.Calendar ifDecisionBefore;
    private types.FutureTimeT executionLikelyUntil;
    private types.FutureTimeT resultValidUntil;
    private java.lang.Long estMaxSize;
    private types.ContextT volatileRequestInfo;

    public OfferExecutionContractT() {
    }

    public OfferExecutionContractT(
           java.lang.Long estMaxSize,
           types.FutureTimeT executionLikelyUntil,
           java.util.Calendar ifDecisionBefore,
           types.FutureTimeT resultValidUntil,
           types.ContextT volatileRequestInfo) {
           this.ifDecisionBefore = ifDecisionBefore;
           this.executionLikelyUntil = executionLikelyUntil;
           this.resultValidUntil = resultValidUntil;
           this.estMaxSize = estMaxSize;
           this.volatileRequestInfo = volatileRequestInfo;
    }


    /**
     * Gets the ifDecisionBefore value for this OfferExecutionContractT.
     * 
     * @return ifDecisionBefore
     */
    public java.util.Calendar getIfDecisionBefore() {
        return ifDecisionBefore;
    }


    /**
     * Sets the ifDecisionBefore value for this OfferExecutionContractT.
     * 
     * @param ifDecisionBefore
     */
    public void setIfDecisionBefore(java.util.Calendar ifDecisionBefore) {
        this.ifDecisionBefore = ifDecisionBefore;
    }


    /**
     * Gets the executionLikelyUntil value for this OfferExecutionContractT.
     * 
     * @return executionLikelyUntil
     */
    public types.FutureTimeT getExecutionLikelyUntil() {
        return executionLikelyUntil;
    }


    /**
     * Sets the executionLikelyUntil value for this OfferExecutionContractT.
     * 
     * @param executionLikelyUntil
     */
    public void setExecutionLikelyUntil(types.FutureTimeT executionLikelyUntil) {
        this.executionLikelyUntil = executionLikelyUntil;
    }


    /**
     * Gets the resultValidUntil value for this OfferExecutionContractT.
     * 
     * @return resultValidUntil
     */
    public types.FutureTimeT getResultValidUntil() {
        return resultValidUntil;
    }


    /**
     * Sets the resultValidUntil value for this OfferExecutionContractT.
     * 
     * @param resultValidUntil
     */
    public void setResultValidUntil(types.FutureTimeT resultValidUntil) {
        this.resultValidUntil = resultValidUntil;
    }


    /**
     * Gets the estMaxSize value for this OfferExecutionContractT.
     * 
     * @return estMaxSize
     */
    public java.lang.Long getEstMaxSize() {
        return estMaxSize;
    }


    /**
     * Sets the estMaxSize value for this OfferExecutionContractT.
     * 
     * @param estMaxSize
     */
    public void setEstMaxSize(java.lang.Long estMaxSize) {
        this.estMaxSize = estMaxSize;
    }


    /**
     * Gets the volatileRequestInfo value for this OfferExecutionContractT.
     * 
     * @return volatileRequestInfo
     */
    public types.ContextT getVolatileRequestInfo() {
        return volatileRequestInfo;
    }


    /**
     * Sets the volatileRequestInfo value for this OfferExecutionContractT.
     * 
     * @param volatileRequestInfo
     */
    public void setVolatileRequestInfo(types.ContextT volatileRequestInfo) {
        this.volatileRequestInfo = volatileRequestInfo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OfferExecutionContractT)) return false;
        OfferExecutionContractT other = (OfferExecutionContractT) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ifDecisionBefore==null && other.getIfDecisionBefore()==null) || 
             (this.ifDecisionBefore!=null &&
              this.ifDecisionBefore.equals(other.getIfDecisionBefore()))) &&
            ((this.executionLikelyUntil==null && other.getExecutionLikelyUntil()==null) || 
             (this.executionLikelyUntil!=null &&
              this.executionLikelyUntil.equals(other.getExecutionLikelyUntil()))) &&
            ((this.resultValidUntil==null && other.getResultValidUntil()==null) || 
             (this.resultValidUntil!=null &&
              this.resultValidUntil.equals(other.getResultValidUntil()))) &&
            ((this.estMaxSize==null && other.getEstMaxSize()==null) || 
             (this.estMaxSize!=null &&
              this.estMaxSize.equals(other.getEstMaxSize()))) &&
            ((this.volatileRequestInfo==null && other.getVolatileRequestInfo()==null) || 
             (this.volatileRequestInfo!=null &&
              this.volatileRequestInfo.equals(other.getVolatileRequestInfo())));
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
        if (getIfDecisionBefore() != null) {
            _hashCode += getIfDecisionBefore().hashCode();
        }
        if (getExecutionLikelyUntil() != null) {
            _hashCode += getExecutionLikelyUntil().hashCode();
        }
        if (getResultValidUntil() != null) {
            _hashCode += getResultValidUntil().hashCode();
        }
        if (getEstMaxSize() != null) {
            _hashCode += getEstMaxSize().hashCode();
        }
        if (getVolatileRequestInfo() != null) {
            _hashCode += getVolatileRequestInfo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OfferExecutionContractT.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "OfferExecutionContractT"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ifDecisionBefore");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "IfDecisionBefore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("executionLikelyUntil");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "ExecutionLikelyUntil"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "FutureTimeT"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultValidUntil");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "ResultValidUntil"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "FutureTimeT"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estMaxSize");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "EstMaxSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("volatileRequestInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "VolatileRequestInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "ContextT"));
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
