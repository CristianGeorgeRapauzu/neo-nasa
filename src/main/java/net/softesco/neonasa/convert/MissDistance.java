package net.softesco.neonasa.convert;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Close Approache Data/Miss Distance for NEO. JavaBean used for JSON unmarshalling.
	Uses lombok annotations for getters/setters of JavaBean properties.
	
	@see https://projectlombok.org/
	@see https://api.nasa.gov/api.html#neows-swagger

	"miss_distance": {
	    "astronomical": "0.0334263473",
	    "lunar": "13.0028495789",
	    "kilometers": "5000510.5",
	    "miles": "3107173.25"
	 }
 * @author cristi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class MissDistance {
	
	@Setter @Getter
	private String astronomical;

	@Setter @Getter
	private String lunar;
}
