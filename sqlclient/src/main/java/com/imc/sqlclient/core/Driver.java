package com.imc.sqlclient.core;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.imc.sqlclient.core.utils.DriverUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Driver {
	
	private String id;
	private String name;
	private String title;
	private Set<String> classes;

	
	public Driver(String name , Set<String> classes) {
		this.name = name;
		this.classes = classes;
		this.id = UUID.randomUUID().toString();
	}
	
	public void addJarFile(String jarFile) {
		this.classes.addAll(getClasses(jarFile));
	}
	
	public void deleteFile(String jarFile) {
		this.classes.removeAll(getClasses(jarFile));
	}
	
	private Set<String> getClasses( String jarFile ) {
		Set<String> jarClasses = new HashSet<>();
		if ( DriverUtils.isJar(jarFile)) {
			boolean exists = new File(jarFile).exists();
			if ( exists ) {
				jarClasses = DriverUtils.listClasses(jarFile);
			}
		}
		return jarClasses;
	}
	
}
