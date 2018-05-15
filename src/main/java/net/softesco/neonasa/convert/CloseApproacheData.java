package net.softesco.neonasa.convert;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Close Approache Data for NEO. JavaBean used for JSON unmarshalling.
	Uses lombok annotations for getters/setters of JavaBean properties.
	
	@see https://projectlombok.org/
	@see https://api.nasa.gov/api.html#neows-swagger

     "close_approach_data": [ ...
		, {
		    "close_approach_date" : "2010-11-20",
		    "epoch_date_close_approach" : 1290240000000,
		    "relative_velocity" : {
		      "kilometers_per_second" : "14.140567791",
		      "kilometers_per_hour" : "50906.0440476936",
		      "miles_per_hour" : "31631.0360917461"
		    },
		    "miss_distance" : {
		      "astronomical" : "0.0613581776",
		      "lunar" : "23.8683300018",
		      "kilometers" : "9179053",
		      "miles" : "5703599"
		    },
		    "orbiting_body" : "Earth"
		  }, {
		    "close_approach_date" : "2018-05-15",
		    "epoch_date_close_approach" : 1526367600000,
		    "relative_velocity" : {
		      "kilometers_per_second" : "12.8100245338",
		      "kilometers_per_hour" : "46116.0883216217",
		      "miles_per_hour" : "28654.743879621"
		    },
		    "miss_distance" : {
		      "astronomical" : "0.001356725",
		      "lunar" : "0.5277660489",
		      "kilometers" : "202963.171875",
		      "miles" : "126115.46875"
		    },
		    "orbiting_body" : "Earth"
		  }, {
		    "close_approach_date" : "2018-05-16",
		    "epoch_date_close_approach" : 1526454000000,
		    "relative_velocity" : {
		      "kilometers_per_second" : "12.8245406266",
		      "kilometers_per_hour" : "46168.3462556768",
		      "miles_per_hour" : "28687.2149275899"
		    },
		    "miss_distance" : {
		      "astronomical" : "0.002899475",
		      "lunar" : "1.1278957129",
		      "kilometers" : "433755.28125",
		      "miles" : "269523.03125"
		    },
		    "orbiting_body" : "Moon"
		  }, ...
		]
 * @author cristi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class CloseApproacheData {

	@JsonProperty(value="close_approach_date")
	@Setter @Getter
	private String closeApproachDate;

	@JsonProperty(value="miss_distance")
	@Setter @Getter
	private MissDistance missDistance;
	
	@JsonProperty(value="orbiting_body")
	@Setter @Getter
	private String orbitingBody;

}
