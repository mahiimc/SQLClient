package com.imc.sqlclient.core.utils;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.imc.sqlclient.connection.DriverShim;
import com.imc.sqlclient.core.CustomClassLoader;
import com.imc.sqlclient.core.PluginFinder;
import com.imc.sqlclient.dto.ConnectionDetails;
import com.imc.sqlclient.exception.SQLClientException;

public class ConnectionUtils {

	public static Connection getConnection(ConnectionDetails connection) {

		final String url = connection.getUrl();
		final String username = connection.getUserName();
		final String password = connection.getPassword();
		final String driver = connection.getDriverClassName();
		try {
			CustomClassLoader classLoader =  new PluginFinder().getPluginClassLoader(driver);
			Class<?> clazz =  classLoader.loadClass(driver);
			Driver driverShim = (Driver) clazz.getDeclaredConstructor().newInstance();
			DriverManager.registerDriver(new DriverShim(driverShim));
			return  DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			throw new SQLClientException(e);
		}
	}

	public static void closeConnection(Connection connection) {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			throw new SQLClientException(e);
		}
	}

}
