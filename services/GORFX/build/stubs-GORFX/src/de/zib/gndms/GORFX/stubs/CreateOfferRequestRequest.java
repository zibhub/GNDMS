/**
 * CreateOfferRequestRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.GORFX.stubs;

public class CreateOfferRequestRequest  implements java.io.Serializable {
    private de.zib.gndms.GORFX.stubs.CreateOfferRequestRequestOfferRequestArguments offerRequestArguments;
    private de.zib.gndms.GORFX.stubs.CreateOfferRequestRequestContext context;

    public CreateOfferRequestRequest() {
    }

    public CreateOfferRequestRequest(
           de.zib.gndms.GORFX.stubs.CreateOfferRequestRequestContext context,
           de.zib.gndms.GORFX.stubs.CreateOfferRequestRequestOfferRequestArguments offerRequestArguments) {
           this.offerRequestArguments = offerRequestArguments;
           this.context = context;
    }


    /**
     * Gets the offerRequestArguments value for this CreateOfferRequestRequest.
     * 
     * @return offerRequestArguments
     */
    public de.zib.gndms.GORFX.stubs.CreateOfferRequestRequestOfferRequestArguments getOfferRequestArguments() {
        return offerRequestArguments;
    }


    /**
     * Sets the offerRequestArguments value for this CreateOfferRequestRequest.
     * 
     * @param offerRequestArguments
     */
    public void setOfferRequestArguments(de.zib.gndms.GORFX.stubs.CreateOfferRequestRequestOfferRequestArguments offerRequestArguments) {
        this.offerRequestArguments = offerRequestArguments;
    }


    /**
     * Gets the context value for this CreateOfferRequestRequest.
     * 
     * @return context
     */
    public de.zib.gndms.GORFX.stubs.CreateOfferRequestRequestContext getContext() {
        return context;
    }


    /**
     * Sets the context value for this CreateOfferRequestRequest.
     * 
     * @param context
     */
    public void setContext(de.zib.gndms.GORFX.stubs.CreateOfferRequestRequestContext context) {
        this.context = context;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateOfferRequestRequest)) return false;
        CreateOfferRequestRequest other = (CreateOfferRequestRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.offerRequestArguments==null && other.getOfferRequestArguments()==null) || 
             (this.offerRequestArguments!=null &&
              this.offerRequestArguments.equals(other.getOfferRequestArguments()))) &&
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
        if (getOfferRequestArguments() != null) {
            _hashCode += getOfferRequestArguments().hashCode();
        }
        if (getContext() != null) {
            _hashCode += getContext().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateOfferRequestRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://GORFX.gndms.zib.de/GORFX", ">CreateOfferRequestRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("offerRequestArguments");
        elemField.setXmlName(new javax.xml.namespace.QName("http://GORFX.gndms.zib.de/GORFX", "offerRequestArguments"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://GORFX.gndms.zib.de/GORFX", ">>CreateOfferRequestRequest>offerRequestArguments"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("context");
        elemField.setXmlName(new javax.xml.namespace.QName("http://GORFX.gndms.zib.de/GORFX", "context"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://GORFX.gndms.zib.de/GORFX", ">>CreateOfferRequestRequest>context"));
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
