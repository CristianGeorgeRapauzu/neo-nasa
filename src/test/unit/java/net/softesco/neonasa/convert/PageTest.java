package net.softesco.neonasa.convert;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PageTest {
	
	public static final String PAGE_936 = "{\"size\": 20,\"total_elements\": 18698,\"total_pages\": 934,\"number\": 936}";
	
	private ObjectMapper objectMapper;
	private Page page;
	
	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		page = GeneratorUtility.buildPage(); // PAGE_1
	}
	
	@Test
	public void pageValidate() {
		// PAGE_1 = "{\"size\": 20,\"total_elements\": 18698,\"total_pages\": 934,\"number\": 1}";
		System.out.println("page 1: " + page.toString());
		assertThat(page).isNotNull();
		assertThat(page.validate()).isTrue();
	}

	@Test
	public void pageValidateFail() throws JsonParseException, JsonMappingException, IOException {
		objectMapper = new ObjectMapper();
		page = objectMapper.readValue(PAGE_936, Page.class);
		System.out.println("page 936: " + page.toString());
		assertThat(page).isNotNull();
		assertThat(page.validate()).isFalse();
	}

}
