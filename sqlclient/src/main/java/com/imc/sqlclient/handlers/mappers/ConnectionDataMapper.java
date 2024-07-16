package com.imc.sqlclient.handlers.mappers;

import com.imc.sqlclient.dto.ConnectionDetails;
import com.imc.sqlclient.model.Connection;

public class ConnectionDataMapper {
	
	public static Connection mapToModel(ConnectionDetails connectionDetails) {
		Connection connection = new Connection();
		connection.setDriver(connectionDetails.getDriverClassName());
		connection.setName(connectionDetails.getName());
		connection.setUsername(connectionDetails.getUserName());
		connection.setPassword(connectionDetails.getPassword());
		connection.setUrl(connectionDetails.getUrl());
		connection.setId(connectionDetails.getId());
		return connection;
	}
	
	
	
	public static ConnectionDetails mapToDTO(Connection connection ) {
		ConnectionDetails details = new ConnectionDetails();
		details.setDriverClassName(connection.getDriver());
		details.setId(connection.getId());
		details.setName(connection.getName());
		details.setPassword(connection.getPassword());
		details.setUrl(connection.getUrl());
		details.setUserName(connection.getUsername());
		
		return details;
	}

}
