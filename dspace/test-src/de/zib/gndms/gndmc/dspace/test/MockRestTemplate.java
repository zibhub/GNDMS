package de.zib.gndms.gndmc.dspace.test;

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

import org.springframework.http.*;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.util.Map;
import java.util.Set;

/**
 * A mockup for a rest template.
 * 
 * @author Ulrike Golas
 */

public class MockRestTemplate implements RestOperations {
	/**
	 * The service url needed to verify the url.
	 */
	private String serviceURL;

	@Override
	public final <T> T getForObject(final String url, final Class<T> responseType,
			final Object... uriVariables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final <T> T getForObject(final String url, final Class<T> responseType,
			final Map<String, ?> uriVariables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final <T> T getForObject(final URI url, final Class<T> responseType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final <T> ResponseEntity<T> getForEntity(final String url,
			final Class<T> responseType, final Object... uriVariables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final <T> ResponseEntity<T> getForEntity(final String url,
			final Class<T> responseType, final Map<String, ?> uriVariables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final <T> ResponseEntity<T> getForEntity(final URI url, final Class<T> responseType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final HttpHeaders headForHeaders(final String url, final Object... uriVariables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final HttpHeaders headForHeaders(final String url, final Map<String, ?> uriVariables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final HttpHeaders headForHeaders(final URI url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final URI postForLocation(final String url, final Object request,
			final Object... uriVariables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final URI postForLocation(final String url, final Object request,
			final Map<String, ?> uriVariables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final URI postForLocation(final URI url, final Object request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final <T> T postForObject(final String url, final Object request,
			final Class<T> responseType, final Object... uriVariables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final <T> T postForObject(final String url, final Object request,
			final Class<T> responseType, final Map<String, ?> uriVariables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final <T> T postForObject(final URI url, final Object request, final Class<T> responseType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final <T> ResponseEntity<T> postForEntity(final String url, final Object request,
			final Class<T> responseType, final Object... uriVariables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final <T> ResponseEntity<T> postForEntity(final String url, final Object request,
			final Class<T> responseType, final Map<String, ?> uriVariables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final <T> ResponseEntity<T> postForEntity(final URI url, final Object request,
			final Class<T> responseType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(final String url, final Object request, final Object... uriVariables) {
		// TODO Auto-generated method stub

	}

	@Override
	public void put(final String url, final Object request, final Map<String, ?> uriVariables) {
		// TODO Auto-generated method stub

	}

	@Override
	public void put(final URI url, final Object request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(final String url, final Object... uriVariables) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(final String url, final Map<String, ?> uriVariables) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(final URI url) {
		// TODO Auto-generated method stub

	}

	@Override
	public final Set<HttpMethod> optionsForAllow(final String url, final Object... uriVariables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final Set<HttpMethod> optionsForAllow(final String url,
			final Map<String, ?> uriVariables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final Set<HttpMethod> optionsForAllow(final URI url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final <T> ResponseEntity<T> exchange(final String url, final HttpMethod method,
			final HttpEntity<?> requestEntity, final Class<T> responseType,
			final Object... uriVariables) {
		
		if (!validUrlMethod(url, method)) {
			throw new RestClientException("Invalid http request for this url");
		}
        return new ResponseEntity<T>(HttpStatus.OK);
	}

	@Override
	public final <T> ResponseEntity<T> exchange(final String url, final HttpMethod method,
			final HttpEntity<?> requestEntity, final Class<T> responseType,
			final Map<String, ?> uriVariables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final <T> ResponseEntity<T> exchange(final URI url, final HttpMethod method,
			final HttpEntity<?> requestEntity, final Class<T> responseType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final <T> T execute(final String url, final HttpMethod method,
			final RequestCallback requestCallback,
			final ResponseExtractor<T> responseExtractor, final Object... uriVariables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final <T> T execute(final String url, final HttpMethod method,
			final RequestCallback requestCallback,
			final ResponseExtractor<T> responseExtractor, final Map<String, ?> uriVariables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final <T> T execute(final URI url, final HttpMethod method,
			final RequestCallback requestCallback,
			final ResponseExtractor<T> responseExtractor) {
		// TODO Auto-generated method stub
		return null;
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
	 * Sets the service URL.
	 * 
	 * @param serviceURL The service url.
	 */
	public final void setServiceURL(final String serviceURL) {
		this.serviceURL = serviceURL;
	}

	/**
	 * Checks if the given HTTP method is allowed to be executed on the given url.
	 * 
	 * @param url The url the method shall be executed.
	 * @param method The HTTP method.
	 * @return true, if the method can be executed on the url, else false.
	 */
	private boolean validUrlMethod(final String url, final HttpMethod method) {
		if (url.matches(serviceURL + "/dspace")) {
			if (method.equals(HttpMethod.GET)) {
				return true;
			}
		}

		if (url.matches(serviceURL + "/dspace/_\\w+")) {
			if (method.equals(HttpMethod.GET) || method.equals(HttpMethod.PUT) || method.equals(HttpMethod.DELETE)) {
				return true;
			}
		}

		if (url.matches(serviceURL + "/dspace/_\\w+/config")) {
			if (method.equals(HttpMethod.GET) || method.equals(HttpMethod.PUT)) {
				return true;
			}
		}

		if (url.matches(serviceURL + "/dspace/_\\w+/slicekinds")) {
			if (method.equals(HttpMethod.GET)) {
				return true;
			}
		}

		if (url.matches(serviceURL + "/dspace/_\\w+/_\\w+")) {
			if (method.equals(HttpMethod.GET) || method.equals(HttpMethod.PUT) 
					|| method.equals(HttpMethod.DELETE) || method.equals(HttpMethod.POST)) {
				return true;
			}
		}
		
		if (url.matches(serviceURL + "/dspace/_\\w+/_\\w+/_\\w+")) {
			if (method.equals(HttpMethod.GET) || method.equals(HttpMethod.PUT) 
					|| method.equals(HttpMethod.DELETE) || method.equals(HttpMethod.POST)) {
				return true;
			}
		}

		if (url.matches(serviceURL + "/dspace/_\\w+/_\\w+/_\\w+/files")) {
			if (method.equals(HttpMethod.GET) || method.equals(HttpMethod.DELETE)) {
				return true;
			}
		}

		if (url.matches(serviceURL + "/dspace/_\\w+/_\\w+/_\\w+/gsiftp")) {
			if (method.equals(HttpMethod.GET)) {
				return true;
			}
		}

		if (url.matches(serviceURL + "/dspace/_\\w+/_\\w+/_\\w+/_\\w+")) {
			if (method.equals(HttpMethod.GET) || method.equals(HttpMethod.PUT) || method.equals(HttpMethod.DELETE)) {
				return true;
			}
		}		
		return false;
	}
}
