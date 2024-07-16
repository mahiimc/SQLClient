package com.imc.sqlclient.boot;

import com.imc.sqlclient.core.DriverLoader;

public class BootTimeLoader implements Runnable  {

	@Override
	public void run() {
		DriverLoader driverLoader = new DriverLoader();
		driverLoader.load();
	}

}
