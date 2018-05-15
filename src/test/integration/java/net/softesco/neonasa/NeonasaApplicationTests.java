package net.softesco.neonasa;

import net.softesco.neonasa.dto.NeoSummary;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NeonasaApplicationTests {

	@Autowired
	private CentralScrutinizer centralScrutinizer;
	
	@Test
	public void contextLoads() {
		// Strategy: AsynchronousFluxWithBackPressureScrutinizingStrategy
		assertThat(centralScrutinizer).isNotNull();
		assertThat(centralScrutinizer.getScrutinizingStrategy()).isNotNull();
		assertThat("Asynchronous with Flux and back-pressure".equals(centralScrutinizer.getScrutinizingStrategy().getDescription())).isTrue();
		final NeoSummary neoSummary = centralScrutinizer.getNeoSummary();
		assertThat(neoSummary).isNotNull();
		// persistence in file /tmp/neonasa/neo.summary
		assertThat(neoSummary.getNeoSummaryChannel()).isNotNull();
	}

}
