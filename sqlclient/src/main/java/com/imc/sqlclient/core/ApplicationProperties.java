package com.imc.sqlclient.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imc.sqlclient.boot.BootTimeLoader;

public enum ApplicationProperties {

	INSTANCE;

	private ApplicationProperties() {

		Logger log = LoggerFactory.getLogger(ApplicationProperties.class);
		log.info("Inside the ApplicationProperties enum");
		BootTimeLoader loader = new BootTimeLoader();
		Thread driverLoaderThread = new Thread(loader);
		driverLoaderThread.setPriority(Thread.MAX_PRIORITY);
		driverLoaderThread.setName("DRIVER_LOADER");
		log.debug("Starting the Driver_Loader");
		try {
			driverLoaderThread.start();
			driverLoaderThread.join();
			log.debug("Successfully loaded all the drivers.");
		} catch (InterruptedException e) {
			log.error("Error occurred while loading the Drivers");
		}
	}
	
	public static ApplicationProperties getInstance() {
		return INSTANCE;
	}

}
