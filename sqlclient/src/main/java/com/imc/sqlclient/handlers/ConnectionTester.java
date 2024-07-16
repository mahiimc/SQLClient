package com.imc.sqlclient.handlers;

import java.sql.Connection;

import org.springframework.stereotype.Component;

import com.imc.sqlclient.core.utils.ConnectionUtils;
import com.imc.sqlclient.dto.ConnectionDetails;

@Component
public class ConnectionTester {

	public boolean test(ConnectionDetails connectionDetails) {
		Connection connection = null;
		connection = ConnectionUtils.getConnection(connectionDetails);
		boolean isNotNull = connection != null;
		ConnectionUtils.closeConnection(connection);
		return isNotNull;
	}

}
