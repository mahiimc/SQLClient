package com.imc.sqlclient.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.imc.sqlclient.dto.ConnectionDetails;
import com.imc.sqlclient.exception.SQLClientException;
import com.imc.sqlclient.handlers.ConnectionTester;
import com.imc.sqlclient.handlers.mappers.ConnectionDataMapper;
import com.imc.sqlclient.repository.ConnectionRepository;
import com.imc.sqlclient.service.ConnectionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ConnectionServiceImpl implements ConnectionService {
	
	private final ConnectionRepository connectionRepo;
	private final ConnectionTester connectionTester;

	@Override
	public boolean test(ConnectionDetails connectionDetails) {
		return connectionTester.test(connectionDetails);
	}

	@Override
	public ConnectionDetails save(ConnectionDetails connectionDetails) {
		com.imc.sqlclient.model.Connection connection = ConnectionDataMapper.mapToModel(connectionDetails);
		com.imc.sqlclient.model.Connection dbConnection =  connectionRepo.save(connection);
		connectionDetails.setId(dbConnection.getId());
		return connectionDetails;
	}

	@Override
	public int delete(com.imc.sqlclient.model.Connection connection) {
		connectionRepo.delete(connection);
		return connection.getId();
	}

	@Override
	public List<com.imc.sqlclient.model.Connection> findAll() {
		return  connectionRepo.findAll();
	}

	@Override
	public com.imc.sqlclient.model.Connection findById(int id) {
		return connectionRepo.findById(id).orElseThrow(() ->  new SQLClientException("Connection does not exists"));
	}
	
	


	@Override
	public int deleteById(int connectionId) {
		com.imc.sqlclient.model.Connection connection =  findById(connectionId);
		return delete(connection);
	}

	@Override
	public boolean test(int connectionId) {
		ConnectionDetails connectionDetails = findConnectionById(connectionId);
		return connectionTester.test(connectionDetails);
	}

	@Override
	public ConnectionDetails findConnectionById(int id) {
		com.imc.sqlclient.model.Connection connectiom = findById(id);
		return ConnectionDataMapper.mapToDTO(connectiom);
	}

}
