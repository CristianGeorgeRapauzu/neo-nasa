package net.softesco.neonasa;

import lombok.Getter;
import net.softesco.neonasa.dto.NeoSummary;
import net.softesco.neonasa.strategy.ScrutinizingStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Scrutinizes the NEO info from REST API responses and
 * accumulates the info (number, greatest, nearest NEO today, plus paging) in NeoSummary.
 * Singleton using Strategy to scrutinize responses and persist NEO summary.
 * 
 * @author cristi
 */
public class CentralScrutinizer {

	private static CentralScrutinizer instance = null;
	
	private static Logger logger = LoggerFactory.getLogger(CentralScrutinizer.class);
	
	// summary of number, greatest, nearest NEO today, plus paging
	@Getter
	private NeoSummary neoSummary;
	
	// flavors: a/synchronous, loop/flux, with/out back-pressure
	@Getter
	private ScrutinizingStrategy scrutinizingStrategy;

	// Singleton
	private CentralScrutinizer(ScrutinizingStrategy scrutinizingStrategy, NeoSummary neoSummary) {
		this.scrutinizingStrategy = scrutinizingStrategy;
		this.neoSummary = neoSummary;
	}
	
	// Singleton instance getter
	public static CentralScrutinizer getInstance(ScrutinizingStrategy scrutinizingStrategy, NeoSummary neoSummary) {
		if (instance == null) {
			instance = new CentralScrutinizer(scrutinizingStrategy, neoSummary);
			logger.info("Strategy: " + scrutinizingStrategy.getDescription());
		}
		return instance;
	}

}
