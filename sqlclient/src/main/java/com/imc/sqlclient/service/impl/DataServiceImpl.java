package com.imc.sqlclient.service.impl;

import java.sql.Connection;

import org.springframework.stereotype.Service;

import com.imc.sqlclient.core.utils.ConnectionUtils;
import com.imc.sqlclient.dto.ConnectionDetails;
import com.imc.sqlclient.dto.Query;
import com.imc.sqlclient.dto.ResultSetData;
import com.imc.sqlclient.handlers.QueryExecuteHandler;
import com.imc.sqlclient.service.ConnectionService;
import com.imc.sqlclient.service.DataService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DataServiceImpl implements DataService {

	private final ConnectionService connectionService;
	private final QueryExecuteHandler queryExecuteHandler;

	@Override
	public ResultSetData fetchData(int connectionId, String tableName) {

		Connection connection = getConnection(connectionId);
		String query = "SELECT * FROM " + tableName+";";
		return queryExecuteHandler.executeQuery(connection, query);
	}

	@Override
	public ResultSetData executeQuery(Query query) {
		int connectionId = query.getConnectionId();
		String queryString = query.getQuery();
		Connection connection = getConnection(connectionId);
		return queryExecuteHandler.executeQuery(connection, queryString);
	}

	private final Connection getConnection(int connectionId) {
		ConnectionDetails connectionDetails = connectionService.findConnectionById(connectionId);
		return ConnectionUtils.getConnection(connectionDetails);
	}

}
