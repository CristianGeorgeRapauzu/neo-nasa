package net.softesco.neonasa.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import net.softesco.neonasa.NeoException;
import net.softesco.neonasa.convert.GeneratorUtility;
import net.softesco.neonasa.convert.Neo;
import net.softesco.neonasa.convert.Page;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class NeoSummaryTest {
	// see integration test CentralScrutinizerTest for Autowired neoSummary testing
	private NeoSummary neoSummary;
	
	@Before
	public void setUp() throws NeoException {
		neoSummary = new NeoSummary();
	}
	
	@After
	public void tearDown() throws NeoException {
		neoSummary.close();
	}
	
	@Test
	public void getNeoSummaryPath() {
		// /tmp/neonasa/neo.summary
		assertThat(neoSummary.getNeoSummaryPath().endsWith(NeoSummary.NEO_SUMMARY_FILENAME)).isTrue();	
		assertThat(neoSummary.getNeoSummaryPath().getParent().endsWith(NeoSummary.NEO_SUMMARY_PATH)).isTrue();
	}
	
	@Test
	public void snapshotAsString() throws NeoException {
		final String snapshotAsString = neoSummary.snapshotAsString();
		System.out.println("NEO summary snapshot: " + snapshotAsString);
		assertThat(snapshotAsString).isNotNull();
		assertThat(snapshotAsString.startsWith("NeoSummary")).isTrue();
	}
	
	@Test
	public void updatePageInfo() throws JsonParseException, JsonMappingException, IOException {
		Page page1 = GeneratorUtility.buildPage();
		neoSummary.updatePageInfo(page1);
		assertThat(neoSummary.getCurrentPage() == 1L).isTrue();
		assertThat(neoSummary.getTotalPages() == 934L).isTrue();
	}

	@Test
	public void updateDistanceInfo() throws JsonParseException, JsonMappingException, IOException, NeoException {
		List<Neo> neoListPerPage = GeneratorUtility.buildListNeo();
		assertThat(neoListPerPage).isNotNull();
		assertThat(neoListPerPage.size()).isEqualTo(20);
		neoSummary.updateDistanceInfo(neoListPerPage);
		assertThat(neoSummary.getMinMissDistance()).isEqualTo(GeneratorUtility.MIN_MISS_DISTANCE_PER_PAGE);
		assertThat(neoSummary.getNearestNeo().getCloseApproacheDataFirstMissDistanceAstronomical()).isEqualTo(GeneratorUtility.MIN_MISS_DISTANCE_PER_PAGE);
		assertThat(neoSummary.getNearestNeo().getNeoReferenceId()).isEqualTo(GeneratorUtility.NEAREST_NEO_PER_PAGE);
	}

	@Test
	public void updateSizeInfo() throws JsonParseException, JsonMappingException, IOException, NeoException {
		List<Neo> neoListPerPage = GeneratorUtility.buildListNeo();
		neoSummary.updateSizeInfo(neoListPerPage);
		assertThat(neoSummary.getMaxAbsoluteMagnitudeH()).isEqualTo(GeneratorUtility.MAX_ABSOLUTE_MAGNITUDE_H_PER_PAGE);
		assertThat(neoSummary.getGreatestNeo().getAbsoluteMagnitudeH()).isEqualTo(GeneratorUtility.MAX_ABSOLUTE_MAGNITUDE_H_PER_PAGE);
		assertThat(neoSummary.getGreatestNeo().getNeoReferenceId()).isEqualTo(GeneratorUtility.GREATEST_NEO_PER_PAGE);
	}

}
