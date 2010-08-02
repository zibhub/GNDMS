/**
 * DataDescriptorT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package types;

public class DataDescriptorT  implements java.io.Serializable {
    private types.ObjectListT objectList;
    private types.DataDescriptorTConstraints constraints;
    private java.lang.String dataFormat;
    private java.lang.String dataArchiveFormat;
    private java.lang.String metaDataFormat;
    private java.lang.String metaDataArchiveFormat;

    public DataDescriptorT() {
    }

    public DataDescriptorT(
           types.DataDescriptorTConstraints constraints,
           java.lang.String dataArchiveFormat,
           java.lang.String dataFormat,
           java.lang.String metaDataArchiveFormat,
           java.lang.String metaDataFormat,
           types.ObjectListT objectList) {
           this.objectList = objectList;
           this.constraints = constraints;
           this.dataFormat = dataFormat;
           this.dataArchiveFormat = dataArchiveFormat;
           this.metaDataFormat = metaDataFormat;
           this.metaDataArchiveFormat = metaDataArchiveFormat;
    }


    /**
     * Gets the objectList value for this DataDescriptorT.
     * 
     * @return objectList
     */
    public types.ObjectListT getObjectList() {
        return objectList;
    }


    /**
     * Sets the objectList value for this DataDescriptorT.
     * 
     * @param objectList
     */
    public void setObjectList(types.ObjectListT objectList) {
        this.objectList = objectList;
    }


    /**
     * Gets the constraints value for this DataDescriptorT.
     * 
     * @return constraints
     */
    public types.DataDescriptorTConstraints getConstraints() {
        return constraints;
    }


    /**
     * Sets the constraints value for this DataDescriptorT.
     * 
     * @param constraints
     */
    public void setConstraints(types.DataDescriptorTConstraints constraints) {
        this.constraints = constraints;
    }


    /**
     * Gets the dataFormat value for this DataDescriptorT.
     * 
     * @return dataFormat
     */
    public java.lang.String getDataFormat() {
        return dataFormat;
    }


    /**
     * Sets the dataFormat value for this DataDescriptorT.
     * 
     * @param dataFormat
     */
    public void setDataFormat(java.lang.String dataFormat) {
        this.dataFormat = dataFormat;
    }


    /**
     * Gets the dataArchiveFormat value for this DataDescriptorT.
     * 
     * @return dataArchiveFormat
     */
    public java.lang.String getDataArchiveFormat() {
        return dataArchiveFormat;
    }


    /**
     * Sets the dataArchiveFormat value for this DataDescriptorT.
     * 
     * @param dataArchiveFormat
     */
    public void setDataArchiveFormat(java.lang.String dataArchiveFormat) {
        this.dataArchiveFormat = dataArchiveFormat;
    }


    /**
     * Gets the metaDataFormat value for this DataDescriptorT.
     * 
     * @return metaDataFormat
     */
    public java.lang.String getMetaDataFormat() {
        return metaDataFormat;
    }


    /**
     * Sets the metaDataFormat value for this DataDescriptorT.
     * 
     * @param metaDataFormat
     */
    public void setMetaDataFormat(java.lang.String metaDataFormat) {
        this.metaDataFormat = metaDataFormat;
    }


    /**
     * Gets the metaDataArchiveFormat value for this DataDescriptorT.
     * 
     * @return metaDataArchiveFormat
     */
    public java.lang.String getMetaDataArchiveFormat() {
        return metaDataArchiveFormat;
    }


    /**
     * Sets the metaDataArchiveFormat value for this DataDescriptorT.
     * 
     * @param metaDataArchiveFormat
     */
    public void setMetaDataArchiveFormat(java.lang.String metaDataArchiveFormat) {
        this.metaDataArchiveFormat = metaDataArchiveFormat;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DataDescriptorT)) return false;
        DataDescriptorT other = (DataDescriptorT) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.objectList==null && other.getObjectList()==null) || 
             (this.objectList!=null &&
              this.objectList.equals(other.getObjectList()))) &&
            ((this.constraints==null && other.getConstraints()==null) || 
             (this.constraints!=null &&
              this.constraints.equals(other.getConstraints()))) &&
            ((this.dataFormat==null && other.getDataFormat()==null) || 
             (this.dataFormat!=null &&
              this.dataFormat.equals(other.getDataFormat()))) &&
            ((this.dataArchiveFormat==null && other.getDataArchiveFormat()==null) || 
             (this.dataArchiveFormat!=null &&
              this.dataArchiveFormat.equals(other.getDataArchiveFormat()))) &&
            ((this.metaDataFormat==null && other.getMetaDataFormat()==null) || 
             (this.metaDataFormat!=null &&
              this.metaDataFormat.equals(other.getMetaDataFormat()))) &&
            ((this.metaDataArchiveFormat==null && other.getMetaDataArchiveFormat()==null) || 
             (this.metaDataArchiveFormat!=null &&
              this.metaDataArchiveFormat.equals(other.getMetaDataArchiveFormat())));
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
        if (getObjectList() != null) {
            _hashCode += getObjectList().hashCode();
        }
        if (getConstraints() != null) {
            _hashCode += getConstraints().hashCode();
        }
        if (getDataFormat() != null) {
            _hashCode += getDataFormat().hashCode();
        }
        if (getDataArchiveFormat() != null) {
            _hashCode += getDataArchiveFormat().hashCode();
        }
        if (getMetaDataFormat() != null) {
            _hashCode += getMetaDataFormat().hashCode();
        }
        if (getMetaDataArchiveFormat() != null) {
            _hashCode += getMetaDataArchiveFormat().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DataDescriptorT.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "DataDescriptorT"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("objectList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "ObjectList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "ObjectListT"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("constraints");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "Constraints"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", ">DataDescriptorT>Constraints"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataFormat");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "DataFormat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataArchiveFormat");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "DataArchiveFormat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("metaDataFormat");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "MetaDataFormat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("metaDataArchiveFormat");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "MetaDataArchiveFormat"));
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
