package de.zib.gndms.model.dspace.types;

import de.zib.gndms.model.common.SimpleRKRef;

import javax.xml.namespace.QName;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Arrays;

/**
 * This is the model for a slice reference.
 *
 * Use infra.GNDMSTools::SliceRefAsEPR to obtain a EPR for this ref.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 03.11.2008, Time: 10:30:48
 */
public class SliceRef extends SimpleRKRef {

    public static final QName RESOURCE_KEY_NAME =
        new QName("http://dspace.gndms.zib.de/DSpace/Slice", "SliceKey");

    private static final List<String> RESOURCE_NAMES =
        Arrays.asList( "DSpace", "Subspace", "Slice" );

    private String resourceKeyValue; // e.g. the resources uuid
    private String gridSiteId; // the service uri

    @NotNull
    @Override
    public QName getResourceKeyName() {
        return RESOURCE_KEY_NAME;
    }


    @Override
    public String getResourceKeyValue() {
        return resourceKeyValue;
    }


    @Override
    public void setResourceKeyValue( String newValue ) {
        resourceKeyValue = newValue;
    }


    @Override
    public String getGridSiteId() {
        return gridSiteId;
    }


    @Override
    public void setGridSiteId( String newSiteId ) {
        gridSiteId = newSiteId;
    }


    @NotNull
    @Override
    public List<String> getResourceNames() {
        return RESOURCE_NAMES;
    }
}
