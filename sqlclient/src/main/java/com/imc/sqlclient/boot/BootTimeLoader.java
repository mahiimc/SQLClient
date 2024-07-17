package com.imc.sqlclient.boot;

import com.imc.sqlclient.core.DriverLoader;

public class BootTimeLoader implements Runnable  {
	
	private final String driverPath;
	
	public BootTimeLoader(String driverPath) {
		this.driverPath = driverPath;
	}

	@Override
	public void run() {
		DriverLoader driverLoader = new DriverLoader(driverPath);
		driverLoader.load();
	}

}
