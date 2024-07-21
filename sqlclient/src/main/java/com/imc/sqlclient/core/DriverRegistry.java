package com.imc.sqlclient.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum DriverRegistry {
	INSTANCE;

	private final Set<String> entryPoints = new HashSet<>();
	public static DriverRegistry getInstance() {
		return INSTANCE;
	}
	
	public void addEntryPointClass(Set<String> classes) {
		entryPoints.addAll(classes);
	}
	
	public Set<String> getEntryPointClasses() {
		return Collections.unmodifiableSet(entryPoints);
	}

}