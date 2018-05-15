Problem
-------
Write a Java application to get a list of “Near Earth Objects” using the NASA RESTful Web Service https://api.nasa.gov/api.html#NeoWS . 
Identify which NEO is the largest in size and which is the closest to Earth.

Output the total number of NEOs, and the details retrieved for both the largest and closest NEOs identified.

Solution
--------
A command line application which traces only the last 20 pages (instead of the grand total of 935).

Unpack neonasa.tar.gz

Build and run:

./mvnw clean package
java -jar target/neonasa-0.0.1-SNAPSHOT.jar

The above command line (without args) uses a decent default: "startpage=910"

java -jar target/neonasa-0.0.1-SNAPSHOT.jar -startpage=900

To glean information from the whole NEO set, start browsing from page 1 up to 935 (or even more):

java -jar target/neonasa-0.0.1-SNAPSHOT.jar -startpage=1


References
----------

https://api.nasa.gov/api.html#neows-swagger

http://spring.io/guides/gs/reactive-rest-service/
http://spring.io/guides/gs/consuming-rest/

https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/reactive/function/client/WebClient.RequestHeadersSpec.html#exchange--

org.springframework.test.web.reactive.server
Interface WebTestClient

org.springframework.test.web.reactive.server.ExchangeResult
org.springframework.test.web.reactive.server.FluxExchangeResult<T>

https://spring.io/understanding/HATEOAS
-------------------------
NASA REST API access
- need to register, it is free

access key: (instead of DEMO_KEY, with limited number of usages per hour and day)
Wbff...

example:
https://api.nasa.gov/planetary/apod?api_key=Wbff...
-------------------------
https://api.nasa.gov/neo/rest/v1/neo/browse?api_key=Wbff...
-------------------------
https://cneos.jpl.nasa.gov/about/neo_groups.html
https://cneos.jpl.nasa.gov/glossary/NEO.html An asteroid or comet with a perihelion distance less than or equal to 1.3 au
https://cneos.jpl.nasa.gov/glossary/perihelion.html An orbit’s closest point to the Sun.
https://cneos.jpl.nasa.gov/glossary/h.html Absolute Magnitude H
https://cneos.jpl.nasa.gov/ca/ Close Approach

PHAs 	
MOID<=0.05 au
H<=22.0 	
Potentially Hazardous Asteroids: 
NEAs whose Minimum Orbit Intersection Distance (MOID) with the Earth is 0.05 au or less
and whose absolute magnitude (H) is 22.0 or brighter.
