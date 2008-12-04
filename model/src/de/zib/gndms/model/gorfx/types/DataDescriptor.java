package de.zib.gndms.model.gorfx.types;

import java.io.Serializable;


/**
 * This is the model class representing a xsd gndms:DataDescriptor.
 * It is intended to store common data for staging requests.
 *
 * @author Maik Jorra <jorra@zib.de>
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

    // todo maybe use a real object list here
    private String[] objectList; // required
    private DataConstraints constrains; // not required 
    private String dataFormat; // required
    private String dataArchiveFormat; // not required
    private String metaDataFormat; // required
    private String metaDataArchiveFormat; // not required

    private static final long serialVersionUID = -574650275362439122L;


    public DataDescriptor() {
    }


    public String[] getObjectList() {
        return objectList;
    }


    public void setObjectList( String[] objectList ) {
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
