/**
 * TaskResourceProperties.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.GORFX.context.stubs;

public class TaskResourceProperties  implements java.io.Serializable {
    private java.util.Calendar currentTime;
    private java.util.Calendar terminationTime;
    private types.TaskExecutionState taskExecutionState;
    private types.TaskExecutionFailure taskExecutionFailure;
    private types.DynamicOfferDataSeqT taskExecutionResults;

    public TaskResourceProperties() {
    }

    public TaskResourceProperties(
           java.util.Calendar currentTime,
           types.TaskExecutionFailure taskExecutionFailure,
           types.DynamicOfferDataSeqT taskExecutionResults,
           types.TaskExecutionState taskExecutionState,
           java.util.Calendar terminationTime) {
           this.currentTime = currentTime;
           this.terminationTime = terminationTime;
           this.taskExecutionState = taskExecutionState;
           this.taskExecutionFailure = taskExecutionFailure;
           this.taskExecutionResults = taskExecutionResults;
    }


    /**
     * Gets the currentTime value for this TaskResourceProperties.
     * 
     * @return currentTime
     */
    public java.util.Calendar getCurrentTime() {
        return currentTime;
    }


    /**
     * Sets the currentTime value for this TaskResourceProperties.
     * 
     * @param currentTime
     */
    public void setCurrentTime(java.util.Calendar currentTime) {
        this.currentTime = currentTime;
    }


    /**
     * Gets the terminationTime value for this TaskResourceProperties.
     * 
     * @return terminationTime
     */
    public java.util.Calendar getTerminationTime() {
        return terminationTime;
    }


    /**
     * Sets the terminationTime value for this TaskResourceProperties.
     * 
     * @param terminationTime
     */
    public void setTerminationTime(java.util.Calendar terminationTime) {
        this.terminationTime = terminationTime;
    }


    /**
     * Gets the taskExecutionState value for this TaskResourceProperties.
     * 
     * @return taskExecutionState
     */
    public types.TaskExecutionState getTaskExecutionState() {
        return taskExecutionState;
    }


    /**
     * Sets the taskExecutionState value for this TaskResourceProperties.
     * 
     * @param taskExecutionState
     */
    public void setTaskExecutionState(types.TaskExecutionState taskExecutionState) {
        this.taskExecutionState = taskExecutionState;
    }


    /**
     * Gets the taskExecutionFailure value for this TaskResourceProperties.
     * 
     * @return taskExecutionFailure
     */
    public types.TaskExecutionFailure getTaskExecutionFailure() {
        return taskExecutionFailure;
    }


    /**
     * Sets the taskExecutionFailure value for this TaskResourceProperties.
     * 
     * @param taskExecutionFailure
     */
    public void setTaskExecutionFailure(types.TaskExecutionFailure taskExecutionFailure) {
        this.taskExecutionFailure = taskExecutionFailure;
    }


    /**
     * Gets the taskExecutionResults value for this TaskResourceProperties.
     * 
     * @return taskExecutionResults
     */
    public types.DynamicOfferDataSeqT getTaskExecutionResults() {
        return taskExecutionResults;
    }


    /**
     * Sets the taskExecutionResults value for this TaskResourceProperties.
     * 
     * @param taskExecutionResults
     */
    public void setTaskExecutionResults(types.DynamicOfferDataSeqT taskExecutionResults) {
        this.taskExecutionResults = taskExecutionResults;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TaskResourceProperties)) return false;
        TaskResourceProperties other = (TaskResourceProperties) obj;
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
            ((this.taskExecutionState==null && other.getTaskExecutionState()==null) || 
             (this.taskExecutionState!=null &&
              this.taskExecutionState.equals(other.getTaskExecutionState()))) &&
            ((this.taskExecutionFailure==null && other.getTaskExecutionFailure()==null) || 
             (this.taskExecutionFailure!=null &&
              this.taskExecutionFailure.equals(other.getTaskExecutionFailure()))) &&
            ((this.taskExecutionResults==null && other.getTaskExecutionResults()==null) || 
             (this.taskExecutionResults!=null &&
              this.taskExecutionResults.equals(other.getTaskExecutionResults())));
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
        if (getTaskExecutionState() != null) {
            _hashCode += getTaskExecutionState().hashCode();
        }
        if (getTaskExecutionFailure() != null) {
            _hashCode += getTaskExecutionFailure().hashCode();
        }
        if (getTaskExecutionResults() != null) {
            _hashCode += getTaskExecutionResults().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TaskResourceProperties.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://GORFX.gndms.zib.de/GORFX/Context", ">TaskResourceProperties"));
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
        elemField.setFieldName("taskExecutionState");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "TaskExecutionState"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", ">TaskExecutionState"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taskExecutionFailure");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "TaskExecutionFailure"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", ">TaskExecutionFailure"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taskExecutionResults");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "TaskExecutionResults"));
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
