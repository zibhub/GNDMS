package de.zib.gndms.gndmc;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

import de.zib.gndms.common.rest.GNDMSResponseHeader;

/**
 * An abstract client which implements the HTTP requests.
 * 
 * @author Ulrike Golas
 * 
 */
public abstract class AbstractClient {

	/**
	 * The logger.
	 */
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * A rest template for internal use.
	 */
	private RestOperations restTemplate;

	/**
	 * The service url like <code>http://www.barz.org/gndms/<gridname></code>.
	 */
	private String serviceURL;

	/**
	 * The constructor.
	 */
	protected AbstractClient() {
	}

	/**
	 * The constructor.
	 * 
	 * @param serviceURL
	 *            The base url of the grid.
	 */
	public AbstractClient(final String serviceURL) {
		this.serviceURL = serviceURL;
	}

	/**
	 * Executes a given HTTP method on a url.
	 * 
	 * The request header contains a given user name and workflow id, the body of the request contains 
	 * a given object of type P.
	 * 
	 * @param <T> The body type of the response.
	 * @param <P> The body type of the request.
	 * @param x The kind of HTTP method to be executed.
	 * @param clazz The type of the return value.
	 * @param parm The body of the request.
	 * @param url The url of the request.
	 * @param dn The user name.
	 * @param wid The workflow id.
	 * @return The response as entity.
	 */
	private <T, P> ResponseEntity<T> unifiedX(final HttpMethod x,
			final Class<T> clazz, final P parm, final String url,
			final String dn, final String wid) {
		GNDMSResponseHeader requestHeaders = new GNDMSResponseHeader();
		if (dn != null) {
			requestHeaders.setDN(dn);
		}
		if (wid != null) {
			requestHeaders.setWId(wid);
		}
		HttpEntity<P> requestEntity = new HttpEntity<P>(parm, requestHeaders);
		return restTemplate.exchange(url, x, requestEntity, clazz);
	}

	/**
	 * Executes a GET on a url, where the request header contains a given user name.
	 * 
	 * @param <T> The body type of the response.
	 * @param clazz The type of the return value.
	 * @param url The url of the request.
	 * @param dn The user name.
	 * @return The response as entity.
	 */
	protected final <T> ResponseEntity<T> unifiedGet(final Class<T> clazz,
			final String url, final String dn) {
		return unifiedX(HttpMethod.GET, clazz, null, url, dn, null);
	}

	/**
	 * Executes a GET on a url, where the request header contains a given user name and workflow id.
	 * 
	 * @param <T> The body type of the response.
	 * @param clazz The type of the return value.
	 * @param url The url of the request.
	 * @param dn The user name.
	 * @param wid The workflow id.
	 * @return The response as entity.
	 */
	protected final <T> ResponseEntity<T> unifiedGet(final Class<T> clazz,
			final String url, final String dn, final String wid) {
		return unifiedX(HttpMethod.GET, clazz, null, url, dn, wid);
	}

	/**
	 * Executes a POST on a url.
	 * 
	 * The request header contains a given user name, the body of the request contains 
	 * a given object of type P.
	 * 
	 * @param <T> The body type of the response.
	 * @param <P> The body type of the request.
	 * @param clazz The type of the return value.
	 * @param parm The body of the request.
	 * @param url The url of the request.
	 * @param dn The user name.
	 * @return The response as entity.
	 */
	protected final <T, P> ResponseEntity<T> unifiedPost(final Class<T> clazz,
			final P parm, final String url, final String dn) {
		return unifiedX(HttpMethod.POST, clazz, parm, url, dn, null);
	}

	/**
	 * Executes a POST on a url.
	 * 
	 * The request header contains a given user name and workflow id, the body of the request contains 
	 * a given object of type P.
	 * 
	 * @param <T> The body type of the response.
	 * @param <P> The body type of the request.
	 * @param clazz The type of the return value.
	 * @param parm The body of the request.
	 * @param url The url of the request.
	 * @param dn The user name.
	 * @param wid The workflow id.
	 * @return The response as entity.
	 */
	protected final <T, P> ResponseEntity<T> unifiedPost(final Class<T> clazz,
			final P parm, final String url, final String dn, final String wid) {
		return unifiedX(HttpMethod.POST, clazz, parm, url, dn, wid);
	}

	/**
	 * Executes a PUT on a url.
	 * 
	 * The request header contains a given user name, the body of the request contains 
	 * a given object of type P.
	 * 
	 * @param <T> The body type of the response.
	 * @param <P> The body type of the request.
	 * @param clazz The type of the return value.
	 * @param parm The body of the request.
	 * @param url The url of the request.
	 * @param dn The user name.
	 * @return The response as entity.
	 */
	protected final <T, P> ResponseEntity<T> unifiedPut(final Class<T> clazz,
			final P parm, final String url, final String dn) {
		return unifiedX(HttpMethod.PUT, clazz, parm, url, dn, null);
	}

	/**
	 * Executes a PUT on a url.
	 * 
	 * The request header contains a given user name and workflow id, the body of the request contains 
	 * a given object of type P.
	 * 
	 * @param <T> The body type of the response.
	 * @param <P> The body type of the request.
	 * @param clazz The type of the return value.
	 * @param parm The body of the request.
	 * @param url The url of the request.
	 * @param dn The user name.
	 * @param wid The workflow id.
	 * @return The response as entity.
	 */
	protected final <T, P> ResponseEntity<T> unifiedPut(final Class<T> clazz,
			final P parm, final String url, final String dn, final String wid) {
		return unifiedX(HttpMethod.PUT, clazz, parm, url, dn, wid);
	}

	/**
	 * Executes a DELETE on a url, where the request header contains a given user name.
	 * 
	 * @param url The url of the request.
	 * @param dn The user name.
	 * @return The response as entity with Void body.
	 */
	protected final ResponseEntity<Void> unifiedDelete(final String url,
			final String dn) {
		return unifiedX(HttpMethod.DELETE, Void.class, null, url, dn, null);
	}

	/**
	 * Executes a DELETE on a url, where the request header contains a given user name.
	 * 
	 * @param <T> The body type of the response.
	 * @param clazz The type of the return value.
	 * @param url The url of the request.
	 * @param dn The user name.
	 * @return The response as entity.
	 */
	protected final <T> ResponseEntity<T> unifiedDelete(final Class<T> clazz,
			final String url, final String dn) {
		return unifiedX(HttpMethod.DELETE, clazz, null, url, dn, null);
	}

	/**
	 * Executes a DELETE on a url, where the request header contains a given user name and workflow id.
	 * 
	 * @param url The url of the request.
	 * @param dn The user name.
	 * @param wid The workflow id.
	 * @return The response as entity with Void body.
	 */
	protected final ResponseEntity<Void> unifiedDelete(final String url,
			final String dn, final String wid) {
		return unifiedX(HttpMethod.DELETE, Void.class, null, url, dn, wid);
	}

	/**
	 * Gets the rest template.
	 * 
	 * @return The rest template.
	 */
	public final RestOperations getRestTemplate() {
		return restTemplate;
	}

	/**
	 * Sets the rest template.
	 * 
	 * @param restTemplate The rest template.
	 */
	@Autowired
	public final void setRestTemplate(final RestOperations restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * Gets the service url.
	 * 
	 * @return The service url.
	 */
	public final String getServiceURL() {
		return serviceURL;
	}

	/**
	 * Sets the service url.
	 * 
	 * @param serviceURL The service url.
	 */
	public void setServiceURL(final String serviceURL) {
		this.serviceURL = serviceURL;
	}

}
