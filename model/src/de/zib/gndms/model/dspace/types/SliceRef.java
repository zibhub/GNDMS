package de.zib.gndms.model.dspace.types;

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



import de.zib.gndms.model.common.SimpleRKRef;
import org.jetbrains.annotations.NotNull;

import javax.xml.namespace.QName;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


/**
 * This is the model for a slice reference.
 *
 * Use infra.GNDMSTools::SliceRefAsEPR to obtain a EPR for this ref.
 *
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 03.11.2008, Time: 10:30:48
 */
public class SliceRef extends SimpleRKRef implements Serializable {
    private static final long serialVersionUID = -970796595131309306L;

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
