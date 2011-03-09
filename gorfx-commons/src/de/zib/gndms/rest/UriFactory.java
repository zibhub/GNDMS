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

import java.util.HashMap;
import java.util.Map;

/**
 * @author try ma ik jo rr a zib
 * @date 03.03.11  18:23
 * @brief
 */
public class UriFactory {

    private String baseUrl;
    private UriTemplate taskFlowTemplate;
    private UriTemplate taskFlowTypeTemplate;
    private UriTemplate quoteTemplate;
    private UriTemplate taskTemplate;


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
        taskTemplate = new UriTemplate ( baseUrl +"/{service}/task/_{taskId}" );
    }


    public String taskFlowUri( Map<String, String> vars, String facet ) {
        return taskFlowTemplate.expand( vars ).toString() + "/" +facet;
    }


    public String taskFlowTypeUri( Map<String, String> vars, String facet ) {
        return taskFlowTypeTemplate.expand( vars ).toString() + "/" +facet;
    }


    public String quoteUri( HashMap<String,String> urimap ) {
        return quoteTemplate.expand( urimap ).toString();
    }


    public String taskUri( HashMap<String,String> urimap, String facet ) {
        return taskTemplate.expand( urimap ).toString() + "/" + facet;
    }


    public String getBaseUrl() {
        return baseUrl;
    }


    public void setBaseUrl( String baseUrl ) {
        this.baseUrl = baseUrl;
        init();
    }
}

