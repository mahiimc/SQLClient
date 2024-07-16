package com.imc.sqlclient.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.imc.sqlclient.core.utils.PluginUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Plugin {
	
	private String id;
	private String name;
	private String title;
	private boolean isEnabled;
	private boolean isDriver;
	private List<String> entryPointClasses;
	private Set<String> classes;

	
	public Plugin(String name , Set<String> classes) {
		this.name = name;
		this.classes = classes;
		this.id = UUID.randomUUID().toString();
		this.entryPointClasses = new ArrayList<>();
		this.isEnabled = false;
		this.isDriver = false;
	}
	
	public void addJarFile(String jarFile) {
		this.classes.addAll(getClasses(jarFile));
	}
	
	public void deleteFile(String jarFile) {
		this.classes.removeAll(getClasses(jarFile));
	}
	
	private Set<String> getClasses( String jarFile ) {
		Set<String> jarClasses = new HashSet<>();
		if ( PluginUtils.isJar(jarFile)) {
			boolean exists = new File(jarFile).exists();
			if ( exists ) {
				jarClasses = PluginUtils.listClasses(jarFile);
			}
		}
		return jarClasses;
	}
	
}
