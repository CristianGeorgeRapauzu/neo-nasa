package net.softesco.neonasa.contact;

import org.junit.Before;
import org.junit.Test;

import reactor.test.StepVerifier;

public class NeoBrowseWebClientTest {

	private NeoBrowseWebClient browseWebClient;
	
	@Before
	public void setUp() {
		browseWebClient = new NeoBrowseWebClient();
	}
	
	@Test
	public void getPage10AsMono() {
		// as result is Mono<PaginatedNeo> there should be only one PaginatedNeo received
		StepVerifier.create(browseWebClient.getPage(10))
					.expectNextCount(1)
					.verifyComplete();
	}

	@Test
	public void getPage10AsMonoPaginatedNeoWith20Neos() {
		// as result is Mono<PaginatedNeo> there should be only one PaginatedNeo with 20 NEOs received
		StepVerifier.create(browseWebClient.getPage(10))
					.expectNextMatches(paginatedNeo -> paginatedNeo.getNeos() != null 
													&& paginatedNeo.getNeos().size() == 20)
					.verifyComplete();
	}

}
