package com.imc.sqlclient.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.imc.sqlclient.core.Plugin;
import com.imc.sqlclient.core.PluginClassesRepository;

public class DriverListLoaderUtility {
	
	
	public static List<String> getPluginClasses() {
		List<String> pluginClasses = new ArrayList<>();
		PluginClassesRepository repository = PluginClassesRepository.getInstance();
		Map<String,Plugin> plugins =  repository.getRepository();
		plugins.forEach((key,value)-> {
			pluginClasses.addAll(value.getClasses());
		});
		return pluginClasses;
	}
	
	public static List<String> getCoreClasses() {
		ClassLoader classLoader = DriverListLoaderUtility.class.getClassLoader();
		return   PluginUtils.getLoadedClasses(classLoader);
	}
	
	public static List<String> getAllClasses() {
		List<String> loadedClasses = getCoreClasses();
		loadedClasses.addAll(getPluginClasses());
		return loadedClasses;
	}

}
