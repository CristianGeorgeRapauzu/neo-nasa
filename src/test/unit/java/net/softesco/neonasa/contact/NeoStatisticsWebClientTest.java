package net.softesco.neonasa.contact;

import org.junit.Before;
import org.junit.Test;

import reactor.test.StepVerifier;

public class NeoStatisticsWebClientTest {

	private NeoStatisticsWebClient statsWebClient;
	
	@Before
	public void setUp() {
		statsWebClient = new NeoStatisticsWebClient();
	}

	@Test
	public void getResultAsMono() {
		// as result is Mono<NeoStatistics> there should be only one NeoStatistics received
		StepVerifier.create(statsWebClient.getResult())
					.expectNextCount(1)
					.verifyComplete();
	}

	@Test
	public void getResultAsMonoNeoStatistics() {
		// as result is Mono<NeoStatistics> there should be only one NeoStatistics with NEO count received
		StepVerifier.create(statsWebClient.getResult())
					.expectNextMatches(neoStatistics -> neoStatistics != null 
													 && neoStatistics.getNeoCount() > 18000 
													 && neoStatistics.getLastUpdated() != null)
					.verifyComplete();
	}

}
