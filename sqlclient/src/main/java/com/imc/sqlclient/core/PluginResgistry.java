package com.imc.sqlclient.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.imc.sqlclient.core.utils.PluginUtils;
import com.imc.sqlclient.exception.SQLClientException;

public enum PluginResgistry {
	
	INSTANCE;
	
	private final Map<String,PluginEntry> enabledPlugins = new ConcurrentHashMap<>();
	private final Set<String> entryPoints = new HashSet<>();
	
	public static PluginResgistry getInstance() {
		return INSTANCE;
	}
	
	public Map<String,PluginEntry> getEnabledPlugins() {
		return Collections.unmodifiableMap(enabledPlugins);
	}
	
	public CustomClassLoader getPluginClassLoader(String clazz) {
		PluginEntry entry = this.enabledPlugins.get(clazz);
		if ( entry != null ) {
			return entry.getCustomClassLoader();
		}
		return null;
	}
	
	public void addEntryPoints(Set<String> entryPoints) {
		this.entryPoints.addAll(entryPoints);
	}
	
	public Set<String> findEntryPoints() {
		return Collections.unmodifiableSet(entryPoints);
	}
	
	public PluginEntry registerPlugin(String entryPointClass, PluginEntry pluginEntry ) {
		if ( entryPointClass == null || pluginEntry == null ) {
			throw new SQLClientException("Params should not be null");
		}
		
		Plugin plugin = pluginEntry.getPlugin();
		plugin.setEnabled(true);
		plugin.getEntryPointClasses().add(entryPointClass);
		
		if (PluginUtils.isDriver(entryPointClass, pluginEntry.getCustomClassLoader())) {
			plugin.setDriver(true);
		}
		return this.enabledPlugins.putIfAbsent(entryPointClass, pluginEntry);
	} 
}
