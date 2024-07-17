package com.imc.sqlclient.boot;

import java.io.InputStream;
import java.util.concurrent.Callable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.imc.sqlclient.core.ApplicationProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigLoader implements Callable<Boolean> {

	private static final String CONFIG_FILE_NAME = "config.json";

	private final ApplicationProperties properties;

	public ConfigLoader(ApplicationProperties properties) {
		this.properties = properties;
	}

	@Override
	public Boolean call() {
		try {
			log.debug("Loading Config");
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream(CONFIG_FILE_NAME);
			ObjectNode configNode = new ObjectMapper().readValue(inputStream, ObjectNode.class);
			String driverPath = configNode.get("driverPath").asText();
			this.properties.setDriverPath(driverPath);
			log.debug("Loaded config successfully.");
			return true;
		} catch (Exception e) {
			log.error("Error occurred while loading config : ",e);
			return false;
		}
	}

}
