package com.imc.sqlclient.core;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum RegisteredClassesRepository {
	
	INSTANCE;

	public static RegisteredClassesRepository getInstance() {
		return INSTANCE;
	}

	private final ConcurrentHashMap<String, Driver> classesMap = new ConcurrentHashMap<>();

	public Map<String, Driver> getRepository() {
		return Collections.unmodifiableMap(this.classesMap);
	}

	public void update(String file, Driver driver) {
		synchronized (this.classesMap) {
			this.classesMap.put(file, driver);
		}
		log.debug("Successfully updated the driver " + file);

	}

	public Driver getDriver(String file) {
		return this.classesMap.get(file);
	}

	public void remove(String file) {
		synchronized (this.classesMap) {
			this.classesMap.remove(file);
		}
		log.debug("Successfully removed  the driver " + file);
		
	}
}
