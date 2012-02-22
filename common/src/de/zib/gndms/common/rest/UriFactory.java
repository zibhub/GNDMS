package de.zib.gndms.common.rest;
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
import java.util.Map;

/**
 * @author try ma ik jo rr a zib
 * @date 03.03.11  18:23
 * @brief A factory for GNDMS-resource URIs.
 *
 * Methods of this class require a map for the path variables and are generating uri templates for different elements.
 */
public class UriFactory {

    /**
     * The key for a service.
     */
    public static final String BASE_URL = "baseUrl";

	/**
	 * The key for a service.
	 */
    public static final String SERVICE = "service";
    /**
     * The key for a taskflow type.
     */
    public static final String TASKFLOW_TYPE = "type";
    /**
     * The key for a taskflow id.
     */
    public static final String TASKFLOW_ID = "id";
    /**
     * The key for a quote id.
     */
    public static final String QUOTE_ID = "idx";
    /**
     * The key for a task ide.
     */
    public static final String TASK_ID = "taskId";
    /**
     * The key for a subspace.
     */
    public static final String SUBSPACE = "subspace";
    /**
     * The key for a slice kind.
     */
    public static final String SLICE_KIND = "sliceKind";
    /**
     * The key for a slice.
     */
    public static final String SLICE = "sliceId";

    /**
     * The base url, something like \c http://my.host.org/gndms/grid_id..
     */
    private String baseUrl;
    /**
     * The template for a service.
     */
    private UriTemplate serviceTemplate;
    /**
     * The template for a task flow.
     */ 
    private UriTemplate taskFlowTemplate;
    /**
     * The template for a task flow type.
     */
    private UriTemplate taskFlowTypeTemplate;
    /**
     * The template for a quote.
     */
    private UriTemplate quoteTemplate;
    /**
     * The template for a task.
     */
    private UriTemplate taskTemplate;
    /**
     * The template for a task service.
     */
    private UriTemplate taskServiceTemplate;
    /**
     * The template for a subspace.
     */
    private UriTemplate subspaceTemplate;
    private UriTemplate sliceKindTemplate;
    private UriTemplate sliceTemplate;
    private static final String SERVICE_TEMPLATE = "/{service}";
    private static final String TASK_FLOW_TEMPLATE = "/{service}/_{type}/_{id}";
    private static final String TASK_FLOW_TYPE_TEMPLATE = "/{service}/_{type}";
    private static final String QUOTE_TEMPATE = "/{service}/_{type}/_{id}/quotes/{idx}";
    private static final String TASK_TEMPLATE = "/{service}/tasks/_{taskId}";
    private static final String TASK_SERVICE_TEMPLATE = "/{service}/tasks";
    private static final String SUBSPACE_TEMPLATE = "/{service}/_{subspace}";
    private static final String SLICE_KIND_TEMPLATE = "/{service}/_{subspace}/_{sliceKind}";
    private static final String SLICE_TEMPLATE = "/{service}/_{subspace}/_{sliceKind}/_{sliceId}";


    /**
     * The constructor.
     */
    public UriFactory() {
        this.baseUrl = "";
        init();
    }

    /**
     * The constructor.
     * @param baseUrl The base url.
     */
    public UriFactory( final String baseUrl ) {
        this.baseUrl = baseUrl;
        init();
    }

    /**
     * Initialization of the templates.
     */
    private void init( ) {
        serviceTemplate = new UriTemplate(baseUrl + SERVICE_TEMPLATE );
        taskFlowTemplate = new UriTemplate( baseUrl + TASK_FLOW_TEMPLATE );
        taskFlowTypeTemplate = new UriTemplate( baseUrl + TASK_FLOW_TYPE_TEMPLATE );
        quoteTemplate = new UriTemplate( baseUrl + QUOTE_TEMPATE );
        taskTemplate = new UriTemplate( baseUrl + TASK_TEMPLATE );
        taskServiceTemplate = new UriTemplate( baseUrl + TASK_SERVICE_TEMPLATE );
        subspaceTemplate = new UriTemplate(baseUrl + SUBSPACE_TEMPLATE );
        sliceKindTemplate = new UriTemplate(baseUrl + SLICE_KIND_TEMPLATE );
        sliceTemplate = new UriTemplate( baseUrl + SLICE_TEMPLATE );
    }

    /**
     * Returns the uri for a service, expanding the variables as given in the map.
     * @param vars A map, which should contain the key "service".
     * @return The corresponding uri.
     */
    public final String serviceUri(final Map<String, String> vars) {
        return serviceTemplate.expand(vars).toString();
    }
    
    /**
     * Returns the uri for a task flow, expanding the variables as given in the map, followed by the given facet.
     * @param vars A map, which should contain the keys "service", "type", and "id".
     * @param facet The facet.
     * @return The corresponding uri.
     */
    public final String taskFlowUri( final Map<String, String> vars, final String facet ) {
        return addFacet( taskFlowTemplate.expand( vars ), facet );
    }

    /**
     * Returns the uri for a task flow type, expanding the variables as given in the map, followed by the given facet.
     * @param vars A map, which should contain the keys "service" and "type".
     * @param facet The facet.
     * @return The corresponding uri.
     */
    public final String taskFlowTypeUri( final Map<String, String> vars, final String facet ) {
        return addFacet( taskFlowTypeTemplate.expand( vars ), facet );
    }

    /**
     * Returns the uri for a quote, expanding the variables as given in the map.
     * @param urimap A map, which should contain the keys "service", "type", "id", and "idx".
     * @return The corresponding uri.
     */
    public final String quoteUri( final Map<String, String> urimap ) {
        return quoteTemplate.expand( urimap ).toString();
    }

    /**
     * Returns the uri for a task, expanding the variables as given in the map, followed by the given facet.
     * @param urimap A map, which should contain the keys "service" and "taskId".
     * @param facet The facet.
     * @return The corresponding uri.
     */
    public final String taskUri( final Map<String, String> urimap, final String facet ) {
        return addFacet( taskTemplate.expand( urimap ), facet );
    }

    /**
     * Returns the uri for a task service, expanding the variables as given in the map.
     * @param urimap A map, which should contain the key "service".
     * @return The corresponding uri.
     */
    public final String taskServiceUri( final Map<String, String> urimap ) {
        return taskServiceTemplate.expand( urimap ).toString();
    }

    /**
     * Returns the uri for a task service, expanding the variables as given in the map, followed by the given facet.
     * @param urimap A map, which should contain the key "service".
     * @param facet The facet.
     * @return The corresponding uri.
     */
    public final String taskServiceUri( final Map<String, String> urimap, final String facet ) {
        return addFacet( taskServiceTemplate.expand( urimap ), facet );
    }

    /**
     * Returns the uri for a subspace, expanding the variables as given in the map, followed by the given facet.
     * @param vars A map, which should contain the keys "service" and "subspace".
     * @param facet The facet.
     * @return The corresponding uri.
     */
    public final String subspaceUri(final Map<String, String> vars, final String facet) {
        return addFacet(subspaceTemplate.expand(vars), facet);
    }

    public final String sliceKindUri(final Map<String, String> vars, final String facet) {
        return addFacet(sliceKindTemplate.expand(vars), facet);
    }

    public final String sliceUri( final Map< String, String > vars, final String facet ) {
        return addFacet( sliceTemplate.expand( vars ), facet );
    }

    /**
     * Add "/" and the name of the given facet to an uri, if this facet is not null.
     * @param uri The uri.
     * @param facet The facet.
     * @return The uri followed by the facet.
     */
    private String addFacet( final URI uri, final String facet ) {
        if ( facet != null ) {
            return uri.toString() + "/" + facet;
        } else {
            return uri.toString();
        }
    }

    /**
     * Returns the base url.
     * @return The base url.
     */
    public final String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Sets the base url.
     * @param baseUrl The base url.
     */
    public final void setBaseUrl( final String baseUrl ) {
        this.baseUrl = baseUrl;
        init();
    }


    /**
     * Creates a vaild slice kind specifier
     *
     * Both uriMap and url are populated given the provided information.
     *
     * @param baseUrl The service base url (including gndms/_grid-name_)
     * @param subspace The subspace name
     * @param sliceKind The slice kind id
     *
     * @return A slice kind specifier
     */
    public static Specifier<Void> createSliceKindSpecifier( final String baseUrl,
                                                 final String subspace, final String sliceKind ) {

        Specifier<Void> specifier = new Specifier<Void>();
        specifier.addMapping( BASE_URL, baseUrl );
        specifier.addMapping( SERVICE, "dspace" );
        specifier.addMapping( SUBSPACE, subspace );
        specifier.addMapping( SLICE_KIND, sliceKind );

        specifier.setUrl(  new UriTemplate( baseUrl + SLICE_KIND_TEMPLATE ).expand( specifier
                .getUriMap() ).toString() );

        return specifier;
    }
}


