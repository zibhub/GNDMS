/**
 * CreateSliceRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.subspace.stubs;

public class CreateSliceRequest  implements java.io.Serializable {
    private de.zib.gndms.dspace.subspace.stubs.CreateSliceRequestSliceCreationSpecifier sliceCreationSpecifier;
    private de.zib.gndms.dspace.subspace.stubs.CreateSliceRequestContext context;

    public CreateSliceRequest() {
    }

    public CreateSliceRequest(
           de.zib.gndms.dspace.subspace.stubs.CreateSliceRequestContext context,
           de.zib.gndms.dspace.subspace.stubs.CreateSliceRequestSliceCreationSpecifier sliceCreationSpecifier) {
           this.sliceCreationSpecifier = sliceCreationSpecifier;
           this.context = context;
    }


    /**
     * Gets the sliceCreationSpecifier value for this CreateSliceRequest.
     * 
     * @return sliceCreationSpecifier
     */
    public de.zib.gndms.dspace.subspace.stubs.CreateSliceRequestSliceCreationSpecifier getSliceCreationSpecifier() {
        return sliceCreationSpecifier;
    }


    /**
     * Sets the sliceCreationSpecifier value for this CreateSliceRequest.
     * 
     * @param sliceCreationSpecifier
     */
    public void setSliceCreationSpecifier(de.zib.gndms.dspace.subspace.stubs.CreateSliceRequestSliceCreationSpecifier sliceCreationSpecifier) {
        this.sliceCreationSpecifier = sliceCreationSpecifier;
    }


    /**
     * Gets the context value for this CreateSliceRequest.
     * 
     * @return context
     */
    public de.zib.gndms.dspace.subspace.stubs.CreateSliceRequestContext getContext() {
        return context;
    }


    /**
     * Sets the context value for this CreateSliceRequest.
     * 
     * @param context
     */
    public void setContext(de.zib.gndms.dspace.subspace.stubs.CreateSliceRequestContext context) {
        this.context = context;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateSliceRequest)) return false;
        CreateSliceRequest other = (CreateSliceRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
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
        new org.apache.axis.description.TypeDesc(CreateSliceRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace", ">CreateSliceRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sliceCreationSpecifier");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace", "sliceCreationSpecifier"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace", ">>CreateSliceRequest>sliceCreationSpecifier"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("context");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace", "context"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace", ">>CreateSliceRequest>context"));
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
