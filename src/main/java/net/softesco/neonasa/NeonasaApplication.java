package net.softesco.neonasa;

import net.softesco.neonasa.dto.NeoSummary;
import net.softesco.neonasa.strategy.AsynchronousFluxWithBackPressureScrutinizingStrategy;
import net.softesco.neonasa.strategy.ScrutinizingStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * Use NASA Near Earth Object (NEO) REST API to obtain NEO information like: 
 * - greatest NEO
 * - closest NEO
 * - NEO count
 * 
 * @author cristi
 */
@SpringBootApplication
public class NeonasaApplication {
	
	private static Logger logger = LoggerFactory.getLogger(NeonasaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(NeonasaApplication.class, args);
	}
	
	@Bean
	ScrutinizingStrategy scrutinizingStrategy() {
		// Various strategies:
		//return new SynchronousLoopScrutinizingStrategy();
		//return new AsynchronousLoopScrutinizingStrategy();
		//return new AsynchronousFluxNoBackPressureScrutinizingStrategy();
		return new AsynchronousFluxWithBackPressureScrutinizingStrategy();
	}

	@Bean
	NeoSummary neoSummary() {
		return new NeoSummary();
	}

	@Bean
	@Scope("singleton") // default scope - just a reminder of the convention-over-configuration advantages
	public CentralScrutinizer centralScrutinizer(ScrutinizingStrategy scrutinizingStrategy, NeoSummary neoSummary) {
		return new CentralScrutinizer(scrutinizingStrategy, neoSummary);
	}
	
	@Bean
	public CommandLineRunner run(CentralScrutinizer centralScrutinizer) throws Exception {
		return args -> {
			// must explicitly require all pages by providing parameter "-startpage=1", otherwise start at less-stressing default 910
			int firstPageNumber = 910;
			if (args.length > 0) {
				String[] startpageArg = args[0].split("=");
				firstPageNumber = Integer.parseInt(startpageArg[1]); 
			}
			final ScrutinizingStrategy scrutinizingStrategy = centralScrutinizer.getScrutinizingStrategy();
			logger.info("Starts scanning from page: " + firstPageNumber + " using strategy: " + scrutinizingStrategy.getDescription());
			scrutinizingStrategy.obtainNeoCountFromStatistics(centralScrutinizer.getNeoSummary());
			scrutinizingStrategy.obtainNeoInfoFromBrowsedPages(centralScrutinizer.getNeoSummary(), firstPageNumber);
		};
	}
}
