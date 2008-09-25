package de.zib.gndms.model.gorfx.types;
import java.util.HashMap;

/**
 * This is the model class representing a xsd gndms:DataDescriptor.
 * It is intended to store common data for staging requests.
 *
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 3:57:32 PM
 */
public class DataDescriptor {

    // todo maybe use a real object list here
    private String[] objectList;
    private SpaceConstraint spaceConstraint;
    private TimeConstraint timeConstraint;
    private String[] CFList;
    private HashMap<String,String> constraintList;
    private String dataFormat;
    private String metaDataFormat;


    public DataDescriptor() {
    }


    public String[] getObjectList() {
        return objectList;
    }


    public void setObjectList( String[] objectList ) {
        this.objectList = objectList;
    }


    public SpaceConstraint getSpaceConstraint() {
        return spaceConstraint;
    }


    public void setSpaceConstraint( SpaceConstraint spaceConstraint ) {
        this.spaceConstraint = spaceConstraint;
    }


    public TimeConstraint getTimeConstraint() {
        return timeConstraint;
    }


    public void setTimeConstraint( TimeConstraint timeConstraint ) {
        this.timeConstraint = timeConstraint;
    }


    public String[] getCFList() {
        return CFList;
    }


    public void setCFList( String[] CFList ) {
        this.CFList = CFList;
    }


    public HashMap<String, String> getConstraintList() {
        return constraintList;
    }


    public void setConstraintList( HashMap<String, String> constraintList ) {
        this.constraintList = constraintList;
    }


    public String getDataFormat() {
        return dataFormat;
    }


    public void setDataFormat( String dataFormat ) {
        this.dataFormat = dataFormat;
    }


    public String getMetaDataFormat() {
        return metaDataFormat;
    }


    public void setMetaDataFormat( String metaDataFormat ) {
        this.metaDataFormat = metaDataFormat;
    }

}
