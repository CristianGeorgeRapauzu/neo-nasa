package net.softesco.neonasa.convert;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

import net.softesco.neonasa.NeoException;
import net.softesco.neonasa.dto.NeoSummary;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class PaginatedNeoTest {

	private PaginatedNeo paginatedNeo;

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		paginatedNeo = GeneratorUtility.buildPaginatedNeo(); // page 901
	}

	@Test
	public void persistNeoSummary() throws IOException, NeoException {
		assertThat(paginatedNeo).isNotNull();
		NeoSummary neoSummary = new NeoSummary();
		paginatedNeo.processPage(neoSummary);
		
		// NeoSummary(neoCount=0, lastUpdated=null, totalPages=937, currentPage=901, 
		// maxAbsoluteMagnitudeH=28.0, 
		// minMissDistance=0.096868248, 
		// nearestNeo=Neo(neoReferenceId=3762130, name=(2016 UX41), potentiallyHazardousAsteroid=false, absoluteMagnitudeH=20.1), 
		// greatestNeo=Neo(neoReferenceId=3799280, name=(2018 CF2), potentiallyHazardousAsteroid=false, absoluteMagnitudeH=28.0))
		assertThat(neoSummary.getGreatestNeo().getNeoReferenceId()).isEqualTo(GeneratorUtility.GREATEST_NEO_PER_PAGE);
		assertThat(neoSummary.getNearestNeo().getNeoReferenceId()).isEqualTo(GeneratorUtility.NEAREST_NEO_PER_PAGE);
		
		// NeoSummary persisted in file: /tmp/neonasa/neo.summary
		SeekableByteChannel neoSummaryChannel = neoSummary.getNeoSummaryChannel();
		ByteBuffer byteBuffer = ByteBuffer.allocate(400);
		neoSummaryChannel.position(0);
		neoSummaryChannel.read(byteBuffer);
	    String persistedNeoSummary = new String(byteBuffer.array());
	    assertThat(persistedNeoSummary).contains(GeneratorUtility.GREATEST_NEO_PER_PAGE);
	    assertThat(persistedNeoSummary).contains(GeneratorUtility.NEAREST_NEO_PER_PAGE);
	}

}
