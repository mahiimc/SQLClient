package com.imc.sqlclient.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.imc.sqlclient.core.utils.ConnectionUtils;
import com.imc.sqlclient.core.utils.MetadataUtils;
import com.imc.sqlclient.dto.Column;
import com.imc.sqlclient.dto.ConnectionDetails;
import com.imc.sqlclient.dto.Metadata;
import com.imc.sqlclient.dto.Table;
import com.imc.sqlclient.exception.SQLClientException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MetadataService {

	private final ConnectionService connectionService;

	public List<Table> getTables(Integer connectionId) {
		List<Table> tables = new ArrayList<>();
		ConnectionDetails connection = connectionService.findConnectionById(connectionId.intValue());
		Connection dbConnection = ConnectionUtils.getConnection(connection);
		try {
			tables = MetadataUtils.getTables(dbConnection.getMetaData());
		} catch (SQLException e) {
			throw new SQLClientException(e);
		} finally {
			ConnectionUtils.closeConnection(dbConnection);
		}
		return tables;
	}

	public List<Column> getColumns(Integer connectionId, String tableName) {
		List<Column> columns = new ArrayList<>();
		ConnectionDetails connection = connectionService.findConnectionById(connectionId.intValue());
		Connection dbConnection = ConnectionUtils.getConnection(connection);
		try {
			columns = MetadataUtils.getColumns(dbConnection.getMetaData(),tableName);
		} catch (SQLException e) {
			throw new SQLClientException(e);
		} finally {
			ConnectionUtils.closeConnection(dbConnection);
		}
		return columns;
	}

}
