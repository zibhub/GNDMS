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
import org.springframework.web.client.RestTemplate;

import de.zib.gndms.rest.GNDMSResponseHeader;

/**
 * An abstract client which implements the HTTP requests.
 * 
 * @author Ulrike Golas
 *
 */
public abstract class AbstractClient {

    // le logger
    protected Logger logger = LoggerFactory.getLogger( this.getClass() );


	/**
	 * A rest template for internal use.
	 */
	private RestTemplate restTemplate;


	/**
	 * The service url like <code>http://www.barz.org/gndms/<gridname></code>.
	 */
	private String serviceURL;


    protected AbstractClient() {
    }


    public AbstractClient( String serviceURL ) {
        this.serviceURL = serviceURL;
    }


    private <T,P> ResponseEntity<T> unifiedX(HttpMethod x, final Class<T> clazz, final P parm,
			final String url, final String dn, final String wid){
		GNDMSResponseHeader requestHeaders = new GNDMSResponseHeader();
        if (dn != null)
            requestHeaders.setDN(dn);

		if (wid != null)
			requestHeaders.setWId(wid);

		HttpEntity<P> requestEntity = new HttpEntity<P>(parm, requestHeaders);
		return restTemplate.exchange(url, x, requestEntity, clazz);
	}
	
	protected final <T> ResponseEntity<T> unifiedGet(final Class<T> clazz, final String url,
			final String dn) {
		return unifiedX(HttpMethod.GET, clazz, null, url, dn, null);
	}

	protected final <T> ResponseEntity<T> unifiedGet(final Class<T> clazz, final String url,
			final String dn, final String wid) {
		return unifiedX(HttpMethod.GET, clazz, null, url, dn, wid);
	}

	protected final <T, P> ResponseEntity<T> unifiedPost(final Class<T> clazz, final P parm,
			final String url, final String dn) {
		return unifiedX(HttpMethod.POST, clazz, parm, url, dn, null);
	}

	protected final <T, P> ResponseEntity<T> unifiedPost(final Class<T> clazz, final P parm,
			final String url, final String wid, final String dn) {
		return unifiedX(HttpMethod.POST, clazz, parm, url, dn, wid);
	}

	protected final <T, P> ResponseEntity<T> unifiedPut(final Class<T> clazz, final P parm,
			final String url, final String dn) {
		return unifiedX(HttpMethod.PUT, clazz, parm, url, dn, null);
	}

	protected final <T, P> ResponseEntity<T> unifiedPut(final Class<T> clazz, final P parm,
			final String url, final String dn, final String wid) {
		return unifiedX(HttpMethod.PUT, clazz, parm, url, dn, wid);
	}

	protected final ResponseEntity<Void> unifiedDelete(final String url, final String dn) {
		return unifiedX(HttpMethod.DELETE, Void.class, null, url, dn, null);
	}

	protected final ResponseEntity<Void> unifiedDelete(final String url, final String dn, final String wid) {
		return unifiedX(HttpMethod.DELETE, Void.class, null, url, dn, wid);
	}
	
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

    @Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

}
