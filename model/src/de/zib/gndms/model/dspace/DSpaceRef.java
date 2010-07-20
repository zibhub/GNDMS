package de.zib.gndms.model.dspace;

import de.zib.gndms.model.common.SimpleRKRef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * VEPRef to a DSpace instance
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 30.07.2008 Time: 15:01:58
 */
@Embeddable
public class DSpaceRef extends SimpleRKRef {
    private static final QName RESOURCE_KEY_NAME =
        new QName("http://dspace.gndms.zib.de/DSpace", "DSpaceKey");
    private static final List<String> RESOURCE_NAMES =
        new ArrayList( 1 ) {{ add( "DSpace" ); }};

    private String gridSiteId;
    private String resourceKeyValue;

    @Transient
    public QName getResourceKeyName () { return RESOURCE_KEY_NAME; }
    @Transient
    public List<String> getResourceNames () { return RESOURCE_NAMES; }

    @Column(name="key_site", nullable=true, updatable=false, columnDefinition="CHAR", length=64)
    public String getGridSiteId () { return gridSiteId; }
    public void setGridSiteId (final String newSiteId) { gridSiteId = newSiteId; }

    @Column(name="key_val", nullable=false, updatable=false, columnDefinition="CHAR", length=36)
    public String getResourceKeyValue () { return resourceKeyValue; }
    public void setResourceKeyValue (final String newValue) { resourceKeyValue = newValue; }
}
