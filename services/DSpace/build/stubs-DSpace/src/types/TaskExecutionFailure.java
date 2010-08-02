/**
 * TaskExecutionFailure.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package types;

public class TaskExecutionFailure  implements java.io.Serializable {
    private org.oasis.wsrf.faults.BaseFaultType baseFault;
    private types.TaskExecutionFailureImplementationFault implementationFault;
    private java.lang.Object allIsFine;

    public TaskExecutionFailure() {
    }

    public TaskExecutionFailure(
           java.lang.Object allIsFine,
           org.oasis.wsrf.faults.BaseFaultType baseFault,
           types.TaskExecutionFailureImplementationFault implementationFault) {
           this.baseFault = baseFault;
           this.implementationFault = implementationFault;
           this.allIsFine = allIsFine;
    }


    /**
     * Gets the baseFault value for this TaskExecutionFailure.
     * 
     * @return baseFault
     */
    public org.oasis.wsrf.faults.BaseFaultType getBaseFault() {
        return baseFault;
    }


    /**
     * Sets the baseFault value for this TaskExecutionFailure.
     * 
     * @param baseFault
     */
    public void setBaseFault(org.oasis.wsrf.faults.BaseFaultType baseFault) {
        this.baseFault = baseFault;
    }


    /**
     * Gets the implementationFault value for this TaskExecutionFailure.
     * 
     * @return implementationFault
     */
    public types.TaskExecutionFailureImplementationFault getImplementationFault() {
        return implementationFault;
    }


    /**
     * Sets the implementationFault value for this TaskExecutionFailure.
     * 
     * @param implementationFault
     */
    public void setImplementationFault(types.TaskExecutionFailureImplementationFault implementationFault) {
        this.implementationFault = implementationFault;
    }


    /**
     * Gets the allIsFine value for this TaskExecutionFailure.
     * 
     * @return allIsFine
     */
    public java.lang.Object getAllIsFine() {
        return allIsFine;
    }


    /**
     * Sets the allIsFine value for this TaskExecutionFailure.
     * 
     * @param allIsFine
     */
    public void setAllIsFine(java.lang.Object allIsFine) {
        this.allIsFine = allIsFine;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TaskExecutionFailure)) return false;
        TaskExecutionFailure other = (TaskExecutionFailure) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.baseFault==null && other.getBaseFault()==null) || 
             (this.baseFault!=null &&
              this.baseFault.equals(other.getBaseFault()))) &&
            ((this.implementationFault==null && other.getImplementationFault()==null) || 
             (this.implementationFault!=null &&
              this.implementationFault.equals(other.getImplementationFault()))) &&
            ((this.allIsFine==null && other.getAllIsFine()==null) || 
             (this.allIsFine!=null &&
              this.allIsFine.equals(other.getAllIsFine())));
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
        if (getBaseFault() != null) {
            _hashCode += getBaseFault().hashCode();
        }
        if (getImplementationFault() != null) {
            _hashCode += getImplementationFault().hashCode();
        }
        if (getAllIsFine() != null) {
            _hashCode += getAllIsFine().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TaskExecutionFailure.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", ">TaskExecutionFailure"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("baseFault");
        elemField.setXmlName(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-BaseFaults-1.2-draft-01.xsd", "BaseFault"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-BaseFaults-1.2-draft-01.xsd", "BaseFaultType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("implementationFault");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "ImplementationFault"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", ">>TaskExecutionFailure>ImplementationFault"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("allIsFine");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "AllIsFine"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
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
