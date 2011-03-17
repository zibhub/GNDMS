package de.zib.gndms.rest;
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

import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author try ma ik jo rr a zib
 * @date 03.03.11  18:23
 * @brief A factory GNDMS-resource URIs.
 *
 * Methods of this class require a map for the path variables and are generating
 */
public class UriFactory {

    private String baseUrl;
    private UriTemplate taskFlowTemplate;
    private UriTemplate taskFlowTypeTemplate;
    private UriTemplate quoteTemplate;
    private UriTemplate taskTemplate;
    private UriTemplate taskServiceTemplate;


    public UriFactory() {
    }


    public UriFactory( String baseUrl ) {
        this.baseUrl = baseUrl;
        init();
    }


    private void init( ) {
        taskFlowTemplate = new UriTemplate ( baseUrl +"/{service}/_{type}/_{id}" );
        taskFlowTypeTemplate = new UriTemplate( baseUrl +"/{service}/_{type}" );
        quoteTemplate = new UriTemplate ( baseUrl +"/{service}/_{type}/_{id}/quotes/{idx}" );
        taskTemplate = new UriTemplate ( baseUrl +"/{service}/tasks/_{taskId}" );
        taskServiceTemplate = new UriTemplate ( baseUrl +"/{service}/tasks" );
    }


    public String taskFlowUri( Map<String, String> vars, String facet ) {
        return addFacet ( taskFlowTemplate.expand( vars ), facet );
    }


    public String taskFlowTypeUri( Map<String, String> vars, String facet ) {
        return addFacet ( taskFlowTypeTemplate.expand( vars ), facet );
    }


    public String quoteUri( Map<String,String> urimap ) {
        return quoteTemplate.expand( urimap ).toString();
    }


    public String taskUri( Map<String,String> urimap, String facet ) {
        return addFacet( taskTemplate.expand( urimap ), facet );
    }

    public String taskServiceUri( Map<String,String> urimap ) {
        return taskServiceTemplate.expand( urimap ).toString();
    }

    public String taskServiceUri( Map<String,String> urimap, String facet ) {
        return addFacet( taskServiceTemplate.expand( urimap ), facet );
    }

    private String addFacet( URI uri, String facet ) {
        if( facet != null )
            return uri.toString() + "/" + facet;
        else
            return uri.toString();
    }


    public String getBaseUrl() {
        return baseUrl;
    }


    public void setBaseUrl( String baseUrl ) {
        this.baseUrl = baseUrl;
        init();
    }
}


