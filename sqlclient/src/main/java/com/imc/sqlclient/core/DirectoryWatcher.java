package com.imc.sqlclient.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imc.sqlclient.exception.SQLClientException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DirectoryWatcher implements Runnable {
	
	public final WatchService watcher;
	private final Map<WatchKey, Path> keys;
	private boolean trace = false;
	private static final ApplicationProperties properties = ApplicationProperties.getInstance();
	private DriverLoader driverLoader = new DriverLoader();
	
	public DirectoryWatcher() {
		try {
			this.watcher = FileSystems.getDefault().newWatchService();
			this.keys = new HashMap<>();
			String driverPath = properties.getDriverPath();
			Path drivers = Paths.get(driverPath);
			registerAll(drivers);
			this.trace = true;
		}
		catch (IOException  e) {
			throw new SQLClientException(e);
		}
	}
	
	
	private final void registerAll(final Path path) throws IOException {
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs) throws IOException {
				register(path);
				return FileVisitResult.CONTINUE;
			}
		});
	}
	
	private void register(Path path) throws IOException {
		WatchKey key = path.register(this.watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
		if (this.trace) {
			Path prev = this.keys.get(key);
			if ( prev == null ) {
				log.debug("Registering {}",path);
			}
			else {
				if(!path.equals(prev)) {
					log.debug("Update {} -> {}",prev,path);
				}
			}
		}
		this.keys.put(key, path);
	}
	

	@Override
	public void run() {
		while(true) {
			WatchKey key;
			try {
				key = this.watcher.take();
			}
			catch (InterruptedException e) {
				log.error("DirectoryWatcher interrupted. {} ",e);
				return ;
			}
			Path path = this.keys.get(key);
			if (path == null ) {
				log.debug("Watch Key not recognized.");
				continue;
			}
			
			try {
				processEvents(key,path);
			}
			catch (Exception e) {
				log.error("an error occurred while processing DirectoryWatcher events.",e);
			}
			
			boolean valid = key.reset();
			if(!valid) {
				this.keys.remove(key);
				if(this.keys.isEmpty()) {
					break;
				}
			}
		}
	}
	
	
	private void processEvents(WatchKey key, Path path ) {
		Path prevChild = null;
		List<WatchEvent<?>> watchEvents = key.pollEvents();
		log.debug("List of events : {}",watchEvents);
		
		for( WatchEvent<?> event : watchEvents ) {
			WatchEvent.Kind kind = event.kind();
			if ( kind == StandardWatchEventKinds.OVERFLOW ) {
				continue;
			}
			
			WatchEvent<Path> watchEvent = (WatchEvent<Path>) event;
			Path name = watchEvent.context();
			Path child = path.resolve(name);
			if(prevChild == null ) {
				prevChild = handleCreateAndDelete(kind,child);
			}
			else {
				if (prevChild.toString().equals(child.toString())) {
					continue;
				}
				else {
					prevChild = handleCreateAndDelete(kind,child);
				}
			}
		}
	}
	private Path handleCreateAndDelete(WatchEvent.Kind kind, Path child) {
		log.info("Event Notified :: {} , {} ", kind , child);
		
		File file = child.toFile();
		if ( kind == StandardWatchEventKinds.ENTRY_CREATE ) {
			driverLoader.makeEntry(file);
		}
		if ( kind == StandardWatchEventKinds.ENTRY_DELETE ) {
			driverLoader.deleteEntry(file);
		}
		return child;
	}

}
