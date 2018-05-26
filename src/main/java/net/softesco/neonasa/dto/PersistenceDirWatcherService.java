package net.softesco.neonasa.dto;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Watch directory with neo.summary used for persistence
 * in order to decide when all asynchronous processing has actually ended and
 * print the stabilized NEO summary.
 * 
 * @author cristi
 * @see: https://docs.oracle.com/javase/tutorial/essential/io/examples/WatchDir.java
 */
public class PersistenceDirWatcherService {

	private static Logger logger = LoggerFactory.getLogger(PersistenceDirWatcherService.class);

    private final WatchService watchService;
    private final Map<WatchKey,Path> watchKeyPathMap;
    private boolean watched = false;
    
	public PersistenceDirWatcherService(Path watchedPath) throws IOException {
        this.watchService = FileSystems.getDefault().newWatchService();
        this.watchKeyPathMap = new HashMap<WatchKey,Path>();
        register(watchedPath);
		this.watched = true;
	}

    private void register(Path watchedPath) throws IOException {
        WatchKey key = watchedPath.register(watchService, ENTRY_MODIFY);
        if (watched) {
            Path prev = watchKeyPathMap.get(key);
            if (prev == null) {
            	logger.debug("register: " + watchedPath);
            } else {
                if (!watchedPath.equals(prev)) {
                	logger.debug("update: " + prev + ", " + watchedPath);
                }
            }
        }
        watchKeyPathMap.put(key, watchedPath);
    }

    public void loopWatchingEvents() {
        for (;;) {

            // wait for key to be signaled
            WatchKey watchKey;
            try {
            	watchKey = watchService.poll(3, TimeUnit.SECONDS);
            } catch (InterruptedException x) {
                return;
            }
            
            Path dir = watchKeyPathMap.get(watchKey);
            if (dir == null) {
            	logger.info("WatchKey not recognized: " + watchKey + ", probably due to end of asynchronous processing.\n");
                return;
            }

            for (WatchEvent<?> watchEvent: watchKey.pollEvents()) {
                Kind<?> watchEventKind = watchEvent.kind();

                if (watchEventKind == ENTRY_MODIFY) {
	                // Context for directory entry event is the file name of entry
	                WatchEvent<Path> we = cast(watchEvent);
	                Path name = we.context();
	                Path persistencePath = dir.resolve(name);
	
	                logger.debug("persisted: " + persistencePath);
                }
            }
            
            // reset key and remove from set if directory no longer accessible
            boolean valid = watchKey.reset();
            if (!valid) {
            	watchKeyPathMap.remove(watchKey);

                // all directories are inaccessible
                if (watchKeyPathMap.isEmpty()) {
                    break;
                }
            }

        }
    }
    
    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> watchEvent) {
        return (WatchEvent<T>)watchEvent;
    }

}
