package net.softesco.neonasa;

import net.softesco.neonasa.dto.NeoSummary;
import net.softesco.neonasa.strategy.AsynchronousFluxWithBackPressureScrutinizingStrategy;
import net.softesco.neonasa.strategy.ScrutinizingStrategy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
	public CentralScrutinizer centralScrutinizer(ScrutinizingStrategy scrutinizingStrategy, NeoSummary neoSummary) {
		return CentralScrutinizer.getInstance(scrutinizingStrategy, neoSummary);
	}
	
	@Bean
	public CommandLineRunner run(CentralScrutinizer centralScrutinizer) throws Exception {
		return args -> {
			int firstPageNumber = 910; // must explicitly require all pages by providing parameter "-startpage=1"
			System.out.println("args: " + ((args.length==0) ? "none (starts scanning from page 910)" : args[0]));
			centralScrutinizer.getScrutinizingStrategy().obtainNeoCountFromStatistics(centralScrutinizer.getNeoSummary());
			centralScrutinizer.getScrutinizingStrategy().obtainNeoInfoFromBrowsedPages(centralScrutinizer.getNeoSummary(), firstPageNumber);
		};
	}
}
