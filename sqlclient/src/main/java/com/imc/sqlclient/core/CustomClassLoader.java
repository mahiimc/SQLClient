package com.imc.sqlclient.core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.imc.sqlclient.exception.SQLClientException;

public class CustomClassLoader extends ClassLoader implements AutoCloseable  {
	
	private final CustomURLClassLoader urlClassLoader;
	
	public CustomClassLoader( List<URL> classPath ) {
		super(CustomClassLoader.class.getClassLoader());
		URL[] urls = classPath.toArray(new URL[classPath.size()]);
		this.urlClassLoader = new CustomURLClassLoader(urls, this.getParent());
	}

	@Override
	public void close() throws Exception {
		this.urlClassLoader.close();
	}
	
	public void addURL(File file) throws MalformedURLException {
		this.urlClassLoader.addURL(file.toURI().toURL());
	}
	
	@Override
	public synchronized Class<?> loadClass(String className)  {
		try {
			return this.urlClassLoader.findClass(className);
		}
		catch (ClassNotFoundException e) {
			throw new SQLClientException("Could not load " + className);
		}
	}
	
}
