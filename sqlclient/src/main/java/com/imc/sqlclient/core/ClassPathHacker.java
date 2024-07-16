package com.imc.sqlclient.core;

import com.imc.sqlclient.exception.SQLClientException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClassPathHacker {

	private final PluginFinder pluginFinder = new PluginFinder();

	public Class<?> getClass(String clazz) throws ClassNotFoundException {
		try {
			return getClassPathClass(clazz);
		} catch (SQLClientException e) {
			log.debug("Could not find the class in the classpath, trying to load it from plugins.");
			return getClassFromPlugin(clazz);
		}
	}

	private Class<?> getClassFromPlugin(String clazz) throws ClassNotFoundException {
		PluginResgistry registry = PluginResgistry.getInstance();

		CustomClassLoader classLoader = registry.getPluginClassLoader(clazz);

		if (classLoader != null) {
			return Class.forName(clazz, true, classLoader);
		} else {
			return loadPlugin(clazz);
		}
	}

	private Class<?> loadPlugin(String clazz) {
		try {
			CustomClassLoader classLoader = pluginFinder.getPluginClassLoader(clazz);
			return getClassUsingPluginClassLoader(clazz, classLoader);
		} catch (ClassNotFoundException e) {
			throw new SQLClientException(clazz);
		}
	}

	public Class<?> load(String clazz) throws ClassNotFoundException {
		return Class.forName(clazz);
	}

	public Class<?> getClassPathClass(String clazz) {
		try {
			return Class.forName(clazz);
		} catch (ClassNotFoundException e) {
			throw new SQLClientException("Could not load class");
		}
	}

	public Class<?> getClassUsingPluginClassLoader(String clazz, CustomClassLoader pluginClassLoader)
			throws ClassNotFoundException {
		return Class.forName(clazz, true, pluginClassLoader);
	}

}
