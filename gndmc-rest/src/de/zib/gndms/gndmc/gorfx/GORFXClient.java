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

import de.zib.gndms.GORFX.service.GORFXServiceEssentials;
import de.zib.gndms.model.gorfx.types.AbstractTF;
import de.zib.gndms.model.gorfx.types.TaskFlowInfo;
import de.zib.gndms.rest.Facets;
import de.zib.gndms.rest.GNDMSResponseHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author try ma ik jo rr a zib
 * @date 09.02.11, Time: 14:20
 *
 * @brief
 */
public class GORFXClient implements GORFXServiceEssentials {

    private RestTemplate restTemplate; ///< Just for internal use.
    private String serviceURL; ///< The service url like http://www.barz.org/gndms/<gridname>
                               /// \note no gorfx foo in the url


    public GORFXClient() {
    }


    public GORFXClient( String serviceURL ) {
        this.serviceURL = serviceURL;
    }


    public ResponseEntity<Facets> listAvailableFacets( String dn ) {
        return unifiedGet( Facets.class, serviceURL + "/gorfx", dn );
    }



    public ResponseEntity<List<String>> listTaskFlows( String dn ) {
        return ( ResponseEntity<List<String>> ) (Object) unifiedGet( List.class, serviceURL + "/gorfx/taskflows", dn );
    }


    public ResponseEntity<TaskFlowInfo> getTaskFlowInfo( String type, String dn ) {
        return unifiedGet( TaskFlowInfo.class, serviceURL + "/gorfx/" + type, dn );
    }


    public ResponseEntity<String> createTaskFlow( String type, AbstractTF order, String dn, String wid ) {
        GNDMSResponseHeader requestHeaders = new GNDMSResponseHeader();
        requestHeaders.setDN( dn );
        requestHeaders.setWid( wid );
        return unifiedPost( String.class, AbstractTF.class, serviceURL + "/gorfx/" + type, requestHeaders );
    }


    protected <T> ResponseEntity<T> unifiedGet( Class<T> clazz, String url, String dn ) {
        GNDMSResponseHeader requestHeaders = new GNDMSResponseHeader();
        requestHeaders.setDN( dn );
        HttpEntity<?> requestEntity = new HttpEntity(requestHeaders);
        return restTemplate.exchange( url, HttpMethod.GET, requestEntity, clazz );
    }


    protected <T,P> ResponseEntity<T> unifiedPost( Class<T> resClazz, P parm, String url, HttpHeaders headers ) {
        HttpEntity<P> requestEntity = new HttpEntity<P>(parm, headers );
        return restTemplate.exchange( url, HttpMethod.POST, requestEntity, resClazz );
    }

    public String getServiceURL() {
        return serviceURL;
    }


    public void setServiceURL( String serviceURL ) {
        this.serviceURL = serviceURL;
    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }


    @Autowired
    public void setRestTemplate( RestTemplate restTemplate ) {
        System.out.println( "ijecting restTemplate" );
        this.restTemplate = restTemplate;
    }
}
