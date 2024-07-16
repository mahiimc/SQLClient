package com.imc.sqlclient.service;

import java.util.List;

import com.imc.sqlclient.dto.ConnectionDetails;
import com.imc.sqlclient.model.Connection;

public interface ConnectionService {
	
	boolean test(ConnectionDetails connectionDetails);
	ConnectionDetails save(ConnectionDetails connectionDetails);
	int delete(Connection connectionDetails);
	List<Connection> findAll();
	Connection findById(int id);
	ConnectionDetails findConnectionById(int id);
	int deleteById(int connectionId);
	boolean test(int connectionId);

}
