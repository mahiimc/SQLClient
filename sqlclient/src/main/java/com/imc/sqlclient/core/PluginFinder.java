package com.imc.sqlclient.core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.imc.sqlclient.exception.SQLClientException;


public class PluginFinder {

	private static final PluginClassesRepository REPO = PluginClassesRepository.getInstance();
	
	public CustomClassLoader getPluginClassLoader(String clazz) {
		Plugin plugin = getPlugin(clazz);
		List<URL> urlList;
		try {
			urlList = getUrls(plugin.getName());
			if (urlList.isEmpty() ) {
				throw new SQLClientException("Plugin does not contains resources");
			}
		}
		catch (MalformedURLException e) {
			throw new SQLClientException(e);
		}
		
		CustomClassLoader customClassLoader = new CustomClassLoader(urlList);
		
		final PluginResgistry registry = PluginResgistry.getInstance();
		
		PluginEntry pluginEntry = new PluginEntry(plugin, customClassLoader);
		PluginEntry oldEntry = registry.registerPlugin(clazz, pluginEntry);
		
		if ( oldEntry != null ) {
			CustomClassLoader classLoader = oldEntry.getCustomClassLoader();
			if ( classLoader != null ) {
				try {
					classLoader.close();
				}
				catch (Exception e) {
				}
			}
			Plugin oldEntryPlugin = oldEntry.getPlugin();
			oldEntryPlugin.setEnabled(false);
		}
		
		return customClassLoader;
	}
	
	private Plugin getPlugin(String name) {
		Map<String,Plugin> repository =  REPO.getRepository();
		Plugin plugin = null;
		for(Map.Entry<String, Plugin> entry : repository.entrySet() ) {
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
