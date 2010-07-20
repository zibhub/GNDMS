package de.zib.gndms.model.dspace;

import de.zib.gndms.model.common.SimpleRKRef;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.namespace.QName;
import javax.persistence.Embedded;

/**
 * VEPRef to a DSpace instance
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 30.07.2008 Time: 15:01:58
 */
@Embeddable
class DSpaceRef extends SimpleRKRef {
    private static final QName RESOURCE_KEY_NAME =
        new QName("http://dspace.gndms.zib.de/DSpace", "DSpaceKey")
    private static final List<String> RESOURCE_NAMES = ["DSpace"].asImmutable()

    private String gridSiteId;
    private String resourceKeyValue;

    @Transient
    QName getResourceKeyName() { RESOURCE_KEY_NAME }
    @Transient
    List<String> getResourceNames() { RESOURCE_NAMES }

    @Column(name="key_site", nullable=true, updatable=false, columnDefinition="CHAR", length=64)
    String getGridSiteId() { gridSiteId }
    void setGridSiteId(final String newSiteId) { gridSiteId = newSiteId }

    @Column(name="key_val", nullable=false, updatable=false, columnDefinition="CHAR", length=36)
    String getResourceKeyValue() { resourceKeyValue }
    void setResourceKeyValue(final String newValue) { resourceKeyValue = newValue }
}
