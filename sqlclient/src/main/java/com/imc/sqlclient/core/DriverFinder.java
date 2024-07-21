package com.imc.sqlclient.core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.imc.sqlclient.exception.SQLClientException;


public class DriverFinder {

	private static final RegisteredClassesRepository REPO = RegisteredClassesRepository.getInstance();
	
	public CustomClassLoader getPluginClassLoader(String clazz) {
		Driver driver = getDriver(clazz);
		List<URL> urlList;
		try {
			urlList = getUrls(driver.getName());
			if (urlList.isEmpty() ) {
				throw new SQLClientException("Plugin does not contains resources");
			}
		}
		catch (MalformedURLException e) {
			throw new SQLClientException(e);
		}
		CustomClassLoader customClassLoader = new CustomClassLoader(urlList);
		return customClassLoader;
	}
	
	private Driver getDriver(String name) {
		Map<String,Driver> repository =  REPO.getRepository();
		Driver plugin = null;
		for(Map.Entry<String, Driver> entry : repository.entrySet() ) {
			plugin  = entry.getValue();
			Set<String> classess =  plugin.getClasses();
			if (classess.contains(name)) {
				break;
			}
		}
		if ( plugin == null ) {
			throw new SQLClientException("Could not find the class");
		}
		return plugin;
	}
	
	private List<URL> getUrls(String plugin) throws MalformedURLException {
		List<URL> urlList = new ArrayList<>();
		File file = new File(plugin);
		urlList.add(file.toURI().toURL());
		return urlList;
		
	}
}
