package net.softesco.neonasa.strategy;

import reactor.core.publisher.Mono;
import net.softesco.neonasa.contact.NeoBrowseWebClient;
import net.softesco.neonasa.convert.PaginatedNeo;
import net.softesco.neonasa.dto.NeoSummary;

public class SynchronousLoopScrutinizingStrategy implements ScrutinizingStrategy {

	private static final String SYNCHRONOUS_LOOP_STRATEGY = "Synchronous loop with blocking";
	
	@Override
	public String getDescription() {
		return SYNCHRONOUS_LOOP_STRATEGY;
	}

	/**
	 * Use REST API to browse pages of 20 NEOs per page
	 * and extract NEO info from current page synchronously - with block() waiting for the server
	 * @see https://api.nasa.gov/neo/rest/v1/neo/browse?page=0&size=20&api_key=DEMO_KEY
	 * @param neoSummary in/out summary of number, greatest, nearest NEO today, plus paging
	 * @param firstPageNumber used to limit the scrutinizing range;
	 *        default=1 means more than 935 pages will be requested
	 */
	@Override
	public void obtainNeoInfoFromBrowsedPages(NeoSummary neoSummary, int firstPageNumber) {
		NeoBrowseWebClient browseWebClient = new NeoBrowseWebClient();
		
		//int pageRange = 20;
		//int firstPageNumber = 916;
		//final int lastPage = firstPageNumber + pageRange; // last page = totalPages 935
		final int totalPages = neoSummary.getTotalPages().intValue();
		final int lastPage = (totalPages > 0) ? totalPages : (firstPageNumber + 20);
		
		int pageNumber = firstPageNumber;
		do {			
			Mono<PaginatedNeo> browseResult = browseWebClient.getPage(pageNumber);
			PaginatedNeo paginatedNeo = browseResult.block(); // page of NEOs with navigation links (HATEOAS)

			paginatedNeo.processPage(neoSummary);

			/* alternatives for NEO count:
			   Long neoCount = pageOfNeosWithNavigationLinks.getPage().getTotalElements();
			   Long neoCount = this.neoSummary.getNeoCount();
			 */
			logger.info("Near Earth Object (NEO) count from page " + pageNumber + "/" + neoSummary.getTotalPages() + ": " + neoSummary.getNeoCount() + 
					", greatest: " + neoSummary.getMaxAbsoluteMagnitudeH() + " NEO is: " + neoSummary.getGreatestNeo().toString() +
					", nearest: " + neoSummary.getMinMissDistance() + " NEO is: " + neoSummary.getNearestNeo().toString());			
			pageNumber++;
		} while (pageNumber <= lastPage);
	}

}
