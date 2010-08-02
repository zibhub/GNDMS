/**
 * SliceCreationSpecifier.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package types;

public class SliceCreationSpecifier  implements java.io.Serializable {
    private org.apache.axis.types.URI sliceKind;
    private org.apache.axis.types.UnsignedLong totalStorageSize;
    private java.util.Calendar terminationTime;

    public SliceCreationSpecifier() {
    }

    public SliceCreationSpecifier(
           org.apache.axis.types.URI sliceKind,
           java.util.Calendar terminationTime,
           org.apache.axis.types.UnsignedLong totalStorageSize) {
           this.sliceKind = sliceKind;
           this.totalStorageSize = totalStorageSize;
           this.terminationTime = terminationTime;
    }


    /**
     * Gets the sliceKind value for this SliceCreationSpecifier.
     * 
     * @return sliceKind
     */
    public org.apache.axis.types.URI getSliceKind() {
        return sliceKind;
    }


    /**
     * Sets the sliceKind value for this SliceCreationSpecifier.
     * 
     * @param sliceKind
     */
    public void setSliceKind(org.apache.axis.types.URI sliceKind) {
        this.sliceKind = sliceKind;
    }


    /**
     * Gets the totalStorageSize value for this SliceCreationSpecifier.
     * 
     * @return totalStorageSize
     */
    public org.apache.axis.types.UnsignedLong getTotalStorageSize() {
        return totalStorageSize;
    }


    /**
     * Sets the totalStorageSize value for this SliceCreationSpecifier.
     * 
     * @param totalStorageSize
     */
    public void setTotalStorageSize(org.apache.axis.types.UnsignedLong totalStorageSize) {
        this.totalStorageSize = totalStorageSize;
    }


    /**
     * Gets the terminationTime value for this SliceCreationSpecifier.
     * 
     * @return terminationTime
     */
    public java.util.Calendar getTerminationTime() {
        return terminationTime;
    }


    /**
     * Sets the terminationTime value for this SliceCreationSpecifier.
     * 
     * @param terminationTime
     */
    public void setTerminationTime(java.util.Calendar terminationTime) {
        this.terminationTime = terminationTime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SliceCreationSpecifier)) return false;
        SliceCreationSpecifier other = (SliceCreationSpecifier) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sliceKind==null && other.getSliceKind()==null) || 
             (this.sliceKind!=null &&
              this.sliceKind.equals(other.getSliceKind()))) &&
            ((this.totalStorageSize==null && other.getTotalStorageSize()==null) || 
             (this.totalStorageSize!=null &&
              this.totalStorageSize.equals(other.getTotalStorageSize()))) &&
            ((this.terminationTime==null && other.getTerminationTime()==null) || 
             (this.terminationTime!=null &&
              this.terminationTime.equals(other.getTerminationTime())));
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
        if (getSliceKind() != null) {
            _hashCode += getSliceKind().hashCode();
        }
        if (getTotalStorageSize() != null) {
            _hashCode += getTotalStorageSize().hashCode();
        }
        if (getTerminationTime() != null) {
            _hashCode += getTerminationTime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SliceCreationSpecifier.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", ">SliceCreationSpecifier"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sliceKind");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "SliceKind"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalStorageSize");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "TotalStorageSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "unsignedLong"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("terminationTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "TerminationTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
