package com.imc.sqlclient.core;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.imc.sqlclient.exception.SQLClientException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum PluginClassesRepository {
	
	INSTANCE;

	public static PluginClassesRepository getInstance() {
		return INSTANCE;
	}

	private final ConcurrentHashMap<String, Plugin> classesMap = new ConcurrentHashMap<>();

	public Map<String, Plugin> getRepository() {
		return Collections.unmodifiableMap(this.classesMap);
	}

	public void update(String file, Plugin plugin) {
		check(file);
		synchronized (this.classesMap) {
			this.classesMap.put(file, plugin);
		}
		log.debug("Successfully updated the driver " + file);

	}

	public Plugin getPlugin(String file) {
		check(file);
		return this.classesMap.get(file);
	}

	public void remove(String file) {
		check(file);
		synchronized (this.classesMap) {
			this.classesMap.remove(file);
		}
		log.debug("Successfully removed  the driver " + file);
		
	}

	private void check(String file) {
		if (file == null) {
			throw new SQLClientException("file name should not be null");
		}
	}
}
