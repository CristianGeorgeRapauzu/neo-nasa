package net.softesco.neonasa.convert;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CloseApproacheDataTest {

	static public final String CLOSE_APPROACHE_DATA_2018_05_15 = "{\"close_approach_date\" : \"2018-05-15\",\"epoch_date_close_approach\" : 1526367600000," +
    "\"relative_velocity\" : {" +
    "\"kilometers_per_second\" : \"12.8100245338\"," +
    "\"kilometers_per_hour\" : \"46116.0883216217\"," +
    "\"miles_per_hour\" : \"28654.743879621\"}," +
    "\"miss_distance\" : {" +
      "\"astronomical\" : \"0.001356725\"," +
      "\"lunar\" : \"0.5277660489\"," +
      "\"kilometers\" : \"202963.171875\"," +
      "\"miles\" : \"126115.46875\"}," +
    "\"orbiting_body\" : \"Earth\"}";

	private ObjectMapper objectMapper;
	private CloseApproacheData closeApproacheData;
	
	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		objectMapper = new ObjectMapper();
		closeApproacheData = objectMapper.readValue(CLOSE_APPROACHE_DATA_2018_05_15, CloseApproacheData.class);
	}

	@Test
	public void parseOnlySignificantFields() {
		System.out.println("closeApproacheData: " + closeApproacheData.toString());
		assertThat(closeApproacheData).isNotNull();
		assertThat("2018-05-15".equals(closeApproacheData.getCloseApproachDate())).isTrue();
		assertThat("Earth".equals(closeApproacheData.getOrbitingBody())).isTrue();
		assertThat("0.001356725".equals(closeApproacheData.getMissDistance().getAstronomical())).isTrue();
	}

}
