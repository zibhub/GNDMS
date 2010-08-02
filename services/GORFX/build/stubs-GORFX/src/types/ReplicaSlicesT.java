/**
 * ReplicaSlicesT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package types;

public class ReplicaSlicesT  implements java.io.Serializable {
    private types.ReplicaSliceT[] replicaSlice;

    public ReplicaSlicesT() {
    }

    public ReplicaSlicesT(
           types.ReplicaSliceT[] replicaSlice) {
           this.replicaSlice = replicaSlice;
    }


    /**
     * Gets the replicaSlice value for this ReplicaSlicesT.
     * 
     * @return replicaSlice
     */
    public types.ReplicaSliceT[] getReplicaSlice() {
        return replicaSlice;
    }


    /**
     * Sets the replicaSlice value for this ReplicaSlicesT.
     * 
     * @param replicaSlice
     */
    public void setReplicaSlice(types.ReplicaSliceT[] replicaSlice) {
        this.replicaSlice = replicaSlice;
    }

    public types.ReplicaSliceT getReplicaSlice(int i) {
        return this.replicaSlice[i];
    }

    public void setReplicaSlice(int i, types.ReplicaSliceT _value) {
        this.replicaSlice[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReplicaSlicesT)) return false;
        ReplicaSlicesT other = (ReplicaSlicesT) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.replicaSlice==null && other.getReplicaSlice()==null) || 
             (this.replicaSlice!=null &&
              java.util.Arrays.equals(this.replicaSlice, other.getReplicaSlice())));
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
        if (getReplicaSlice() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getReplicaSlice());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getReplicaSlice(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReplicaSlicesT.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "ReplicaSlicesT"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("replicaSlice");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "ReplicaSlice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "ReplicaSliceT"));
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
