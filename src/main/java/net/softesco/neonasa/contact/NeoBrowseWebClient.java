package net.softesco.neonasa.contact;

import net.softesco.neonasa.convert.PaginatedNeo;

import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

/**
 * Use REST API to browse NEOs one page per request
 * @see https://api.nasa.gov/neo/rest/v1/neo/browse?page=935&size=20&api_key=DEMO_KEY
 * 
 * @author cristi
 */
public class NeoBrowseWebClient {

	// "/neo/browse?page=935&size=20&api_key=DEMO_KEY"
	public static final String NEO_BROWSE_PAGE = "/neo/browse?page=%d&size=20&api_key=%s";
	
	private WebClient webClient = WebClientFactory.createWebClient();

	public Mono<PaginatedNeo> getPage(int pageNumber) {
		return WebClientFactory
				.retrieveFromRelativeUrl(webClient, String.format(NEO_BROWSE_PAGE, pageNumber, WebClientFactory.NEO_ACCESS_KEY))
				.flatMap(res -> res.bodyToMono(PaginatedNeo.class));
	}

}
