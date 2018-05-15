package net.softesco.neonasa.contact;

import net.softesco.neonasa.convert.NeoStatistics;

import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

/**
 * Use REST API to obtain NEO statistics: 
 * NeoStatistics(neoCount=18678, closeApproachCount=488290, lastUpdated=2018-05-06)
 * 
 * @author cristi
 */
public class NeoStatisticsWebClient {

	private WebClient webClient = WebClientFactory.createWebClient();
	private Mono<ClientResponse> result = WebClientFactory.retrieveFromRelativeUrl(webClient, 
														   						   "/stats?api_key=" + WebClientFactory.NEO_ACCESS_KEY);
	public Mono<NeoStatistics> getResult() {
		return result.flatMap(res -> res.bodyToMono(NeoStatistics.class));
	}

}
