package de.zib.gndms.model.common;

import de.zib.gndms.model.ModelObject;
import javax.persistence.Embeddable;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract superclass of "virtual" endpoint references.
 *
 * Points to a resource on a runtime-resolved GridSite.
 *
 * Subclasses specialize by resource key type.
 *
 * No version field since instances are immutable
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 28.07.2008 Time: 14:34:05
 */
@Embeddable
abstract class VEPRef extends ModelObject {

    abstract String getGridSiteId()
    abstract void setGridSiteId(final String newSiteId)

    @NotNull
    abstract List<String> getResourceNames();
}
