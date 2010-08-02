/**
 * DataDescriptorTConstraints.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package types;

public class DataDescriptorTConstraints  implements java.io.Serializable {
    private types.SpaceConstraintT spaceConstr;
    private types.TimeConstraintT timeConstr;
    private types.CFListT CFList;
    private types.ConstraintListT constraintList;

    public DataDescriptorTConstraints() {
    }

    public DataDescriptorTConstraints(
           types.CFListT CFList,
           types.ConstraintListT constraintList,
           types.SpaceConstraintT spaceConstr,
           types.TimeConstraintT timeConstr) {
           this.spaceConstr = spaceConstr;
           this.timeConstr = timeConstr;
           this.CFList = CFList;
           this.constraintList = constraintList;
    }


    /**
     * Gets the spaceConstr value for this DataDescriptorTConstraints.
     * 
     * @return spaceConstr
     */
    public types.SpaceConstraintT getSpaceConstr() {
        return spaceConstr;
    }


    /**
     * Sets the spaceConstr value for this DataDescriptorTConstraints.
     * 
     * @param spaceConstr
     */
    public void setSpaceConstr(types.SpaceConstraintT spaceConstr) {
        this.spaceConstr = spaceConstr;
    }


    /**
     * Gets the timeConstr value for this DataDescriptorTConstraints.
     * 
     * @return timeConstr
     */
    public types.TimeConstraintT getTimeConstr() {
        return timeConstr;
    }


    /**
     * Sets the timeConstr value for this DataDescriptorTConstraints.
     * 
     * @param timeConstr
     */
    public void setTimeConstr(types.TimeConstraintT timeConstr) {
        this.timeConstr = timeConstr;
    }


    /**
     * Gets the CFList value for this DataDescriptorTConstraints.
     * 
     * @return CFList
     */
    public types.CFListT getCFList() {
        return CFList;
    }


    /**
     * Sets the CFList value for this DataDescriptorTConstraints.
     * 
     * @param CFList
     */
    public void setCFList(types.CFListT CFList) {
        this.CFList = CFList;
    }


    /**
     * Gets the constraintList value for this DataDescriptorTConstraints.
     * 
     * @return constraintList
     */
    public types.ConstraintListT getConstraintList() {
        return constraintList;
    }


    /**
     * Sets the constraintList value for this DataDescriptorTConstraints.
     * 
     * @param constraintList
     */
    public void setConstraintList(types.ConstraintListT constraintList) {
        this.constraintList = constraintList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DataDescriptorTConstraints)) return false;
        DataDescriptorTConstraints other = (DataDescriptorTConstraints) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.spaceConstr==null && other.getSpaceConstr()==null) || 
             (this.spaceConstr!=null &&
              this.spaceConstr.equals(other.getSpaceConstr()))) &&
            ((this.timeConstr==null && other.getTimeConstr()==null) || 
             (this.timeConstr!=null &&
              this.timeConstr.equals(other.getTimeConstr()))) &&
            ((this.CFList==null && other.getCFList()==null) || 
             (this.CFList!=null &&
              this.CFList.equals(other.getCFList()))) &&
            ((this.constraintList==null && other.getConstraintList()==null) || 
             (this.constraintList!=null &&
              this.constraintList.equals(other.getConstraintList())));
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
        if (getSpaceConstr() != null) {
            _hashCode += getSpaceConstr().hashCode();
        }
        if (getTimeConstr() != null) {
            _hashCode += getTimeConstr().hashCode();
        }
        if (getCFList() != null) {
            _hashCode += getCFList().hashCode();
        }
        if (getConstraintList() != null) {
            _hashCode += getConstraintList().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DataDescriptorTConstraints.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", ">DataDescriptorT>Constraints"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spaceConstr");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "SpaceConstr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "SpaceConstraintT"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeConstr");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "TimeConstr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "TimeConstraintT"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CFList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "CFList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "CFListT"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("constraintList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "ConstraintList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "ConstraintListT"));
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
