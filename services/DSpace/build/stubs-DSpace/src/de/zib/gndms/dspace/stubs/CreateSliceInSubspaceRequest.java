/**
 * CreateSliceInSubspaceRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.stubs;

public class CreateSliceInSubspaceRequest  implements java.io.Serializable {
    private de.zib.gndms.dspace.stubs.CreateSliceInSubspaceRequestSubspaceSpecifier subspaceSpecifier;
    private de.zib.gndms.dspace.stubs.CreateSliceInSubspaceRequestSliceCreationSpecifier sliceCreationSpecifier;
    private de.zib.gndms.dspace.stubs.CreateSliceInSubspaceRequestContext context;

    public CreateSliceInSubspaceRequest() {
    }

    public CreateSliceInSubspaceRequest(
           de.zib.gndms.dspace.stubs.CreateSliceInSubspaceRequestContext context,
           de.zib.gndms.dspace.stubs.CreateSliceInSubspaceRequestSliceCreationSpecifier sliceCreationSpecifier,
           de.zib.gndms.dspace.stubs.CreateSliceInSubspaceRequestSubspaceSpecifier subspaceSpecifier) {
           this.subspaceSpecifier = subspaceSpecifier;
           this.sliceCreationSpecifier = sliceCreationSpecifier;
           this.context = context;
    }


    /**
     * Gets the subspaceSpecifier value for this CreateSliceInSubspaceRequest.
     * 
     * @return subspaceSpecifier
     */
    public de.zib.gndms.dspace.stubs.CreateSliceInSubspaceRequestSubspaceSpecifier getSubspaceSpecifier() {
        return subspaceSpecifier;
    }


    /**
     * Sets the subspaceSpecifier value for this CreateSliceInSubspaceRequest.
     * 
     * @param subspaceSpecifier
     */
    public void setSubspaceSpecifier(de.zib.gndms.dspace.stubs.CreateSliceInSubspaceRequestSubspaceSpecifier subspaceSpecifier) {
        this.subspaceSpecifier = subspaceSpecifier;
    }


    /**
     * Gets the sliceCreationSpecifier value for this CreateSliceInSubspaceRequest.
     * 
     * @return sliceCreationSpecifier
     */
    public de.zib.gndms.dspace.stubs.CreateSliceInSubspaceRequestSliceCreationSpecifier getSliceCreationSpecifier() {
        return sliceCreationSpecifier;
    }


    /**
     * Sets the sliceCreationSpecifier value for this CreateSliceInSubspaceRequest.
     * 
     * @param sliceCreationSpecifier
     */
    public void setSliceCreationSpecifier(de.zib.gndms.dspace.stubs.CreateSliceInSubspaceRequestSliceCreationSpecifier sliceCreationSpecifier) {
        this.sliceCreationSpecifier = sliceCreationSpecifier;
    }


    /**
     * Gets the context value for this CreateSliceInSubspaceRequest.
     * 
     * @return context
     */
    public de.zib.gndms.dspace.stubs.CreateSliceInSubspaceRequestContext getContext() {
        return context;
    }


    /**
     * Sets the context value for this CreateSliceInSubspaceRequest.
     * 
     * @param context
     */
    public void setContext(de.zib.gndms.dspace.stubs.CreateSliceInSubspaceRequestContext context) {
        this.context = context;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateSliceInSubspaceRequest)) return false;
        CreateSliceInSubspaceRequest other = (CreateSliceInSubspaceRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.subspaceSpecifier==null && other.getSubspaceSpecifier()==null) || 
             (this.subspaceSpecifier!=null &&
              this.subspaceSpecifier.equals(other.getSubspaceSpecifier()))) &&
            ((this.sliceCreationSpecifier==null && other.getSliceCreationSpecifier()==null) || 
             (this.sliceCreationSpecifier!=null &&
              this.sliceCreationSpecifier.equals(other.getSliceCreationSpecifier()))) &&
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
        if (getSubspaceSpecifier() != null) {
            _hashCode += getSubspaceSpecifier().hashCode();
        }
        if (getSliceCreationSpecifier() != null) {
            _hashCode += getSliceCreationSpecifier().hashCode();
        }
        if (getContext() != null) {
            _hashCode += getContext().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateSliceInSubspaceRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">CreateSliceInSubspaceRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subspaceSpecifier");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", "subspaceSpecifier"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">>CreateSliceInSubspaceRequest>subspaceSpecifier"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sliceCreationSpecifier");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", "sliceCreationSpecifier"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">>CreateSliceInSubspaceRequest>sliceCreationSpecifier"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("context");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", "context"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">>CreateSliceInSubspaceRequest>context"));
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
