package de.zib.gndms.gndmc.dspace;

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

import java.net.URI;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

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
	public <T> T getForObject(String url, Class<T> responseType,
			Object... uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getForObject(String url, Class<T> responseType,
			Map<String, ?> uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getForObject(URI url, Class<T> responseType)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> ResponseEntity<T> getForEntity(String url,
			Class<T> responseType, Object... uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> ResponseEntity<T> getForEntity(String url,
			Class<T> responseType, Map<String, ?> uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpHeaders headForHeaders(String url, Object... uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpHeaders headForHeaders(String url, Map<String, ?> uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpHeaders headForHeaders(URI url) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URI postForLocation(String url, Object request,
			Object... uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URI postForLocation(String url, Object request,
			Map<String, ?> uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URI postForLocation(URI url, Object request)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T postForObject(String url, Object request,
			Class<T> responseType, Object... uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T postForObject(String url, Object request,
			Class<T> responseType, Map<String, ?> uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T postForObject(URI url, Object request, Class<T> responseType)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> ResponseEntity<T> postForEntity(String url, Object request,
			Class<T> responseType, Object... uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> ResponseEntity<T> postForEntity(String url, Object request,
			Class<T> responseType, Map<String, ?> uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> ResponseEntity<T> postForEntity(URI url, Object request,
			Class<T> responseType) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(String url, Object request, Object... uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub

	}

	@Override
	public void put(String url, Object request, Map<String, ?> uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub

	}

	@Override
	public void put(URI url, Object request) throws RestClientException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String url, Object... uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String url, Map<String, ?> uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(URI url) throws RestClientException {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<HttpMethod> optionsForAllow(String url, Object... uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HttpMethod> optionsForAllow(String url,
			Map<String, ?> uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HttpMethod> optionsForAllow(URI url) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> ResponseEntity<T> exchange(String url, HttpMethod method,
			HttpEntity<?> requestEntity, Class<T> responseType,
			Object... uriVariables) throws RestClientException {
		
		if (!validUrlMethod(url, method)) {
			throw new RestClientException("Invalid http request for this url");
		}
		ResponseEntity<T> res = new ResponseEntity<T>(HttpStatus.OK);		
		return res;
	}

	@Override
	public <T> ResponseEntity<T> exchange(String url, HttpMethod method,
			HttpEntity<?> requestEntity, Class<T> responseType,
			Map<String, ?> uriVariables) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> ResponseEntity<T> exchange(URI url, HttpMethod method,
			HttpEntity<?> requestEntity, Class<T> responseType)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T execute(String url, HttpMethod method,
			RequestCallback requestCallback,
			ResponseExtractor<T> responseExtractor, Object... uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T execute(String url, HttpMethod method,
			RequestCallback requestCallback,
			ResponseExtractor<T> responseExtractor, Map<String, ?> uriVariables)
			throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T execute(URI url, HttpMethod method,
			RequestCallback requestCallback,
			ResponseExtractor<T> responseExtractor) throws RestClientException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	private boolean validUrlMethod(String url, HttpMethod method) {
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
			if (method.equals(HttpMethod.GET) || method.equals(HttpMethod.PUT) || method.equals(HttpMethod.DELETE) || method.equals(HttpMethod.POST)) {
				return true;
			}
		}
		
		if (url.matches(serviceURL + "/dspace/_\\w+/_\\w+/_\\w+")) {
			if (method.equals(HttpMethod.GET) || method.equals(HttpMethod.PUT) || method.equals(HttpMethod.DELETE) || method.equals(HttpMethod.POST)) {
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
