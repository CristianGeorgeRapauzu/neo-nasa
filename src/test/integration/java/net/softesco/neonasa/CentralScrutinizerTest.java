package net.softesco.neonasa;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import net.softesco.neonasa.dto.NeoSummary;
import net.softesco.neonasa.strategy.ScrutinizingStrategy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CentralScrutinizerTest {

	@Autowired
	private ScrutinizingStrategy scrutinizingStrategy;
	@Autowired
	private NeoSummary neoSummary;
	@Autowired
	private CentralScrutinizer centralScrutinizer;
	
	private String todayIsoFormat;
	
	@Before
	public void setUp() {
		final LocalDate today = LocalDate.now();
		todayIsoFormat = today.format(DateTimeFormatter.ISO_LOCAL_DATE); 
	}
	
	@Test
	public void obtainNeoCountFromStatistics() {
		// the default implementation of obtainNeoCountFromStatistics uses block(), 
		// so the NEO count result is returned synchronously
		// The configured strategy (Asynchronous with Flux and no back-pressure) does NOT override the default method, 
		// thus "asynchronous flavor" has no impact on obtainNeoCountFromStatistics
		centralScrutinizer.getScrutinizingStrategy().obtainNeoCountFromStatistics(neoSummary);
		
		// NeoSummary(neoCount=18724, lastUpdated=2018-05-11, totalPages=0, currentPage=0, maxAbsoluteMagnitudeH=0.0, minMissDistance=1.0, nearestNeo=null, greatestNeo=null)
		System.out.println("********** NeoSummary: " + neoSummary);

		assertThat(neoSummary).isNotNull();		
		assertThat(neoSummary.getNeoCount() > 18000).isTrue();
		assertThat(neoSummary.getLastUpdated()).isNotNull();		
		assertThat(todayIsoFormat.compareTo(neoSummary.getLastUpdated()) >= 0).isTrue();
		// all the other neoSummary fields could have changed already if some page processing finished
	}

}
