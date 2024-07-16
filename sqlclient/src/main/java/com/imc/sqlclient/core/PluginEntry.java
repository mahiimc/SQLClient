package com.imc.sqlclient.core;

import lombok.Data;

@Data
public class PluginEntry {
	
	private Plugin plugin;
	private CustomClassLoader customClassLoader;
	
	public PluginEntry( Plugin plugin, CustomClassLoader classLoader ) {
		this.plugin = plugin;
		this.customClassLoader = classLoader;
	}

}
