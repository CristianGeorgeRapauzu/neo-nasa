package net.softesco.neonasa;

import lombok.Getter;
import net.softesco.neonasa.dto.NeoSummary;
import net.softesco.neonasa.strategy.ScrutinizingStrategy;

/**
 * Scrutinizes the NEO info from REST API responses and
 * accumulates the info (number, greatest, nearest NEO today, plus paging) in NeoSummary.
 * Singleton (default scope of Spring bean) using Strategy to scrutinize responses and persist NEO summary.
 * 
 * @author cristi
 */
public class CentralScrutinizer {

	// summary of number, greatest, nearest NEO today, plus paging
	@Getter
	private NeoSummary neoSummary;
	
	// flavors: a/synchronous, loop/flux, with/out back-pressure
	@Getter
	private ScrutinizingStrategy scrutinizingStrategy;

	public CentralScrutinizer(ScrutinizingStrategy scrutinizingStrategy, NeoSummary neoSummary) {
		this.scrutinizingStrategy = scrutinizingStrategy;
		this.neoSummary = neoSummary;
	}
	
}
