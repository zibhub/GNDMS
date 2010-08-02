/**
 * DSpacePortTypeSOAPBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.dspace.stubs.bindings;

public class DSpacePortTypeSOAPBindingStub extends org.apache.axis.client.Stub implements de.zib.gndms.dspace.stubs.DSpacePortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[9];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetSubspace");
        oper.addParameter(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", "GetSubspaceRequest"), new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">GetSubspaceRequest"), de.zib.gndms.dspace.stubs.GetSubspaceRequest.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">GetSubspaceResponse"));
        oper.setReturnClass(de.zib.gndms.dspace.stubs.GetSubspaceResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", "GetSubspaceResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/types", "UnknownSubspace"),
                      "de.zib.gndms.dspace.stubs.types.UnknownSubspace",
                      new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/types", "UnknownSubspace"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ListPublicSubspaces");
        oper.addParameter(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", "ListPublicSubspacesRequest"), new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">ListPublicSubspacesRequest"), de.zib.gndms.dspace.stubs.ListPublicSubspacesRequest.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">ListPublicSubspacesResponse"));
        oper.setReturnClass(de.zib.gndms.dspace.stubs.ListPublicSubspacesResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", "ListPublicSubspacesResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ListSupportedSchemas");
        oper.addParameter(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", "ListSupportedSchemasRequest"), new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">ListSupportedSchemasRequest"), de.zib.gndms.dspace.stubs.ListSupportedSchemasRequest.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">ListSupportedSchemasResponse"));
        oper.setReturnClass(de.zib.gndms.dspace.stubs.ListSupportedSchemasResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", "ListSupportedSchemasResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateSliceInSubspace");
        oper.addParameter(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", "CreateSliceInSubspaceRequest"), new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">CreateSliceInSubspaceRequest"), de.zib.gndms.dspace.stubs.CreateSliceInSubspaceRequest.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">CreateSliceInSubspaceResponse"));
        oper.setReturnClass(de.zib.gndms.dspace.stubs.CreateSliceInSubspaceResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", "CreateSliceInSubspaceResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/types", "UnknownSubspace"),
                      "de.zib.gndms.dspace.stubs.types.UnknownSubspace",
                      new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/types", "UnknownSubspace"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/types", "InternalFailure"),
                      "de.zib.gndms.dspace.stubs.types.InternalFailure",
                      new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/types", "InternalFailure"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace/types", "UnknownOrInvalidSliceKind"),
                      "de.zib.gndms.dspace.subspace.stubs.types.UnknownOrInvalidSliceKind",
                      new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace/types", "UnknownOrInvalidSliceKind"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace/types", "OutOfSpace"),
                      "de.zib.gndms.dspace.subspace.stubs.types.OutOfSpace",
                      new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace/types", "OutOfSpace"), 
                      true
                     ));
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("callMaintenanceAction");
        oper.addParameter(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", "CallMaintenanceActionRequest"), new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">CallMaintenanceActionRequest"), de.zib.gndms.dspace.stubs.CallMaintenanceActionRequest.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">CallMaintenanceActionResponse"));
        oper.setReturnClass(de.zib.gndms.dspace.stubs.CallMaintenanceActionResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", "CallMaintenanceActionResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetMultipleResourceProperties");
        oper.addParameter(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "GetMultipleResourceProperties"), new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">GetMultipleResourceProperties"), org.oasis.wsrf.properties.GetMultipleResourceProperties_Element.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">GetMultipleResourcePropertiesResponse"));
        oper.setReturnClass(org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "GetMultipleResourcePropertiesResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidResourcePropertyQNameFault"),
                      "org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType",
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidResourcePropertyQNameFaultType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "ResourceUnknownFault"),
                      "org.oasis.wsrf.properties.ResourceUnknownFaultType",
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "ResourceUnknownFaultType"), 
                      true
                     ));
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetResourceProperty");
        oper.addParameter(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "GetResourceProperty"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "QName"), javax.xml.namespace.QName.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">GetResourcePropertyResponse"));
        oper.setReturnClass(org.oasis.wsrf.properties.GetResourcePropertyResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "GetResourcePropertyResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidResourcePropertyQNameFault"),
                      "org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType",
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidResourcePropertyQNameFaultType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "ResourceUnknownFault"),
                      "org.oasis.wsrf.properties.ResourceUnknownFaultType",
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "ResourceUnknownFaultType"), 
                      true
                     ));
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("QueryResourceProperties");
        oper.addParameter(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "QueryResourceProperties"), new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">QueryResourceProperties"), org.oasis.wsrf.properties.QueryResourceProperties_Element.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">QueryResourcePropertiesResponse"));
        oper.setReturnClass(org.oasis.wsrf.properties.QueryResourcePropertiesResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "QueryResourcePropertiesResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "UnknownQueryExpressionDialectFault"),
                      "org.oasis.wsrf.properties.UnknownQueryExpressionDialectFaultType",
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "UnknownQueryExpressionDialectFaultType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "QueryEvaluationErrorFault"),
                      "org.oasis.wsrf.properties.QueryEvaluationErrorFaultType",
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "QueryEvaluationErrorFaultType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidQueryExpressionFault"),
                      "org.oasis.wsrf.properties.InvalidQueryExpressionFaultType",
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidQueryExpressionFaultType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidResourcePropertyQNameFault"),
                      "org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType",
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidResourcePropertyQNameFaultType"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "ResourceUnknownFault"),
                      "org.oasis.wsrf.properties.ResourceUnknownFaultType",
                      new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "ResourceUnknownFaultType"), 
                      true
                     ));
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getServiceSecurityMetadata");
        oper.addParameter(new javax.xml.namespace.QName("http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity", "GetServiceSecurityMetadataRequest"), new javax.xml.namespace.QName("http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity", ">GetServiceSecurityMetadataRequest"), gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataRequest.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity", ">GetServiceSecurityMetadataResponse"));
        oper.setReturnClass(gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity", "GetServiceSecurityMetadataResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[8] = oper;

    }

    public DSpacePortTypeSOAPBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public DSpacePortTypeSOAPBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public DSpacePortTypeSOAPBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
        addBindings0();
        addBindings1();
    }

    private void addBindings0() {
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "protectionLevelType");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.ProtectionLevelType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "SliceTransformSpecifierT");
            cachedSerQNames.add(qName);
            cls = types.SliceTransformSpecifierT.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/types", "InternalFailure");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.stubs.types.InternalFailure.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "FileMappingSeqT");
            cachedSerQNames.add(qName);
            cls = types.FileMappingSeqT.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">ResourcePropertyValueChangeNotificationType>NewValue");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.ResourcePropertyValueChangeNotificationTypeNewValue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidSetResourcePropertiesRequestContentFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.InvalidSetResourcePropertiesRequestContentFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">ListPublicSubspacesRequest");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.stubs.ListPublicSubspacesRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-BaseFaults-1.2-draft-01.xsd", ">BaseFaultType>ErrorCode");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.faults.BaseFaultTypeErrorCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">GetSubspaceResponse");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.stubs.GetSubspaceResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "UnknownQueryExpressionDialectFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.UnknownQueryExpressionDialectFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", ">SetTerminationTimeResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.SetTerminationTimeResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", ">ScheduledResourceTerminationRP");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.ScheduledResourceTerminationRP.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", ">TaskExecutionFailure");
            cachedSerQNames.add(qName);
            cls = types.TaskExecutionFailure.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", ">DestroyResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.DestroyResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">NotificationProducerRP");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.NotificationProducerRP.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "None");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.None.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "AttributedURI");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.message.addressing.AttributedURI.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">>CreateSliceInSubspaceRequest>context");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.stubs.CreateSliceInSubspaceRequestContext.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "DeleteType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.DeleteType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "FutureTimeT");
            cachedSerQNames.add(qName);
            cls = types.FutureTimeT.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "GridSiteT");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.types.NormalizedString.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "ReplyAfterType");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.message.addressing.ReplyAfterType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "FilenameT");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.types.NormalizedString.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "InvalidTopicExpressionFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.InvalidTopicExpressionFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "TopicNotSupportedFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.TopicNotSupportedFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "SliceTypeSpecifierT");
            cachedSerQNames.add(qName);
            cls = types.SliceTypeSpecifierT.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">SetResourcePropertiesResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.SetResourcePropertiesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "ReplicaSliceFilterT");
            cachedSerQNames.add(qName);
            cls = types.ReplicaSliceFilterT.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "ResourceNotDestroyedFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.ResourceNotDestroyedFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "SubscribeCreationFailedFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.SubscribeCreationFailedFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", ">SetTerminationTime");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.SetTerminationTime.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", ">>TaskExecutionFailure>ImplementationFault");
            cachedSerQNames.add(qName);
            cls = types.TaskExecutionFailureImplementationFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">SubscribeResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.SubscribeResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "QueryEvaluationErrorFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.QueryEvaluationErrorFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "SliceReferenceT");
            cachedSerQNames.add(qName);
            cls = types.SliceReferenceT.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "NotificationMessageHolderType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.NotificationMessageHolderType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "TerminationTimeChangeRejectedFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.TerminationTimeChangeRejectedFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidResourcePropertyQNameFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", ">TerminationNotification");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.TerminationNotification.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/types", "UnknownSubspace");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.stubs.types.UnknownSubspace.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">>CreateSliceInSubspaceRequest>sliceCreationSpecifier");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.stubs.CreateSliceInSubspaceRequestSliceCreationSpecifier.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "DynamicOfferDataSeqT");
            cachedSerQNames.add(qName);
            cls = types.DynamicOfferDataSeqT.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "SetResourcePropertyRequestFailedFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.SetResourcePropertyRequestFailedFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">ListPublicSubspacesResponse");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.stubs.ListPublicSubspacesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">SubscriptionManagerRP");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.SubscriptionManagerRP.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "ReferencePropertiesType");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.message.addressing.ReferencePropertiesType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">>CallMaintenanceActionRequest>options");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.stubs.CallMaintenanceActionRequestOptions.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "ReplicaSlicesT");
            cachedSerQNames.add(qName);
            cls = types.ReplicaSlicesT.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/types", ">DSpaceReference");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.stubs.types.DSpaceReference.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "ResourceUnknownFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.ResourceUnknownFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">QueryResourceProperties");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.QueryResourceProperties_Element.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "QueryExpressionType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.QueryExpressionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "TaskStatusT");
            cachedSerQNames.add(qName);
            cls = types.TaskStatusT.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "RelationshipTypeValues");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.message.addressing.RelationshipTypeValues.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", ">ServiceSecurityMetadata>operations");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadataOperations.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "FileSeqT");
            cachedSerQNames.add(qName);
            cls = types.FileSeqT.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">GetMultipleResourcePropertiesResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "GSITransport");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.GSITransport.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "AttributedQName");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.message.addressing.AttributedQName.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "NoCurrentMessageOnTopicFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.NoCurrentMessageOnTopicFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "ReplicaSliceT");
            cachedSerQNames.add(qName);
            cls = types.ReplicaSliceT.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">GetResourcePropertyResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.GetResourcePropertyResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "TopicPathDialectUnknownFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.TopicPathDialectUnknownFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "CommunicationStyle");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.CommunicationStyle.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">GetCurrentMessageResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.GetCurrentMessageResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity", ">GetServiceSecurityMetadataResponse");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">>GetSubspaceRequest>subspaceSpecifier");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.stubs.GetSubspaceRequestSubspaceSpecifier.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">Subscribe");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.Subscribe.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "OfferExecutionContractT");
            cachedSerQNames.add(qName);
            cls = types.OfferExecutionContractT.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "CommunicationMechanism");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.CommunicationMechanism.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", ">SliceCreationSpecifier");
            cachedSerQNames.add(qName);
            cls = types.SliceCreationSpecifier.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Slice/types", ">SliceReference");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.slice.stubs.types.SliceReference.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-BaseFaults-1.2-draft-01.xsd", ">BaseFaultType>Description");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.faults.BaseFaultTypeDescription.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">PauseSubscriptionResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.PauseSubscriptionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">ListSupportedSchemasRequest");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.stubs.ListSupportedSchemasRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "Relationship");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.message.addressing.Relationship.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "PauseFailedFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.PauseFailedFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">ResourcePropertyValueChangeNotificationType>OldValue");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.ResourcePropertyValueChangeNotificationTypeOldValue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "FileMapEntryT");
            cachedSerQNames.add(qName);
            cls = types.FileMapEntryT.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">QueryResourcePropertiesResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.QueryResourcePropertiesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "UUIDT");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity", ">GetServiceSecurityMetadataRequest");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">ResumeSubscriptionResponse");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.ResumeSubscriptionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "ServiceSecurityMetadata");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadata.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">PauseSubscription");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.PauseSubscription.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">ListSupportedSchemasResponse");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.stubs.ListSupportedSchemasResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "GSISecureMessage");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.GSISecureMessage.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "ResourceUnknownFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.ResourceUnknownFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "EndpointReferenceType");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.message.addressing.EndpointReferenceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "UnableToModifyResourcePropertyFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.UnableToModifyResourcePropertyFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "ResumeFailedFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.ResumeFailedFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">ResumeSubscription");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.ResumeSubscription.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", ">TaskExecutionState");
            cachedSerQNames.add(qName);
            cls = types.TaskExecutionState.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity", ">ServiceSecurityResourceProperties");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.introduce.security.stubs.ServiceSecurityResourceProperties.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", ">Destroy");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.Destroy.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "ContextT");
            cachedSerQNames.add(qName);
            cls = types.ContextT.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace/types", "UnknownOrInvalidSliceKind");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.subspace.stubs.types.UnknownOrInvalidSliceKind.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">CallMaintenanceActionRequest");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.stubs.CallMaintenanceActionRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "FaultSubcodeValues");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.message.addressing.FaultSubcodeValues.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-BaseFaults-1.2-draft-01.xsd", "BaseFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.faults.BaseFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }
    private void addBindings1() {
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">Notify");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.Notify.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace/types", "OutOfSpace");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.subspace.stubs.types.OutOfSpace.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/03/addressing", "ServiceNameType");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.message.addressing.ServiceNameType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InsertType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.InsertType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "ResourcePropertyValueChangeNotificationType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.ResourcePropertyValueChangeNotificationType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">SetResourceProperties");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.SetResourceProperties_Element.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">GetSubspaceRequest");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.stubs.GetSubspaceRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">CreateSliceInSubspaceResponse");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.stubs.CreateSliceInSubspaceResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "ResourceUnknownFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.ResourceUnknownFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">CreateSliceInSubspaceRequest");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.stubs.CreateSliceInSubspaceRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">>CreateSliceInSubspaceRequest>subspaceSpecifier");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.stubs.CreateSliceInSubspaceRequestSubspaceSpecifier.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "StorageSizeT");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.types.UnsignedLong.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", ">GetCurrentMessage");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.GetCurrentMessage.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "Operation");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.Operation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://gndms.zib.de/common/types", ">ContextT>entry");
            cachedSerQNames.add(qName);
            cls = types.ContextTEntry.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">DSpaceResourceProperties");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.stubs.DSpaceResourceProperties.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "UpdateType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.UpdateType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace/Subspace/types", ">SubspaceReference");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.subspace.stubs.types.SubspaceReference.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "UnableToSetTerminationTimeFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.lifetime.UnableToSetTerminationTimeFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://dspace.gndms.zib.de/DSpace", ">CallMaintenanceActionResponse");
            cachedSerQNames.add(qName);
            cls = de.zib.gndms.dspace.stubs.CallMaintenanceActionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", "InvalidQueryExpressionFaultType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.InvalidQueryExpressionFaultType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.xsd", ">GetMultipleResourceProperties");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsrf.properties.GetMultipleResourceProperties_Element.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security", "GSISecureConversation");
            cachedSerQNames.add(qName);
            cls = gov.nih.nci.cagrid.metadata.security.GSISecureConversation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.xsd", "TopicExpressionType");
            cachedSerQNames.add(qName);
            cls = org.oasis.wsn.TopicExpressionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call =
                    (org.apache.axis.client.Call) super.service.createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public de.zib.gndms.dspace.stubs.GetSubspaceResponse getSubspace(de.zib.gndms.dspace.stubs.GetSubspaceRequest parameters) throws java.rmi.RemoteException, de.zib.gndms.dspace.stubs.types.UnknownSubspace {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://dspace.gndms.zib.de/DSpace/DSpacePortType/GetSubspaceRequest");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetSubspace"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (de.zib.gndms.dspace.stubs.GetSubspaceResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (de.zib.gndms.dspace.stubs.GetSubspaceResponse) org.apache.axis.utils.JavaUtils.convert(_resp, de.zib.gndms.dspace.stubs.GetSubspaceResponse.class);
            }
        }
    }

    public de.zib.gndms.dspace.stubs.ListPublicSubspacesResponse listPublicSubspaces(de.zib.gndms.dspace.stubs.ListPublicSubspacesRequest parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://dspace.gndms.zib.de/DSpace/DSpacePortType/ListPublicSubspacesRequest");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "ListPublicSubspaces"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (de.zib.gndms.dspace.stubs.ListPublicSubspacesResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (de.zib.gndms.dspace.stubs.ListPublicSubspacesResponse) org.apache.axis.utils.JavaUtils.convert(_resp, de.zib.gndms.dspace.stubs.ListPublicSubspacesResponse.class);
            }
        }
    }

    public de.zib.gndms.dspace.stubs.ListSupportedSchemasResponse listSupportedSchemas(de.zib.gndms.dspace.stubs.ListSupportedSchemasRequest parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://dspace.gndms.zib.de/DSpace/DSpacePortType/ListSupportedSchemasRequest");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "ListSupportedSchemas"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (de.zib.gndms.dspace.stubs.ListSupportedSchemasResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (de.zib.gndms.dspace.stubs.ListSupportedSchemasResponse) org.apache.axis.utils.JavaUtils.convert(_resp, de.zib.gndms.dspace.stubs.ListSupportedSchemasResponse.class);
            }
        }
    }

    public de.zib.gndms.dspace.stubs.CreateSliceInSubspaceResponse createSliceInSubspace(de.zib.gndms.dspace.stubs.CreateSliceInSubspaceRequest parameters) throws java.rmi.RemoteException, de.zib.gndms.dspace.stubs.types.UnknownSubspace, de.zib.gndms.dspace.stubs.types.InternalFailure, de.zib.gndms.dspace.subspace.stubs.types.UnknownOrInvalidSliceKind, de.zib.gndms.dspace.subspace.stubs.types.OutOfSpace {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://dspace.gndms.zib.de/DSpace/DSpacePortType/CreateSliceInSubspaceRequest");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "CreateSliceInSubspace"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (de.zib.gndms.dspace.stubs.CreateSliceInSubspaceResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (de.zib.gndms.dspace.stubs.CreateSliceInSubspaceResponse) org.apache.axis.utils.JavaUtils.convert(_resp, de.zib.gndms.dspace.stubs.CreateSliceInSubspaceResponse.class);
            }
        }
    }

    public de.zib.gndms.dspace.stubs.CallMaintenanceActionResponse callMaintenanceAction(de.zib.gndms.dspace.stubs.CallMaintenanceActionRequest parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://dspace.gndms.zib.de/DSpace/CallMaintenanceActionRequest");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "callMaintenanceAction"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (de.zib.gndms.dspace.stubs.CallMaintenanceActionResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (de.zib.gndms.dspace.stubs.CallMaintenanceActionResponse) org.apache.axis.utils.JavaUtils.convert(_resp, de.zib.gndms.dspace.stubs.CallMaintenanceActionResponse.class);
            }
        }
    }

    public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element getMultipleResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://dspace.gndms.zib.de/DSpace/DSpacePortType/GetMultipleResourcePropertiesRequest");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetMultipleResourceProperties"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {getMultipleResourcePropertiesRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse) org.apache.axis.utils.JavaUtils.convert(_resp, org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse.class);
            }
        }
    }

    public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName getResourcePropertyRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://dspace.gndms.zib.de/DSpace/DSpacePortType/GetResourcePropertyRequest");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetResourceProperty"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {getResourcePropertyRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.oasis.wsrf.properties.GetResourcePropertyResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.oasis.wsrf.properties.GetResourcePropertyResponse) org.apache.axis.utils.JavaUtils.convert(_resp, org.oasis.wsrf.properties.GetResourcePropertyResponse.class);
            }
        }
    }

    public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element queryResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.UnknownQueryExpressionDialectFaultType, org.oasis.wsrf.properties.QueryEvaluationErrorFaultType, org.oasis.wsrf.properties.InvalidQueryExpressionFaultType, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://dspace.gndms.zib.de/DSpace/DSpacePortType/QueryResourcePropertiesRequest");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "QueryResourceProperties"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryResourcePropertiesRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.oasis.wsrf.properties.QueryResourcePropertiesResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.oasis.wsrf.properties.QueryResourcePropertiesResponse) org.apache.axis.utils.JavaUtils.convert(_resp, org.oasis.wsrf.properties.QueryResourcePropertiesResponse.class);
            }
        }
    }

    public gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse getServiceSecurityMetadata(gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataRequest parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://dspace.gndms.zib.de/DSpace/DSpacePortType/getServiceSecurityMetadataRequest");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getServiceSecurityMetadata"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse) org.apache.axis.utils.JavaUtils.convert(_resp, gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse.class);
            }
        }
    }

}
