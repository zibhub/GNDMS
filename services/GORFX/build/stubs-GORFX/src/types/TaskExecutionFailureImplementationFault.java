/**
 * TaskExecutionFailureImplementationFault.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package types;

public class TaskExecutionFailureImplementationFault  implements java.io.Serializable {
    private java.lang.String message;
    private java.lang.String faultClass;
    private java.lang.String faultTrace;
    private java.lang.String faultLocation;

    public TaskExecutionFailureImplementationFault() {
    }

    public TaskExecutionFailureImplementationFault(
           java.lang.String faultClass,
           java.lang.String faultLocation,
           java.lang.String faultTrace,
           java.lang.String message) {
           this.message = message;
           this.faultClass = faultClass;
           this.faultTrace = faultTrace;
           this.faultLocation = faultLocation;
    }


    /**
     * Gets the message value for this TaskExecutionFailureImplementationFault.
     * 
     * @return message
     */
    public java.lang.String getMessage() {
        return message;
    }


    /**
     * Sets the message value for this TaskExecutionFailureImplementationFault.
     * 
     * @param message
     */
    public void setMessage(java.lang.String message) {
        this.message = message;
    }


    /**
     * Gets the faultClass value for this TaskExecutionFailureImplementationFault.
     * 
     * @return faultClass
     */
    public java.lang.String getFaultClass() {
        return faultClass;
    }


    /**
     * Sets the faultClass value for this TaskExecutionFailureImplementationFault.
     * 
     * @param faultClass
     */
    public void setFaultClass(java.lang.String faultClass) {
        this.faultClass = faultClass;
    }


    /**
     * Gets the faultTrace value for this TaskExecutionFailureImplementationFault.
     * 
     * @return faultTrace
     */
    public java.lang.String getFaultTrace() {
        return faultTrace;
    }


    /**
     * Sets the faultTrace value for this TaskExecutionFailureImplementationFault.
     * 
     * @param faultTrace
     */
    public void setFaultTrace(java.lang.String faultTrace) {
        this.faultTrace = faultTrace;
    }


    /**
     * Gets the faultLocation value for this TaskExecutionFailureImplementationFault.
     * 
     * @return faultLocation
     */
    public java.lang.String getFaultLocation() {
        return faultLocation;
    }


    /**
     * Sets the faultLocation value for this TaskExecutionFailureImplementationFault.
     * 
     * @param faultLocation
     */
    public void setFaultLocation(java.lang.String faultLocation) {
        this.faultLocation = faultLocation;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TaskExecutionFailureImplementationFault)) return false;
        TaskExecutionFailureImplementationFault other = (TaskExecutionFailureImplementationFault) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.message==null && other.getMessage()==null) || 
             (this.message!=null &&
              this.message.equals(other.getMessage()))) &&
            ((this.faultClass==null && other.getFaultClass()==null) || 
             (this.faultClass!=null &&
              this.faultClass.equals(other.getFaultClass()))) &&
            ((this.faultTrace==null && other.getFaultTrace()==null) || 
             (this.faultTrace!=null &&
              this.faultTrace.equals(other.getFaultTrace()))) &&
            ((this.faultLocation==null && other.getFaultLocation()==null) || 
             (this.faultLocation!=null &&
              this.faultLocation.equals(other.getFaultLocation())));
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
        if (getMessage() != null) {
            _hashCode += getMessage().hashCode();
        }
        if (getFaultClass() != null) {
            _hashCode += getFaultClass().hashCode();
        }
        if (getFaultTrace() != null) {
            _hashCode += getFaultTrace().hashCode();
        }
        if (getFaultLocation() != null) {
            _hashCode += getFaultLocation().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TaskExecutionFailureImplementationFault.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", ">>TaskExecutionFailure>ImplementationFault"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("message");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "Message"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("faultClass");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "FaultClass"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("faultTrace");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "FaultTrace"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("faultLocation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "FaultLocation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
