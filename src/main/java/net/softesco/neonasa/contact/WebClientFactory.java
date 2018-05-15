package net.softesco.neonasa.contact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

/**
 * Factory class for web clients of REST services
 * with NEO REST API as default.
 * 
 * @author cristi
 */
public class WebClientFactory {

	private static Logger logger = LoggerFactory.getLogger(WebClientFactory.class);
	
	public static final String NEO_REST_V1 = "https://api.nasa.gov/neo/rest/v1";
	// my registered access key: (instead of DEMO_KEY, with limited number of usages per hour and day)
	public static final String NEO_ACCESS_KEY = "Wbffpkrb6J8JNW01nXmUqT1Ql4NCbCRqTsIyH6VC";

	private WebClientFactory() {/* factory class, thus no instantiation */}
	
	/**
	 * WebClient factory method - uses NEO REST API as default
	 * @return WebClient for NEO
	 */
	public static WebClient createWebClient() {
		return createWebClient(NEO_REST_V1);
	}
	
	/**
	 * WebClient factory method for specified URL
	 * @param url of REST API
	 * @return WebClient for url
	 */
	public static WebClient createWebClient(String url) {
		WebClient webClient = WebClient.create(url);
		logger.info("Created WebClient for REST API: " + url);
		return webClient;
	}
	
	/**
	 * Retrieve JSON response from relative URL to the URL used by webClient.
	 * 
	 * @param webClient for REST API access
	 * @param relativeUrl to URL used to create webClient
	 * @return Mono<ClientResponse> of JSON
	 */
	public static Mono<ClientResponse> retrieveFromRelativeUrl(WebClient webClient, String relativeUrl) {
		logger.info("Using relativeUrl: " + relativeUrl);
		return webClient.get()
				.uri(relativeUrl)
				.accept(MediaType.APPLICATION_JSON)
				.exchange();
	}

}
