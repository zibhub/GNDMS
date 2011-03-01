package de.zib.gndms.gndmc.gorfx;

import java.util.List;

import de.zib.gndms.model.gorfx.types.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import de.zib.gndms.GORFX.service.TaskFlowService;
import de.zib.gndms.model.gorfx.types.TaskResult;
import de.zib.gndms.rest.Facets;
import de.zib.gndms.rest.GNDMSResponseHeader;

public class TaskFlowClient implements TaskFlowService {

	// A rest template for internal use
	private RestTemplate restTemplate; ///< Just for internal use.
	// The service url like http://www.barz.org/gndms/<gridname>
    private String serviceURL; 
    
	public TaskFlowClient() {
	}
	
	public TaskFlowClient(String serviceURL){
		this.serviceURL = serviceURL;
	}
	
	public ResponseEntity<Facets> getFacets(String type, String id, String dn) {
        return unifiedGet( Facets.class, serviceURL + "/gorfx/" + type + "/" + id, dn );
	}

	public ResponseEntity<Void> deleteTaskflow(String type, String id, String dn) {
        return unifiedDelete( serviceURL + "/gorfx/" + type + "/" + id, dn );
	}

	public ResponseEntity<AbstractTF> getOrder(String type, String id, String dn) {
        return unifiedGet( AbstractTF.class, serviceURL + "/gorfx/" + type + "/" + id + "/order", dn );
	}

	public ResponseEntity<Void> setOrder(String type, String id, AbstractTF orq, String dn) {
        return unifiedPut( Void.class, orq, serviceURL + "/gorfx/" + type + "/" + id + "/order", dn );
	}

	public ResponseEntity<List<Quote>> getQuotes(String type, String id, String dn) {
		return ( ResponseEntity<List<Quote>> ) (Object) unifiedGet( List.class, serviceURL + "/gorfx/" + type + "/" + id + "/quote", dn );
	}

	public ResponseEntity<Void> setQuote(String type, String id, Quote cont, String dn) {
        return unifiedPut( Void.class, cont, serviceURL + "/gorfx/" + type + "/" + id + "/quote", dn );
	}

	public ResponseEntity<Quote> getQuote(String type, String id, int idx, String dn) {
        return unifiedGet( Quote.class, serviceURL + "/gorfx/" + type + "/" + id + "/quote/" + idx, dn );
	}

	public ResponseEntity<Void> deleteQuotes(String type, String id, int idx, String dn) {
        return unifiedDelete( serviceURL + "/gorfx/" + type + "/" + id + "/quote/" + idx, dn );
	}

	public ResponseEntity<String> getTask(String type, String id, String dn) {
        return unifiedGet( String.class, serviceURL + "/gorfx/" + type + "/" + id + "/task", dn );
	}

	public ResponseEntity<String> createTask(String type, String id,
			String quoteId, String dn) {
        return unifiedPut( String.class, quoteId, serviceURL + "/gorfx/" + type + "/" + id + "/task", dn );
	}

	public ResponseEntity<TaskStatus> getStatus(String type, String id, String dn) {
		return unifiedGet( TaskStatus.class, serviceURL + "/gorfx/" + type + "/" + id + "/status", dn );
	}

	public ResponseEntity<TaskResult> getResult(String type, String id, String dn) {
		return unifiedGet( TaskResult.class, serviceURL + "/gorfx/" + type + "/" + id + "/result", dn );
	}

	public ResponseEntity<TaskFlowFailure> getErrors(String type, String id, String dn) {
		return unifiedGet( TaskFlowFailure.class, serviceURL + "/gorfx/" + type + "/" + id + "/errors", dn );
	}

    protected <T> ResponseEntity<T> unifiedGet( Class<T> clazz, String url, String dn ) {
        GNDMSResponseHeader requestHeaders = new GNDMSResponseHeader();
        requestHeaders.setDN( dn );
        HttpEntity<?> requestEntity = new HttpEntity(requestHeaders);
        return restTemplate.exchange( url, HttpMethod.GET, requestEntity, clazz );
    }

    protected <T,P> ResponseEntity<T> unifiedPost( Class<T> clazz, P parm, String url, HttpHeaders headers ) {
        HttpEntity<P> requestEntity = new HttpEntity<P>(parm, headers );
        return restTemplate.exchange( url, HttpMethod.POST, requestEntity, clazz );
    }

    protected <T,P> ResponseEntity<T> unifiedPut( Class<T> clazz, P parm, String url, String dn ) {
        GNDMSResponseHeader requestHeaders = new GNDMSResponseHeader();
        requestHeaders.setDN( dn );
        HttpEntity<P> requestEntity = new HttpEntity<P>(parm, requestHeaders );
        return restTemplate.exchange( url, HttpMethod.PUT, requestEntity, clazz );
    }

    protected ResponseEntity<Void> unifiedDelete( String url, String dn ) {
        GNDMSResponseHeader requestHeaders = new GNDMSResponseHeader();
        requestHeaders.setDN( dn );
        HttpEntity<?> requestEntity = new HttpEntity(requestHeaders);
        return restTemplate.exchange( url, HttpMethod.DELETE, requestEntity, Void.class );
    }

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

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
