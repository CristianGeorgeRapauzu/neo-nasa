package net.softesco.neonasa.convert;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NeoTest {

	private ObjectMapper objectMapper;
	private Neo neo;
	
	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		neo = GeneratorUtility.buildNeo(); // Neo 3799276
	}

	@Test
	public void getCloseApproacheDataFirstMissDistanceAstronomical() {
		String referenceId = neo.getNeoReferenceId();
		System.out.println("NEO reference id: " + referenceId);
		assertThat(referenceId).isEqualTo("3799276");
		assertThat(neo.getCloseApproacheData().isEmpty()).isFalse();
		assertThat(neo.getCloseApproacheDataFirstMissDistanceAstronomical()).isEqualTo(0.4991403416D);
	}

	@Test
	public void getCloseApproacheDataFirstMissDistanceAstronomicalFromEmptyList() throws JsonParseException, JsonMappingException, IOException {
		// expected MISSED_DISTANCE_1_AU for empty CAD list
		objectMapper = new ObjectMapper();
		File file = ResourceUtils.getFile(this.getClass().getResource("/NeoWithoutCAD_3519516_20180513.json"));
		neo = objectMapper.readValue(file, Neo.class);
		String referenceId = neo.getNeoReferenceId();
		System.out.println("NEO (without Close Approache Data) reference id: " + referenceId);
		assertThat(referenceId).isEqualTo("3519516");
		assertThat(neo.getCloseApproacheData().isEmpty()).isTrue();
		assertThat(neo.getCloseApproacheDataFirstMissDistanceAstronomical()).isEqualTo(Neo.MISSED_DISTANCE_1_AU);
	}

}
