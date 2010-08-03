package de.zib.gndms.model.dspace;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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



import de.zib.gndms.model.common.SimpleRKRef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * VEPRef to a Subspace instance
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 * User: stepn Date: 30.07.2008 Time: 15:01:58
 */
@Embeddable
public class SubspaceRef extends SimpleRKRef {

    private static QName RESOURCE_KEY_NAME =
        new QName("http://dspace.gndms.zib.de/DSpace/Subspace", "SubspaceKey");

    private static final List<String> RESOURCE_NAMES = new ArrayList<String>(2) {{
        add( "DSpace" ); add( "Subspace" ); }};

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
