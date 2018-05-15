package net.softesco.neonasa.convert;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
   	Neo current Page. JavaBean used for JSON unmarshalling
 	Uses lombok annotations for getters/setters of JavaBean properties.
	
	@see https://projectlombok.org/
	@see https://api.nasa.gov/api.html#neows-swagger
    
    Model schema:
	{
	  "page": {
	    "size": 20,
	    "total_elements": 18698,
	    "total_pages": 934,
	    "number": 0
	  }, ...
 * @author cristi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Page {

	@JsonProperty(value="total_elements")
	@Setter @Getter
	private Long totalElements;

	@JsonProperty(value="total_pages")
	@Setter @Getter
	private Long totalPages;

	@Setter @Getter
	private Long size;

	@Setter @Getter
	private Long number;
	
	// avoid exceeding the limit of total pages
	public boolean validate() {
		return number <= totalPages;
	}
}
