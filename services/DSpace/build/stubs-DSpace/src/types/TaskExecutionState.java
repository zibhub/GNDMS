/**
 * TaskExecutionState.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package types;

public class TaskExecutionState  implements java.io.Serializable {
    private types.TaskStatusT status;
    private org.apache.axis.types.PositiveInteger progress;
    private org.apache.axis.types.PositiveInteger maxProgress;
    private org.apache.axis.types.NormalizedString description;
    private boolean contractBroken;

    public TaskExecutionState() {
    }

    public TaskExecutionState(
           boolean contractBroken,
           org.apache.axis.types.NormalizedString description,
           org.apache.axis.types.PositiveInteger maxProgress,
           org.apache.axis.types.PositiveInteger progress,
           types.TaskStatusT status) {
           this.status = status;
           this.progress = progress;
           this.maxProgress = maxProgress;
           this.description = description;
           this.contractBroken = contractBroken;
    }


    /**
     * Gets the status value for this TaskExecutionState.
     * 
     * @return status
     */
    public types.TaskStatusT getStatus() {
        return status;
    }


    /**
     * Sets the status value for this TaskExecutionState.
     * 
     * @param status
     */
    public void setStatus(types.TaskStatusT status) {
        this.status = status;
    }


    /**
     * Gets the progress value for this TaskExecutionState.
     * 
     * @return progress
     */
    public org.apache.axis.types.PositiveInteger getProgress() {
        return progress;
    }


    /**
     * Sets the progress value for this TaskExecutionState.
     * 
     * @param progress
     */
    public void setProgress(org.apache.axis.types.PositiveInteger progress) {
        this.progress = progress;
    }


    /**
     * Gets the maxProgress value for this TaskExecutionState.
     * 
     * @return maxProgress
     */
    public org.apache.axis.types.PositiveInteger getMaxProgress() {
        return maxProgress;
    }


    /**
     * Sets the maxProgress value for this TaskExecutionState.
     * 
     * @param maxProgress
     */
    public void setMaxProgress(org.apache.axis.types.PositiveInteger maxProgress) {
        this.maxProgress = maxProgress;
    }


    /**
     * Gets the description value for this TaskExecutionState.
     * 
     * @return description
     */
    public org.apache.axis.types.NormalizedString getDescription() {
        return description;
    }


    /**
     * Sets the description value for this TaskExecutionState.
     * 
     * @param description
     */
    public void setDescription(org.apache.axis.types.NormalizedString description) {
        this.description = description;
    }


    /**
     * Gets the contractBroken value for this TaskExecutionState.
     * 
     * @return contractBroken
     */
    public boolean isContractBroken() {
        return contractBroken;
    }


    /**
     * Sets the contractBroken value for this TaskExecutionState.
     * 
     * @param contractBroken
     */
    public void setContractBroken(boolean contractBroken) {
        this.contractBroken = contractBroken;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TaskExecutionState)) return false;
        TaskExecutionState other = (TaskExecutionState) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.progress==null && other.getProgress()==null) || 
             (this.progress!=null &&
              this.progress.equals(other.getProgress()))) &&
            ((this.maxProgress==null && other.getMaxProgress()==null) || 
             (this.maxProgress!=null &&
              this.maxProgress.equals(other.getMaxProgress()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            this.contractBroken == other.isContractBroken();
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
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getProgress() != null) {
            _hashCode += getProgress().hashCode();
        }
        if (getMaxProgress() != null) {
            _hashCode += getMaxProgress().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        _hashCode += (isContractBroken() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TaskExecutionState.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", ">TaskExecutionState"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "TaskStatusT"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("progress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "Progress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "positiveInteger"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxProgress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "MaxProgress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "positiveInteger"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "Description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "normalizedString"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contractBroken");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "ContractBroken"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
