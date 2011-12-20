package de.zib.gndms.taskflows.staging.client.model;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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



import java.io.Serializable;
import java.util.List;


/**
 * This is the model class representing a xsd gndms:DataDescriptor.
 * It is intended to store common data for staging requests.
 *
 * @author  try ma ik jo rr a zib
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 3:57:32 PM
 *
 * NOTE: for every not mandatory attribute of this class a has<AttributeName> method
 *       is provided to check if the attribute is set or not.
 * NOTE: If the constrains attribute is missing this should be interpreted as just download.
 *       If *ArchivFormat is provided it means, do create an archive with the requested data in the
 *          given format.
 */
public class DataDescriptor implements Serializable {

    private List<String> objectList; // required
    private DataConstraints constrains; // not required 
    private String dataFormat; // required
    private String dataArchiveFormat; // not required
    private String metaDataFormat; // required
    private String metaDataArchiveFormat; // not required

    private static final long serialVersionUID = -574650275362439122L;


    public DataDescriptor() {
    }


    public List<String> getObjectList() {
        return objectList;
    }


    public void setObjectList( List<String> objectList ) {
        this.objectList = objectList;
    }


    public DataConstraints getConstrains() {
        return constrains;
    }


    public void setConstrains( DataConstraints constrains ) {
        this.constrains = constrains;
    }


    public boolean hasConstraints( ) {
        return constrains != null;
    }


    public boolean hasDataFormat( ) {
        return dataFormat != null;
    }

    
    public String getDataFormat() {
        return dataFormat;
    }


    public void setDataFormat( String dataFormat ) {
        this.dataFormat = dataFormat;
    }


    public String getDataArchiveFormat() {
        return dataArchiveFormat;
    }


    public void setDataArchiveFormat( String dataArchiveFormat ) {
        this.dataArchiveFormat = dataArchiveFormat;
    }


    public boolean hasDataArchiveFormat() {
        return dataArchiveFormat != null;
    }


    public String getMetaDataFormat() {
        return metaDataFormat;
    }


    public void setMetaDataFormat( String metaDataFormat ) {
        this.metaDataFormat = metaDataFormat;
    }


    public String getMetaDataArchiveFormat() {
        return metaDataArchiveFormat;
    }


    public void setMetaDataArchiveFormat( String metaDataArchiveFormat ) {
        this.metaDataArchiveFormat = metaDataArchiveFormat;
    }


    public boolean hasMetaDataArchiveFormat() {
        return metaDataArchiveFormat != null;
    }
}
