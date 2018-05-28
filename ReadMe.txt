Problem
-------
Write a Java application to get a list of “Near Earth Objects” using the NASA RESTful Web Service https://api.nasa.gov/api.html#NeoWS . 
Identify which NEO is the largest in size and which is the closest to Earth.

Output the total number of NEOs, and the details retrieved for both the largest and closest NEOs identified.

Solution
--------
A command line application using SpringBoot which traces by default only the last 25 pages (instead of the grand total of 935),  or starts scanning from the specified start page of NEOs.
Uses a Strategy (a/synchronous, loop/flux, with/out back-pressure) to scrutinize responses and persist NEO summary in /tmp/neonasa/neo.summary.
The directory is watched for modifications with a timeout. After the timeout the stable NEO summary is echoed.

Build and run:
$ ./mvnw clean package

Using default start page 910:
$ java -jar target/neonasa-0.0.1-SNAPSHOT.jar

Using explicit start page 901:
$ java -jar target/neonasa-0.0.1-SNAPSHOT.jar -startpage=901

To glean information from the whole NEO set, start browsing from page 1 up to 935 (or even more):
$ java -jar target/neonasa-0.0.1-SNAPSHOT.jar -startpage=1

View the persisted summary:
$ cat /tmp/neonasa/neo.summary

NeoSummary(neoCount=18791, lastUpdated=2018-05-23, totalPages=939, currentPage=921, 
maxAbsoluteMagnitudeH=34.282, 
minMissDistance=0.0037861112, 
nearestNeo=Neo(neoReferenceId=3797848, name=(2018 BA3), potentiallyHazardousAsteroid=false, absoluteMagnitudeH=26.3), 
greatestNeo=Neo(neoReferenceId=3799865, name=(2018 DM4), potentiallyHazardousAsteroid=false, absoluteMagnitudeH=34.282))

Testing
-------
To run only Unit Tests:
./mvnw test

./mvnw -Dtest=NeoSummaryTest test
./mvnw -Dtest=NeoTest#getCloseApproacheDataFirstMissDistanceAstronomical test

To run Unit Tests AND Integration Tests:
./mvnw verify

longer alternative:
./mvnw integration-test

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
