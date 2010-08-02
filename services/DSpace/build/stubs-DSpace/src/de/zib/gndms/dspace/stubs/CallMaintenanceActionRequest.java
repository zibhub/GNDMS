/**
 * CallMaintenanceActionRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.stubs;

public class CallMaintenanceActionRequest  implements java.io.Serializable {
    private java.lang.String action;
    private de.zib.gndms.dspace.stubs.CallMaintenanceActionRequestOptions options;

    public CallMaintenanceActionRequest() {
    }

    public CallMaintenanceActionRequest(
           java.lang.String action,
           de.zib.gndms.dspace.stubs.CallMaintenanceActionRequestOptions options) {
           this.action = action;
           this.options = options;
    }


    /**
     * Gets the action value for this CallMaintenanceActionRequest.
     * 
     * @return action
     */
    public java.lang.String getAction() {
        return action;
    }


    /**
     * Sets the action value for this CallMaintenanceActionRequest.
     * 
     * @param action
     */
    public void setAction(java.lang.String action) {
        this.action = action;
    }


    /**
     * Gets the options value for this CallMaintenanceActionRequest.
     * 
     * @return options
     */
    public de.zib.gndms.dspace.stubs.CallMaintenanceActionRequestOptions getOptions() {
        return options;
    }


    /**
     * Sets the options value for this CallMaintenanceActionRequest.
     * 
     * @param options
     */
    public void setOptions(de.zib.gndms.dspace.stubs.CallMaintenanceActionRequestOptions options) {
        this.options = options;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CallMaintenanceActionRequest)) return false;
        CallMaintenanceActionRequest other = (CallMaintenanceActionRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.action==null && other.getAction()==null) || 
             (this.action!=null &&
              this.action.equals(other.getAction()))) &&
            ((this.options==null && other.getOptions()==null) || 
             (this.options!=null &&
              this.options.equals(other.getOptions())));
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
        if (getAction() != null) {
            _hashCode += getAction().hashCode();
        }
        if (getOptions() != null) {
            _hashCode += getOptions().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CallMaintenanceActionRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">CallMaintenanceActionRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("action");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", "action"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("options");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", "options"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">>CallMaintenanceActionRequest>options"));
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
