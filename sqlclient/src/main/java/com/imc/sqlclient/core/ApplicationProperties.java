package com.imc.sqlclient.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imc.sqlclient.boot.BootTimeLoader;
import com.imc.sqlclient.boot.ConfigLoader;

public enum ApplicationProperties {

	INSTANCE;
	
	private  String driverPath;

	private ApplicationProperties() {

		Logger log = LoggerFactory.getLogger(ApplicationProperties.class);
		log.info("Inside the ApplicationProperties enum");
		
		ConfigLoader configLoader = new ConfigLoader(this);
		
		ExecutorService executorService = Executors.newFixedThreadPool(1);
		try {
			executorService.submit(configLoader);
			executorService.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.error("Error occurred while loading the Drivers");
		} finally {
			executorService.close();
		}
		
		BootTimeLoader loader = new BootTimeLoader(this.driverPath);
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
	
	
	public void setDriverPath(String driverPath) {
		this.driverPath = driverPath;
	}
	
	public  String getDriverPath() {
		return driverPath;
	}

}
