/**
 * TaskStatusT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package types;

public class TaskStatusT implements java.io.Serializable {
    private org.apache.axis.types.NormalizedString _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TaskStatusT(org.apache.axis.types.NormalizedString value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final org.apache.axis.types.NormalizedString _unknown = new org.apache.axis.types.NormalizedString("unknown");
    public static final org.apache.axis.types.NormalizedString _created = new org.apache.axis.types.NormalizedString("created");
    public static final org.apache.axis.types.NormalizedString _initialized = new org.apache.axis.types.NormalizedString("initialized");
    public static final org.apache.axis.types.NormalizedString _inprogress = new org.apache.axis.types.NormalizedString("inprogress");
    public static final org.apache.axis.types.NormalizedString _failed = new org.apache.axis.types.NormalizedString("failed");
    public static final org.apache.axis.types.NormalizedString _finished = new org.apache.axis.types.NormalizedString("finished");
    public static final TaskStatusT unknown = new TaskStatusT(_unknown);
    public static final TaskStatusT created = new TaskStatusT(_created);
    public static final TaskStatusT initialized = new TaskStatusT(_initialized);
    public static final TaskStatusT inprogress = new TaskStatusT(_inprogress);
    public static final TaskStatusT failed = new TaskStatusT(_failed);
    public static final TaskStatusT finished = new TaskStatusT(_finished);
    public org.apache.axis.types.NormalizedString getValue() { return _value_;}
    public static TaskStatusT fromValue(org.apache.axis.types.NormalizedString value)
          throws java.lang.IllegalArgumentException {
        TaskStatusT enumeration = (TaskStatusT)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TaskStatusT fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        try {
            return fromValue(new org.apache.axis.types.NormalizedString(value));
        } catch (Exception e) {
            throw new java.lang.IllegalArgumentException();
        }
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_.toString();}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TaskStatusT.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "TaskStatusT"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
