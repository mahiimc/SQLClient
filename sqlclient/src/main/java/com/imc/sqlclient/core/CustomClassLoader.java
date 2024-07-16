package com.imc.sqlclient.core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.imc.sqlclient.exception.SQLClientException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomClassLoader extends ClassLoader implements AutoCloseable  {
	
	private CustomUrlClassLoader urlClassLoader;
	
	public CustomClassLoader( List<URL> classPath ) {
		super(CustomClassLoader.class.getClassLoader());
		URL[] urls = classPath.toArray(new URL[classPath.size()]);
		this.urlClassLoader = new CustomUrlClassLoader(urls, this.getParent());
	}

	@Override
	public void close() throws Exception {
		this.urlClassLoader.close();
	}
	
	public void addURL(File file) throws MalformedURLException {
		this.urlClassLoader.addURL(file.toURI().toURL());
	}
	
	
	public List<String> getReg() {
		return Collections.unmodifiableList(this.urlClassLoader.getReg());
	}
	
	
	@Override
	public Class<?> loadClass(String className, boolean resolve)  {
		try {
			return this.urlClassLoader.findClass(className);
		}
		catch (ClassNotFoundException e) {
			throw new SQLClientException("Could not load " + className);
		}
	}
	
	
	private static class CustomUrlClassLoader extends URLClassLoader {
		private ClassLoader parent;
		private List<String> reg;
		
		public CustomUrlClassLoader(URL[] urls, ClassLoader parent) {
			super(urls,null);
			this.parent = parent;
			this.reg = new ArrayList<>();
		}
		
		public void addURL(URL url) {
			super.addURL(url);
		}
		
		public List<String> getReg() {
			return reg;
		}
		
		
		/**
		 * 1. Load the class with System Class Loader / JVM Class Loader
		 * 2. if it returns null, check if class already loaded.
		 * 3. Other wise check with parent class loader
		 */
		
		
		public Class<?>  findClass(String className) throws ClassNotFoundException  {
			
			try {
				try {
					Class<?> systemClass = findSystemClass(className);
					if ( systemClass != null ) {
						log.debug("System class loader found the class {}",className);
						return systemClass;
					}
				}
				catch (ClassNotFoundException e) {
					// JUST IGNORE
					log.debug("System class loader did not find the class {}", className);
				}
				
				Class<?> alreadyLoadedClass = super.findLoadedClass(className);
				if ( alreadyLoadedClass != null ) {
					log.debug("{} class already loaded",className);
					return alreadyLoadedClass;
				}
				log.debug("Trying to load the class with URL class loader..");
				alreadyLoadedClass = super.findClass(className);
				reg.add(alreadyLoadedClass.getName());
				log.debug("URLClassLoader found the class {}",className);
				return alreadyLoadedClass;
				
			}
			catch (ClassNotFoundException e) {
				log.debug("Tried all ways  but could not find class {} , delegating the task to Parent ClassLoader {} ",className, this.parent);
				return parent.loadClass(className);
			}
		}
	}

}
