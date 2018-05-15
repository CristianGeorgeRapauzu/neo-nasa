package net.softesco.neonasa.convert;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** NEO Statistics. JavaBean used for JSON unmarshalling.
	Uses lombok annotations for getters/setters of JavaBean properties.
	
	@see https://projectlombok.org/
	@see https://api.nasa.gov/api.html#neows-swagger

    Get the Near Earth Object data set totals
	get /rest/v1/stats

	Model schema:
	{
	  "links": [
	    {
	      "rel": "string",
	      "href": "string",
	      "templated": true
	    }
	  ],
	  "near_earth_object_count": 0,
	  "close_approach_count": 0,
	  "last_updated": "string",
	  "source": "string",
	  "nasa_jpl_url": {}
	}

	Example:
	{
	  "near_earth_object_count": 18672,
	  "close_approach_count": 488066,
	  "last_updated": "2018-05-05",
	  "source": "All the NEO data is from NASA JPL NEO team.",
	  "nasa_jpl_url": "http://neo.jpl.nasa.gov/"
	}
  	@author cristi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class NeoStatistics {

	@JsonProperty(value="near_earth_object_count")
	@Setter @Getter
	private Long neoCount;

	@JsonProperty(value="close_approach_count")
	@Setter @Getter
	private Long closeApproachCount;

	@JsonProperty(value="last_updated")
	@Setter @Getter
	private String lastUpdated;
}
