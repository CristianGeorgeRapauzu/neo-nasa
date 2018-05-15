package net.softesco.neonasa.strategy;

import net.softesco.neonasa.contact.NeoBrowseWebClient;
import net.softesco.neonasa.contact.NeoStatisticsWebClient;
import net.softesco.neonasa.convert.NeoStatistics;
import net.softesco.neonasa.convert.PaginatedNeo;
import net.softesco.neonasa.dto.NeoSummary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Mono;

public interface ScrutinizingStrategy {
	static Logger logger = LoggerFactory.getLogger(ScrutinizingStrategy.class);

	String getDescription();
	
	/**
	 * Use REST API to obtain NEO statistics: 
	 * NeoStatistics(neoCount=18678, closeApproachCount=488290, lastUpdated=2018-05-06)
	 * and extract NEO count
	 * @see https://api.nasa.gov/neo/rest/v1/stats?api_key=DEMO_KEY
	 */
	default void obtainNeoCountFromStatistics(NeoSummary neoSummary){
		NeoStatisticsWebClient statsWebClient = new NeoStatisticsWebClient();
		Mono<NeoStatistics> statsResult = statsWebClient.getResult();
		NeoStatistics neoStatistics = statsResult.block();
		
		Long neoCount = neoStatistics.getNeoCount();
		String lastUpdated = neoStatistics.getLastUpdated();
		neoSummary.setNeoCount(neoCount);
		neoSummary.setLastUpdated(lastUpdated);
		logger.info("Near Earth Object (NEO) count: " + neoCount + " on: " + lastUpdated);
	}

	/**
	 * Precondition: neoSummary.neoCount has been initialized through a call to obtainNeoCountFromStatistics
	 * 
	 * Use REST API to browse pages of 20 NEOs per page
	 * and extract NEO info from current page
	 * @see https://api.nasa.gov/neo/rest/v1/neo/browse?page=10&size=20&api_key=DEMO_KEY
	 * 
	 * @param neoSummary in/out summary of number, greatest, nearest NEO today, plus paging
	 * @param firstPageNumber used to limit the scrutinizing range;
	 *        default=1 means more than 935 pages will be requested
	 */
	void obtainNeoInfoFromBrowsedPages(NeoSummary neoSummary, int firstPageNumber);
	
	/**
	 * Browse NEO page and update info in NEO summary
	 * 
	 * @param browseWebClient of https://api.nasa.gov/neo/rest/v1/neo/browse?page=101&size=20&api_key=...
	 * @param pageNumber currently browsed
	 * @param totalPages grand total is NEO count/page size (default 20)
	 * @param neoSummary in/out summary of number, greatest, nearest NEO today, plus paging
	 */
	default void processPage(NeoBrowseWebClient browseWebClient, int pageNumber, Long totalPages, NeoSummary neoSummary) {
		logger.debug(" Requesting Near Earth Object (NEO) from page " + pageNumber + "/" + totalPages);
		Mono<PaginatedNeo> browseResult = browseWebClient.getPage(pageNumber);

		browseResult.subscribe(paginatedNeo -> {			
								paginatedNeo.processPage(neoSummary);
							},
							error -> error.printStackTrace(),
							() -> {
								logger.info((neoSummary.getTotalPages().equals(neoSummary.getCurrentPage()) ? "LAST" : "After") + 
											" page: " + neoSummary.getCurrentPage() + 
											", maxAbsoluteMagnitudeH: " + neoSummary.getMaxAbsoluteMagnitudeH() + 
											" of NEO: " + neoSummary.getGreatestNeo().getNeoReferenceId() +
									        ", minMissDistance: " + neoSummary.getMinMissDistance() +
									        " of NEO: " + neoSummary.getNearestNeo().getNeoReferenceId());
								}
					);
	}

}
