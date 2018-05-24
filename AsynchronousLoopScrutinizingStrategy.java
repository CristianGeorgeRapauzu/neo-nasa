package net.softesco.neonasa.strategy;

import net.softesco.neonasa.contact.NeoBrowseWebClient;
import net.softesco.neonasa.dto.NeoSummary;

public class AsynchronousLoopScrutinizingStrategy implements ScrutinizingStrategy {

	private static final String ASYNCHRONOUS_LOOP_STRATEGY = "Asynchronous loop with Flux and no back-pressure";
	
	@Override
	public String getDescription() {
		return ASYNCHRONOUS_LOOP_STRATEGY;
	}

	/**
	 * Precondition: neoSummary.neoCount has been initialized through a call to obtainNeoCountFromStatistics
	 * 
	 * Use REST API to browse pages of 20 NEOs per page
	 * and extract NEO info from current page asynchronously - loop with Flux and no back-pressure
	 * @see https://api.nasa.gov/neo/rest/v1/neo/browse?page=0&size=20&api_key=DEMO_KEY
	 * @param neoSummary in/out summary of number, greatest, nearest NEO today, plus paging
	 * @param firstPageNumber used to limit the scrutinizing range;
	 *        using arg[0]: -startpage=1 (firstPageNumber=1) means more than 935 pages will be requested
	 */
	@Override
	public void obtainNeoInfoFromBrowsedPages(NeoSummary neoSummary, int firstPageNumber) {
		NeoBrowseWebClient browseWebClient = new NeoBrowseWebClient();
		Long totalPages = neoSummary.getNeoCount() / 20;

		int pageNumber = firstPageNumber; 
		do {
			processPage(browseWebClient, pageNumber, totalPages, neoSummary);
			pageNumber++;
		} while (pageNumber <= totalPages);
	}

}
