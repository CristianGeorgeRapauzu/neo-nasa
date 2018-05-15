package net.softesco.neonasa.strategy;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import net.softesco.neonasa.contact.NeoBrowseWebClient;
import net.softesco.neonasa.contact.NeoSubscriber;
import net.softesco.neonasa.convert.PaginatedNeo;
import net.softesco.neonasa.dto.NeoSummary;

public class AsynchronousFluxWithBackPressureScrutinizingStrategy implements ScrutinizingStrategy {

	private static final String ASYNCHRONOUS_FLUX_BACKPRESSURE_STRATEGY = "Asynchronous with Flux and back-pressure";
	
	@Override
	public String getDescription() {
		return ASYNCHRONOUS_FLUX_BACKPRESSURE_STRATEGY;
	}

	/**
	 * Precondition: neoSummary.neoCount has been initialized through a call to obtainNeoCountFromStatistics
	 * 
	 * Use REST API to browse pages of 20 NEOs per page
	 * and extract NEO info from current page asynchronously - with Flux and back-pressure
	 * @see https://api.nasa.gov/neo/rest/v1/neo/browse?page=0&size=20&api_key=DEMO_KEY
	 * @param neoSummary in/out summary of number, greatest, nearest NEO today, plus paging
	 * @param firstPageNumber used to limit the scrutinizing range;
	 *        default=1 means more than 935 pages will be requested
	 */
	@Override
	public void obtainNeoInfoFromBrowsedPages(NeoSummary neoSummary, int firstPageNumber) {
		NeoBrowseWebClient browseWebClient = new NeoBrowseWebClient();
		Long totalPages = neoSummary.getNeoCount() / 20;
		int pageRange = totalPages.intValue() - firstPageNumber;
		
		Flux<Integer> fluxPageNumbers = Flux.range(firstPageNumber, pageRange);
		fluxPageNumbers.subscribe(pageNumber -> {
								processPage(browseWebClient, pageNumber, totalPages, neoSummary);
							},
							error -> error.printStackTrace(),
							() -> logger.info("DONE preparing all pages: " + neoSummary)
						);
	}

	/**
	 * Browse NEO page and update info in NEO summary, 
	 * using a NeoSubscriber<PaginatedNeo> for applying back-pressure
	 * 
	 * @param browseWebClient of https://api.nasa.gov/neo/rest/v1/neo/browse?page=101&size=20&api_key=...
	 * @param pageNumber currently browsed
	 * @param totalPages grand total is NEO count/page size (default 20)
	 * @param neoSummary in/out summary of number, greatest, nearest NEO today, plus paging
	 */
	@Override
	public void processPage(NeoBrowseWebClient browseWebClient, int pageNumber, Long totalPages, NeoSummary neoSummary) {
		logger.debug(" Requesting Near Earth Object (NEO) from page " + pageNumber + "/" + totalPages);
		NeoSubscriber<PaginatedNeo> neoSubscriber = new NeoSubscriber<>();
		Mono<PaginatedNeo> browseResult = browseWebClient.getPage(pageNumber);

		browseResult.subscribe(paginatedNeo -> {		
								paginatedNeo.processPage(neoSummary);
							},
							error -> error.printStackTrace(),
							() -> logger.info((neoSummary.getTotalPages().equals(neoSummary.getCurrentPage()) ? "LAST" : "After") + 
											  " page: " + neoSummary.getCurrentPage() +
											  ", maxAbsoluteMagnitudeH: " + neoSummary.getMaxAbsoluteMagnitudeH() + 
											  " of NEO: " + neoSummary.getGreatestNeo().getNeoReferenceId() +
									          ", minMissDistance: " + neoSummary.getMinMissDistance() +
									          " of NEO: " + neoSummary.getNearestNeo().getNeoReferenceId()),
							subscriber -> subscriber.request(NeoSubscriber.FIVE_REQUESTS)
					);
		browseResult.subscribe(neoSubscriber);
	}

}
