package net.softesco.neonasa.convert;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.softesco.neonasa.NeoException;
import net.softesco.neonasa.dto.NeoSummary;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Group of 20 Near Earth Object (one page of NEOs) JavaBean used for JSON unmarshalling.
	Uses lombok annotations for getters/setters of JavaBean properties.
	
	@see https://projectlombok.org/
	@see https://api.nasa.gov/api.html#neows-swagger
		 https://api.nasa.gov/neo/rest/v1/neo/browse?page=0&size=20&api_key=DEMO_KEY
		 
	Retieve a paginated list of Near Earth Objects via the browse service
	get /rest/v1/neo/browse?page=0&size=20&api_key=DEMO_KEY
		
	Model schema:
	{
	  "links": {
	    "prev": "string",
	    "next": "string",
	    "self": "string"
	  },
	  "page": {
	    "size": 20,
	    "total_elements": 18698,
	    "total_pages": 934,
	    "number": 0
	  },
	  "near_earth_objects": [
			{
			  "is_potentially_hazardous_asteroid": true,
			  "neo_reference_id": "string",
			  "name": "string",
			  "nasa_jpl_url": "string",
			  "absolute_magnitude_h": 0,
			  "estimated_diameter": {},
			  "close_approach_data": [
			    {
			      "close_approach_date": "string",
			      "epoch_date_close_approach": 0,
			      "relative_velocity": {},
			      "miss_distance": {},
			      "orbiting_body": "string"
			    }
			  ],
			  "orbital_data": {}
			}
	}

	Example:
	{
	  "links": {
	    "next": "https://api.nasa.gov/neo/rest/v1/neo/browse?page=1&size=20&api_key=DEMO_KEY",
	    "self": "https://api.nasa.gov/neo/rest/v1/neo/browse?page=0&size=20&api_key=DEMO_KEY"
	  },
	  "page": {
	    "size": 20,
	    "total_elements": 18698,
	    "total_pages": 934,
	    "number": 0
	  },
	  "near_earth_objects": [
	    {
	      "links": {
	        "self": "https://api.nasa.gov/neo/rest/v1/neo/2021277?api_key=DEMO_KEY"
	      },
	      "neo_reference_id": "2021277",
	      "name": "21277 (1996 TO5)",
	      "nasa_jpl_url": "http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=2021277",
	      "absolute_magnitude_h": 16,
	      "estimated_diameter": {
	        "kilometers": {
	          "estimated_diameter_min": 1.6770846216,
	          "estimated_diameter_max": 3.750075218
	        },
	        "meters": {
	          "estimated_diameter_min": 1677.0846216284,
	          "estimated_diameter_max": 3750.0752179805
	        },
	        "miles": {
	          "estimated_diameter_min": 1.0420917484,
	          "estimated_diameter_max": 2.3301879883
	        },
	        "feet": {
	          "estimated_diameter_min": 5502.2463100232,
	          "estimated_diameter_max": 12303.3967781592
	        }
	      },
	      "is_potentially_hazardous_asteroid": false,
	      "close_approach_data": [
	        {
	          "close_approach_date": "1945-06-07",
	          "epoch_date_close_approach": -775328400000,
	          "relative_velocity": {
	            "kilometers_per_second": "15.5095198569",
	            "kilometers_per_hour": "55834.2714848594",
	            "miles_per_hour": "34693.2449679117"
	          },
	          "miss_distance": {
	            "astronomical": "0.0334263473",
	            "lunar": "13.0028495789",
	            "kilometers": "5000510.5",
	            "miles": "3107173.25"
	          },
	          "orbiting_body": "Mars"
	        }
	      ],
	      "orbital_data": {
	        "orbit_id": "98",
	        "orbit_determination_date": "2017-04-06 09:29:34",
	        "orbit_uncertainty": "0",
	        "minimum_orbit_intersection": ".312301",
	        "jupiter_tisserand_invariant": "3.267",
	        "epoch_osculation": "2458200.5",
	        "eccentricity": ".5205867691634213",
	        "semi_major_axis": "2.376837254750018",
	        "inclination": "20.95113067368235",
	        "ascending_node_longitude": "167.3875434155023",
	        "orbital_period": "1338.435958512551",
	        "perihelion_distance": "1.13948722747245",
	        "perihelion_argument": "250.1988657516737",
	        "aphelion_distance": "3.614187282027585",
	        "perihelion_time": "2458492.649346785363",
	        "mean_anomaly": "281.4203980595279",
	        "mean_motion": ".2689706576623061",
	        "equinox": "J2000"
	      }
	    },
    	...	
*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaginatedNeo {

	private static Logger logger = LoggerFactory.getLogger(PaginatedNeo.class);

	@Setter @Getter
	private PageNavigation links; // HATEOAS
	
	@Setter @Getter
	private Page page;

	@JsonProperty(value="near_earth_objects")
	@Setter @Getter
	private List<Neo> neos;
	
	/**
	 * Update info from current page into NEO summary
	 * @param neoSummary in/out summary of number, greatest, nearest NEO today, plus paging
	 */
	public void processPage(NeoSummary neoSummary) {
		final Page page = getPage();

		// avoid exceeding the limit of total pages
		if (page == null || !page.validate()) {
			logger.warn("processPage - INVALID page: " + page);
			return;
		}
		neoSummary.updatePageInfo(page);

		final List<Neo> neoListPerPage = getNeos();
		try {
			neoSummary.updateSizeInfo(neoListPerPage);
			neoSummary.updateDistanceInfo(neoListPerPage);
		} catch (NeoException e) {
			logger.error("Error updating NEO summary", e.getLocalizedMessage());
		}
	}

}
