package net.softesco.neonasa.dto;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.softesco.neonasa.convert.Neo;
import net.softesco.neonasa.convert.Page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Summary of number, greatest, nearest NEO today, plus paging
 * @author cristi
 */
@ToString(exclude="neoSummaryChannel")
public class NeoSummary {

	private static String NEO_SUMMARY_PATH = "/tmp/neonasa";
	private static String NEO_SUMMARY_FILENAME = "neo.summary";
	
	private static Logger logger = LoggerFactory.getLogger(NeoSummary.class);

	@Setter @Getter
	private Long neoCount = 0L;

	@Setter @Getter
	private String lastUpdated;

	@Setter @Getter
	private Long totalPages = 0L;

	@Setter @Getter
	private Long currentPage = 0L;

	@Setter @Getter
	private Double maxAbsoluteMagnitudeH = 0.0;
	
	@Setter @Getter
	private Double minMissDistance = Neo.MISSED_DISTANCE_1_AU; // 1 astronomical unit
	
	@Setter @Getter
	private Neo nearestNeo;

	// determined by absolute_magnitude_h
	// alternative: by estimated_diameter.kilometers.estimated_diameter_max
	@Setter @Getter
	private Neo greatestNeo;
	
	@Getter
	private SeekableByteChannel neoSummaryChannel;
	
	public NeoSummary() {
		try {
			Path neoSummaryPath = Files.createDirectories(Paths.get(NEO_SUMMARY_PATH)).resolve(NEO_SUMMARY_FILENAME);
			this.neoSummaryChannel = Files.newByteChannel(neoSummaryPath, EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Update NEOs paging info
	 * @param page currently browsed
	 */
	public void updatePageInfo(final Page page) {
		if(getTotalPages() == 0L) { // nr of pages does not change during browsing
			setTotalPages(page.getTotalPages());
		}
		setCurrentPage(page.getNumber());

		logger.debug("updatePageInfo - totalPages: " + getTotalPages() + ",  currentPage: "+ getCurrentPage());
	}

	/**
	 * Update smallest NEO close approach miss distance in astronomical units (au) (minMissDistance)
	 * and the corresponding NEO (nearestNeo).
	 * 
	 * @param neoListPerPage list of NEOs in current page (default 20 per page)
	 */
	public void updateDistanceInfo(final List<Neo> neoListPerPage) {
		Double minMissDistance = neoListPerPage.stream()
												 .mapToDouble(Neo::getCloseApproacheDataFirstMissDistanceAstronomical)
												 .min()
												 .orElse(1.0D); // getAsDouble() not safe enough: data might be missing
		if (minMissDistance < getMinMissDistance()) {
			setMinMissDistance(minMissDistance);
		
			Neo nearestNeo = neoListPerPage.stream()
											.filter(neo->minMissDistance.equals(neo.getCloseApproacheDataFirstMissDistanceAstronomical()))
											.findFirst()
											.get();
			setNearestNeo(nearestNeo);
			persist();
		}
		
		logger.debug("updateDistanceInfo - minMissDistance: " + minMissDistance + ", NEO Summary: "+ this);
	}

	/**
	 * Update greatest NEO size (maxAbsoluteMagnitudeH)
	 * and the corresponding NEO (greatestNeo).
	 * 
	 * @param neoListPerPage list of NEOs in current page (default 20 per page)
	 */
	public void updateSizeInfo(final List<Neo> neoListPerPage) {
		Double maxAbsoluteMagnitudeH = neoListPerPage.stream()
													 .mapToDouble(Neo::getAbsoluteMagnitudeH)
													 .max()
													 .orElse(0.0D); // getAsDouble() not safe enough: data might be missing
		if (maxAbsoluteMagnitudeH > getMaxAbsoluteMagnitudeH()) {
			setMaxAbsoluteMagnitudeH(maxAbsoluteMagnitudeH);
		
			Neo greatestNeo = neoListPerPage.stream()
											.filter(neo->neo.getAbsoluteMagnitudeH().equals(maxAbsoluteMagnitudeH))
											.findFirst()
											.get();
			setGreatestNeo(greatestNeo);
			persist();
		}
		
		logger.debug("updateSizeInfo - maxAbsoluteMagnitudeH: " + maxAbsoluteMagnitudeH + ",  NEO Summary: "+ this);
	}

	/**
	 * Overwrite the previous NeoSummary info with the latest
	 * in file /tmp/neonasa/neo.summary
	 * 
	 * NeoSummary(neoCount=18724, lastUpdated=2018-05-11, 
	 * 			totalPages=936, currentPage=922, 
	 * 			maxAbsoluteMagnitudeH=28.6, 
	 * 			minMissDistance=0.0390178586, 
	 * 			nearestNeo=Neo(neoReferenceId=3799717, name=(2018 DE1), potentiallyHazardousAsteroid=false, absoluteMagnitudeH=25.5), 
	 * 			greatestNeo=Neo(neoReferenceId=3802056, name=(2018 FQ3), potentiallyHazardousAsteroid=false, absoluteMagnitudeH=28.6))
	*/
	private void persist() {
		try {
			byte[] neoSummaryBytes = this.toString().getBytes();
			ByteBuffer neoSummaryByteBuffer = ByteBuffer.wrap(neoSummaryBytes);
			// repositioning to beginning is not enough, as NeoSummary instances vary because of currentPage, name
			this.neoSummaryChannel.truncate(neoSummaryBytes.length);
			this.neoSummaryChannel.position(0);
			this.neoSummaryChannel.write(neoSummaryByteBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
