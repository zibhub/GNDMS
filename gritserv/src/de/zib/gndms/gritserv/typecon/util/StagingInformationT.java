/**
/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



 * StagingInformationT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Feb 27, 2008 (08:34:14 CST) WSDL2Java emitter.
 */

package de.zib.gndms.gritserv.typecon.util;

import types.ProviderStageInORQT;
import types.OfferExecutionContractT;
import types.ContextT;

public class StagingInformationT  implements java.io.Serializable {
    private ProviderStageInORQT providerStageInORQ;
    private OfferExecutionContractT offerExecutionContract;
    private ContextT context;
    private java.lang.Object justDownload;
    private java.lang.Object justEstimate;

    public StagingInformationT() {
    }

    public StagingInformationT(
           types.ContextT context,
           java.lang.Object justDownload,
           java.lang.Object justEstimate,     
           types.OfferExecutionContractT offerExecutionContract,
           types.ProviderStageInORQT providerStageInORQ) {
           this.providerStageInORQ = providerStageInORQ;
           this.offerExecutionContract = offerExecutionContract;
           this.context = context;
           this.justDownload = justDownload;
           this.justEstimate = justEstimate;
    }


    /**
     * Gets the providerStageInORQ value for this StagingInformationT.
     * 
     * @return providerStageInORQ
     */
    public types.ProviderStageInORQT getProviderStageInORQ() {
        return providerStageInORQ;
    }


    /**
     * Sets the providerStageInORQ value for this StagingInformationT.
     * 
     * @param providerStageInORQ
     */
    public void setProviderStageInORQ(ProviderStageInORQT providerStageInORQ) {
        this.providerStageInORQ = providerStageInORQ;
    }


    /**
     * Gets the offerExecutionContract value for this StagingInformationT.
     * 
     * @return offerExecutionContract
     */
    public OfferExecutionContractT getOfferExecutionContract() {
        return offerExecutionContract;
    }


    /**
     * Sets the offerExecutionContract value for this StagingInformationT.
     * 
     * @param offerExecutionContract
     */
    public void setOfferExecutionContract(OfferExecutionContractT offerExecutionContract) {
        this.offerExecutionContract = offerExecutionContract;
    }


    /**
     * Gets the context value for this StagingInformationT.
     * 
     * @return context
     */
    public ContextT getContext() {
        return context;
    }


    /**
     * Sets the context value for this StagingInformationT.
     * 
     * @param context
     */
    public void setContext(ContextT context) {
        this.context = context;
    }


    /**
     * Gets the justDownload value for this StagingInformationT.
     * 
     * @return justDownload
     */
    public java.lang.Object getJustDownload() {
        return justDownload;
    }


    /**
     * Sets the justDownload value for this StagingInformationT.
     * 
     * @param justDownload
     */
    public void setJustDownload(java.lang.Object justDownload) {
        this.justDownload = justDownload;
    }


    /**
     * Gets the justEstimate value for this StagingInformationT.
     * 
     * @return justEstimate
     */
    public java.lang.Object getJustEstimate() {
        return justEstimate;
    }


    /**
     * Sets the justEstimate value for this StagingInformationT.
     * 
     * @param justEstimate
     */
    public void setJustEstimate(java.lang.Object justEstimate) {
        this.justEstimate = justEstimate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StagingInformationT)) return false;
        StagingInformationT other = (StagingInformationT) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.providerStageInORQ==null && other.getProviderStageInORQ()==null) || 
             (this.providerStageInORQ!=null &&
              this.providerStageInORQ.equals(other.getProviderStageInORQ()))) &&
            ((this.offerExecutionContract==null && other.getOfferExecutionContract()==null) || 
             (this.offerExecutionContract!=null &&
              this.offerExecutionContract.equals(other.getOfferExecutionContract()))) &&
            ((this.context==null && other.getContext()==null) || 
             (this.context!=null &&
              this.context.equals(other.getContext()))) &&
            ((this.justDownload==null && other.getJustDownload()==null) || 
             (this.justDownload!=null &&
              this.justDownload.equals(other.getJustDownload()))) &&
            ((this.justEstimate==null && other.getJustEstimate()==null) || 
             (this.justEstimate!=null &&
              this.justEstimate.equals(other.getJustEstimate())));
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
        if (getProviderStageInORQ() != null) {
            _hashCode += getProviderStageInORQ().hashCode();
        }
        if (getOfferExecutionContract() != null) {
            _hashCode += getOfferExecutionContract().hashCode();
        }
        if (getContext() != null) {
            _hashCode += getContext().hashCode();
        }
        if (getJustDownload() != null) {
            _hashCode += getJustDownload().hashCode();
        }
        if (getJustEstimate() != null) {
            _hashCode += getJustEstimate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StagingInformationT.class, true);

    /**
     * XSD-Schema description
     */
    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "StagingInformationT"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("providerStageInORQ");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "ProviderStageInORQ"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "ProviderStageInORQT"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("offerExecutionContract");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "OfferExecutionContract"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "OfferExecutionContractT"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("context");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "Context"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://gndms.zib.de/common/types", "ContextT"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("justDownload");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "JustDownload"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("justEstimate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "JustEstimate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
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
