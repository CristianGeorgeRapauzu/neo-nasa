package net.softesco.neonasa.convert;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
  Neo Page navigation links. JavaBean used for JSON unmarshalling
  
  Model schema:
	{
	  "links": {
	    "prev": "string",
	    "next": "string",
	    "self": "string"
	  }, ...

 * @author cristi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class PageNavigation {

	@Setter @Getter
	private String prev;

	@Setter @Getter
	private String next;

	@Setter @Getter
	private String self;
}
