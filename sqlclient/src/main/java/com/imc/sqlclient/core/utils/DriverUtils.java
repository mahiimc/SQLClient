package com.imc.sqlclient.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import com.imc.sqlclient.core.DriverRegistry;
import com.imc.sqlclient.exception.SQLClientException;

public class DriverUtils {

	private static final String JAR_EXTENSION = ".jar";
	private static final DriverRegistry REG = DriverRegistry.getInstance();

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
					REG.addEntryPointClass(entryPoints);
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
