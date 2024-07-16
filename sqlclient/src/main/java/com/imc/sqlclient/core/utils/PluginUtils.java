package com.imc.sqlclient.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.reflections.Reflections;

import com.imc.sqlclient.core.CustomClassLoader;
import com.imc.sqlclient.core.Plugin;
import com.imc.sqlclient.core.PluginClassesRepository;
import com.imc.sqlclient.core.PluginFinder;
import com.imc.sqlclient.core.PluginResgistry;
import com.imc.sqlclient.exception.SQLClientException;

public class PluginUtils {

	private static final String JAR_EXTENSION = ".jar";
	private static final PluginResgistry REG = PluginResgistry.getInstance();

	public static Set<String> listClasses(String jarPath) {
		Set<String> classNames = new HashSet<>();
		try (JarInputStream jar = new JarInputStream(new FileInputStream(jarPath))) {
			JarEntry entry = jar.getNextJarEntry();
			while (entry != null) {

				if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
					String className = entry.getName().replace("/", ".");
					classNames.add(className.substring(0, className.length() - ".class".length()));
				}

				else if (entry.getName().startsWith("META-INF/services/java.sql.Driver")) {
					Set<String> entryPoints = readServiceFile(jarPath, entry);
					REG.addEntryPoints(entryPoints);
				}

				entry = jar.getNextJarEntry();
			}

		} catch (IOException e) {
			throw new SQLClientException("Error occurred while loading the classes of " + jarPath);
		}
		return classNames;
	}

	public static boolean isJar(String jarPath) {
		return jarPath.endsWith(JAR_EXTENSION);
	}

	public static boolean isDriver(String driver, CustomClassLoader classLoader) {
		try {
			Class<?> loadedClass = Class.forName(driver, true, classLoader);
			boolean assignableForm = Driver.class.isAssignableFrom(loadedClass);
			if (!assignableForm) {
				@SuppressWarnings("unused")
				Driver driverInstance = (Driver) loadedClass.getDeclaredConstructor().newInstance();
			}
			return true;
		} catch (ClassNotFoundException e) {
			throw new SQLClientException("Could not load the class " + driver);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new SQLClientException(e);
		}
	}

	public static List<String> getLoadedClasses(ClassLoader classLoader) {
		try {
			Field field = ClassLoader.class.getDeclaredField("classes");
			List<Class<?>> classes = (List<Class<?>>) field.get(classLoader);
			List<String> loadedClasses = new ArrayList<>();
			for (Class<?> clazz : classes) {
				loadedClasses.add(clazz.getName());
			}
			return loadedClasses;
		} catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
			throw new SQLClientException(e);
		}

	}

	private static Set<String> readServiceFile(String jarPath, JarEntry entry) {
		Set<String> lines = new HashSet<>();
		try {
			File jarFile = new File(jarPath);
			URL url = jarFile.toURI().toURL();
			String actualPath = "jar:" + url.toString() + "!/" + entry.getName();
			String serviceFilePath = entry.getName().toString();
			try (InputStream inputStream = new URL(actualPath).openStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
				if (inputStream == null) {
					throw new Exception("Service file not found: " + serviceFilePath);
				}
				String line;
				while ((line = reader.readLine()) != null) {
					lines.add(line.trim());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
		}

		return lines;
	}

}
