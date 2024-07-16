package com.imc.sqlclient.connection;

import java.lang.reflect.Constructor;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import com.imc.sqlclient.core.ClassPathHacker;
import com.imc.sqlclient.exception.SQLClientException;

public class DriverUtility {

	public static boolean isRegistered(String clazz) {
		if (clazz == null) {
			return false;
		}

		Enumeration<Driver> enumeration = DriverManager.getDrivers();

		while (enumeration.hasMoreElements()) {
			Driver driver = enumeration.nextElement();
			if (driver instanceof DriverShim && clazz.equals(((DriverShim) driver).getDriverName())) {
				return true;
			}
		}
		return false;

	}

	public static void register(String clazz) throws SQLException {
		if (!isRegistered(clazz)) {
			Object instance = createObject(clazz);
			DriverShim driver = new DriverShim((Driver) instance);
			DriverManager.registerDriver(driver);
		}

	}

	private static final Object createObject(String clazz) {
		try {
			Class<?> clazzObj = new ClassPathHacker().getClass(clazz);
			Constructor<?> constructor = (Constructor<?>) clazzObj.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (Exception e) {
			throw new SQLClientException(e);
		}
	}
}
