/**
 * SpaceConstraintT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package types;

public class SpaceConstraintT  implements java.io.Serializable {
    private types.MinMaxT latitude;
    private types.MinMaxT longitude;
    private java.lang.String areaCRS;
    private types.MinMaxT altitude;
    private java.lang.String verticalCRS;

    public SpaceConstraintT() {
    }

    public SpaceConstraintT(
           types.MinMaxT altitude,
           java.lang.String areaCRS,
           types.MinMaxT latitude,
           types.MinMaxT longitude,
           java.lang.String verticalCRS) {
           this.latitude = latitude;
           this.longitude = longitude;
           this.areaCRS = areaCRS;
           this.altitude = altitude;
           this.verticalCRS = verticalCRS;
    }


    /**
     * Gets the latitude value for this SpaceConstraintT.
     * 
     * @return latitude
     */
    public types.MinMaxT getLatitude() {
        return latitude;
    }


    /**
     * Sets the latitude value for this SpaceConstraintT.
     * 
     * @param latitude
     */
    public void setLatitude(types.MinMaxT latitude) {
        this.latitude = latitude;
    }


    /**
     * Gets the longitude value for this SpaceConstraintT.
     * 
     * @return longitude
     */
    public types.MinMaxT getLongitude() {
        return longitude;
    }


    /**
     * Sets the longitude value for this SpaceConstraintT.
     * 
     * @param longitude
     */
    public void setLongitude(types.MinMaxT longitude) {
        this.longitude = longitude;
    }


    /**
     * Gets the areaCRS value for this SpaceConstraintT.
     * 
     * @return areaCRS
     */
    public java.lang.String getAreaCRS() {
        return areaCRS;
    }


    /**
     * Sets the areaCRS value for this SpaceConstraintT.
     * 
     * @param areaCRS
     */
    public void setAreaCRS(java.lang.String areaCRS) {
        this.areaCRS = areaCRS;
    }


    /**
     * Gets the altitude value for this SpaceConstraintT.
     * 
     * @return altitude
     */
    public types.MinMaxT getAltitude() {
        return altitude;
    }


    /**
     * Sets the altitude value for this SpaceConstraintT.
     * 
     * @param altitude
     */
    public void setAltitude(types.MinMaxT altitude) {
        this.altitude = altitude;
    }


    /**
     * Gets the verticalCRS value for this SpaceConstraintT.
     * 
     * @return verticalCRS
     */
    public java.lang.String getVerticalCRS() {
        return verticalCRS;
    }


    /**
     * Sets the verticalCRS value for this SpaceConstraintT.
     * 
     * @param verticalCRS
     */
    public void setVerticalCRS(java.lang.String verticalCRS) {
        this.verticalCRS = verticalCRS;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SpaceConstraintT)) return false;
        SpaceConstraintT other = (SpaceConstraintT) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.latitude==null && other.getLatitude()==null) || 
             (this.latitude!=null &&
              this.latitude.equals(other.getLatitude()))) &&
            ((this.longitude==null && other.getLongitude()==null) || 
             (this.longitude!=null &&
              this.longitude.equals(other.getLongitude()))) &&
            ((this.areaCRS==null && other.getAreaCRS()==null) || 
             (this.areaCRS!=null &&
              this.areaCRS.equals(other.getAreaCRS()))) &&
            ((this.altitude==null && other.getAltitude()==null) || 
             (this.altitude!=null &&
              this.altitude.equals(other.getAltitude()))) &&
            ((this.verticalCRS==null && other.getVerticalCRS()==null) || 
             (this.verticalCRS!=null &&
              this.verticalCRS.equals(other.getVerticalCRS())));
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
        if (getLatitude() != null) {
            _hashCode += getLatitude().hashCode();
        }
        if (getLongitude() != null) {
            _hashCode += getLongitude().hashCode();
        }
        if (getAreaCRS() != null) {
            _hashCode += getAreaCRS().hashCode();
        }
        if (getAltitude() != null) {
            _hashCode += getAltitude().hashCode();
        }
        if (getVerticalCRS() != null) {
            _hashCode += getVerticalCRS().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SpaceConstraintT.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "SpaceConstraintT"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("latitude");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "Latitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "MinMaxT"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("longitude");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "Longitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "MinMaxT"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("areaCRS");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "AreaCRS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("altitude");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "Altitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "MinMaxT"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("verticalCRS");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "VerticalCRS"));
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
