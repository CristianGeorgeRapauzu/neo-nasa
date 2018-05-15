package net.softesco.neonasa.contact;

import net.softesco.neonasa.convert.PaginatedNeo;

import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.BaseSubscriber;

/**
 * Use NeoSubscriber<PaginatedNeo> for applying back-pressure
 * i.e. some control over the requests being sent.
 * 
 * @param <T> PaginatedNeo
 */
public class NeoSubscriber<T> extends BaseSubscriber<T> {

	public static final int FIVE_REQUESTS = 5;
	
	private static Logger logger = LoggerFactory.getLogger(NeoSubscriber.class);

	@Override
	public void hookOnSubscribe(Subscription subscription) {
		logger.debug("Subscribed to subscription: " + subscription.toString());
		request(FIVE_REQUESTS); // otherwise it is be unbound: request(Long.MAX_VALUE);
	}

	@Override
	public void hookOnNext(T value) {
		if( value instanceof PaginatedNeo) {
			PaginatedNeo paginatedNeo = (PaginatedNeo) value;
			logger.debug("Page: " + paginatedNeo.getPage().getNumber() + "/" + paginatedNeo.getPage().getTotalPages());
		} else {
			logger.debug(value.toString());			
		}
		request(FIVE_REQUESTS);
	}
	
	public void hookOnComplete() {
		logger.debug("DONE processing page - subscription: " + upstream().toString());
	}
}
