package com.imc.sqlclient.boot;

import java.io.IOException;

import com.imc.sqlclient.core.ApplicationProperties;
import com.imc.sqlclient.core.DirectoryWatcher;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class BootTimeListener implements ServletContextListener {
	
	private DirectoryWatcher directoryWatcher = new DirectoryWatcher();
	public BootTimeListener() {
	}
	
	
	public void contextInitialized(ServletContextEvent sce) {
		
		@SuppressWarnings("unused")
		ApplicationProperties properties = ApplicationProperties.getInstance();
		Thread watcher = new Thread(this.directoryWatcher);
		watcher.setName("Directory_Watcher");
		watcher.start();
		
	}

	public void contextDestroyed(ServletContextEvent sce) {
		try {
			this.directoryWatcher.watcher.close();
		}
		catch (IOException e) {
		}
	}
}
