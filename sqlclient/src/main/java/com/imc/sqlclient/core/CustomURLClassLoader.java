package com.imc.sqlclient.core;

import java.net.URL;
import java.net.URLClassLoader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomURLClassLoader extends URLClassLoader {

	private ClassLoader parent;

	public CustomURLClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, null);
		this.parent = parent;
	}

	public void addURL(URL url) {
		super.addURL(url);
	}

	/**
	 * 1. Load the class with System Class Loader / JVM Class Loader 2. if it
	 * returns null, check if class already loaded. 3. Other wise check with parent
	 * class loader
	 */

	public Class<?> findClass(String className) throws ClassNotFoundException {

		try {
			try {
				Class<?> systemClass = findSystemClass(className);
				if (systemClass != null) {
					log.debug("System class loader found the class {}", className);
					return systemClass;
				}
			} catch (ClassNotFoundException e) {
			}

			Class<?> alreadyLoadedClass = super.findLoadedClass(className);
			if (alreadyLoadedClass != null) {
				return alreadyLoadedClass;
			}
			log.debug("Trying to load the class with URL class loader..");
			alreadyLoadedClass = super.findClass(className);
			log.debug("URLClassLoader found the class {}", className);
			return alreadyLoadedClass;

		} catch (ClassNotFoundException e) {
			log.debug("Tried all ways  but could not find class {} , delegating the task to Parent ClassLoader {} ",
					className, this.parent);
			return parent.loadClass(className);
		}
	}

}
