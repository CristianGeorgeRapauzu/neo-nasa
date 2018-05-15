package net.softesco.neonasa.convert;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneratorUtility {

	public static final String NEAREST_NEO_PER_PAGE = "3762130";
	public static final double MIN_MISS_DISTANCE_PER_PAGE = 0.096868248D;
	public static final String GREATEST_NEO_PER_PAGE = "3799280";
	public static final double MAX_ABSOLUTE_MAGNITUDE_H_PER_PAGE = 28.0D;
	public static final String PAGE_1 = "{\"size\": 20,\"total_elements\": 18698,\"total_pages\": 934,\"number\": 1}";

	private static ObjectMapper objectMapper;

	public static Page buildPage() throws JsonParseException, JsonMappingException, IOException {
		objectMapper = new ObjectMapper();
		return objectMapper.readValue(PAGE_1, Page.class);
	}
	
	public static Neo buildNeo() throws JsonParseException, JsonMappingException, IOException {
		objectMapper = new ObjectMapper();
		File file = ResourceUtils.getFile(GeneratorUtility.class.getResource("/Neo_3799276_20180513.json"));
		return objectMapper.readValue(file, Neo.class);
	}

	public static PaginatedNeo buildPaginatedNeo() throws JsonParseException, JsonMappingException, IOException {
		objectMapper = new ObjectMapper();
		File file = ResourceUtils.getFile(GeneratorUtility.class.getResource("/PaginatedNeo_page901_20180513.json"));
		return objectMapper.readValue(file, PaginatedNeo.class);
	}

	public static List<Neo> buildListNeo() throws JsonParseException, JsonMappingException, IOException {
		PaginatedNeo paginatedNeo = buildPaginatedNeo();
		System.out.println(paginatedNeo);
		return paginatedNeo.getNeos();
	}

}
