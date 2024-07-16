package com.imc.sqlclient.connection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class DriverShim  implements Driver  {
	
	private final  Driver driver;
	
	public DriverShim(Driver driver) {
		this.driver = driver;
	}

	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		return driver.connect(url, info);
	}

	@Override
	public boolean acceptsURL(String url) throws SQLException {
		return driver.acceptsURL(url);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		return driver.getPropertyInfo(url, info);
	}

	@Override
	public int getMajorVersion() {
		return driver.getMajorVersion();
	}

	@Override
	public int getMinorVersion() {
		return driver.getMinorVersion();
	}

	@Override
	public boolean jdbcCompliant() {
		return driver.jdbcCompliant();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return driver.getParentLogger();
	}
	
	
	public String getDriverName() {
		return driver.getClass().getName();
	}

}
