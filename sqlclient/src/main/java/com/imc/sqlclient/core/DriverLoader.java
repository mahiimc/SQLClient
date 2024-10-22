package com.imc.sqlclient.core;

import java.io.File;
import java.util.Set;

import com.imc.sqlclient.core.utils.DriverUtils;

public class DriverLoader {
	
	private  String path;
	
	public DriverLoader() {
		
	}
	
	public DriverLoader(String path) {
		this.path = path;
	}

	private static final RegisteredClassesRepository REPO = RegisteredClassesRepository.getInstance();


	public synchronized void load() {
		File[] files = new File(path).listFiles();
		if (files != null) {
			load(files);
		}
	}

	public void load(File[] files) {
		for (File file : files) {
			String absPath = file.getAbsolutePath();
			if (file.isFile()) {
				loadFile(absPath);
			}
		}
	}

	public void loadFile(String absPath) {

		if (DriverUtils.isJar(absPath) ) {
			Set<String> classes = DriverUtils.listClasses(absPath);
			Driver driver = new Driver(absPath, classes);
			REPO.update(absPath, driver);
		}
	}

	public synchronized void deleteEntry(File file) {
		String absPath = file.getAbsolutePath();
		Driver plugin = REPO.getDriver(absPath);
		String directory = file.getParent();
		if (isWatchedDirectory(directory)) {
			REPO.remove(absPath);
		}
		else if (plugin != null) {
			plugin.deleteFile(absPath);
		}
	}
	
	public synchronized void makeEntry(File file) {
		String absPath = file.getAbsolutePath();
		Driver plugin = REPO.getDriver(absPath);
		String directory = file.getParent();
		if (isWatchedDirectory(directory)) {
			loadFile(absPath);
		}
		else if (plugin != null) {
			plugin.addJarFile(absPath);
		}
	}

	private boolean isWatchedDirectory(String directory) {
		directory = new File(directory).getAbsolutePath();
		return directory.equalsIgnoreCase(path);
	}

}
