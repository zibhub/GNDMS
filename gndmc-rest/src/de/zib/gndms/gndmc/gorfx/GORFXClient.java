package de.zib.gndms.gndmc.gorfx;

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

import de.zib.gndms.common.GORFX.service.GORFXServiceEssentials;
import de.zib.gndms.common.model.gorfx.types.Order;
import de.zib.gndms.common.model.gorfx.types.TaskFlowInfo;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.gndmc.AbstractClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * @author try ma ik jo rr a zib
 * @brief Client for the taskflow part of the gorfx service.
 * <p/>
 * This client doesn't provide methods for batch execution and service configuration
 * only the methods necessary for taskflow execution are exposed.
 * @see de.zib.gndms.common.GORFX.service.GORFXServiceEssentials for details.
 */
public class GORFXClient extends AbstractClient implements GORFXServiceEssentials {


    /**
     * The constructor.
     */
    public GORFXClient() {

    }


    /**
     * The constructor.
     *
     * @param serviceURL The base url of the grid.
     */
    public GORFXClient( final String serviceURL ) {

        this.setServiceURL( serviceURL );
    }


    public final ResponseEntity<Facets> listAvailableFacets( final String dn ) {

        return unifiedGet( Facets.class, getServiceURL() + "/gorfx/", dn );
    }


    @SuppressWarnings( "unchecked" )
    public final ResponseEntity<List<String>> listTaskFlows( final String dn ) {

        return ( ResponseEntity<List<String>> ) ( Object ) unifiedGet( List.class,
                getServiceURL() + "/gorfx/taskflows/", dn );
    }


    public final ResponseEntity<TaskFlowInfo> getTaskFlowInfo( final String type, final String dn ) {

        return unifiedGet( TaskFlowInfo.class, getServiceURL() + "/gorfx/_" + type, dn );
    }


    @SuppressWarnings( "unchecked" )
    public final ResponseEntity<Specifier<Facets>> createTaskFlow( final String type, final Order order,
                                                                   final String dn, final String wid, MultiValueMap<String, String> context ) {

        GNDMSResponseHeader requestHeaders = new GNDMSResponseHeader();
        if ( dn != null ) {
            requestHeaders.setDN( dn );
        }
        if ( wid != null ) {
            requestHeaders.setWId( wid );
        }

        requestHeaders.putAll( context );
        HttpEntity<Order> requestEntity = new HttpEntity<Order>( order, requestHeaders );

        return ( ResponseEntity<Specifier<Facets>> ) ( Object )
                getRestTemplate().exchange( getServiceURL()
                        + "/gorfx/_" + type, HttpMethod.POST, requestEntity, Specifier.class );
    }

}
