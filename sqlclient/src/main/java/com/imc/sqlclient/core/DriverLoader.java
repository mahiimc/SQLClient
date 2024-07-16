package com.imc.sqlclient.core;

import java.io.File;
import java.util.Set;

import com.imc.sqlclient.core.utils.PluginUtils;

public class DriverLoader {

	private static final PluginClassesRepository REPO = PluginClassesRepository.getInstance();

	private static final String DRIVER_PATH = "D:\\Project\\sqlclient\\src\\main\\resources\\drivers";

	public synchronized void load() {
		File[] files = new File(DRIVER_PATH).listFiles();
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

		if (PluginUtils.isJar(absPath) && new File(absPath).exists()) {
			Set<String> classes = PluginUtils.listClasses(absPath);
			Plugin plugin = new Plugin(absPath, classes);
			REPO.update(absPath, plugin);
		}
	}

	public synchronized void deleteEntry(File file) {
		String absPath = file.getAbsolutePath();
		Plugin plugin = REPO.getPlugin(absPath);
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
		Plugin plugin = REPO.getPlugin(absPath);
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
		return directory.equalsIgnoreCase(DRIVER_PATH);
	}

}
