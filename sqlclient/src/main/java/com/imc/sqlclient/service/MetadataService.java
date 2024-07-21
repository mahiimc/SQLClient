package com.imc.sqlclient.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.imc.sqlclient.core.utils.ConnectionUtils;
import com.imc.sqlclient.core.utils.MetadataUtils;
import com.imc.sqlclient.dto.Column;
import com.imc.sqlclient.dto.ConnectionDetails;
import com.imc.sqlclient.dto.Metadata;
import com.imc.sqlclient.dto.MetadataRequest;
import com.imc.sqlclient.dto.Table;
import com.imc.sqlclient.exception.SQLClientException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MetadataService {

	private final ConnectionService connectionService;
	
	
	public List<Table> getTables(MetadataRequest request) {
		List<Table> tables = new ArrayList<>();
		ConnectionDetails connection = connectionService.findConnectionById(request.connectionId());
		Connection dbConnection = ConnectionUtils.getConnection(connection);
		try {
			tables = MetadataUtils.getTables(dbConnection.getMetaData(),request);
		} catch (SQLException e) {
			throw new SQLClientException(e);
		} finally {
			ConnectionUtils.closeConnection(dbConnection);
		}
		return tables;
	}

	public List<Column> getColumns(MetadataRequest request) {
		List<Column> columns = new ArrayList<>();
		ConnectionDetails connection = connectionService.findConnectionById(request.connectionId());
		Connection dbConnection = ConnectionUtils.getConnection(connection);
		try {
			columns = MetadataUtils.getColumns(dbConnection.getMetaData(),request);
		} catch (SQLException e) {
			throw new SQLClientException(e);
		} finally {
			ConnectionUtils.closeConnection(dbConnection);
		}
		return columns;
	}

	public Metadata  getSchema(MetadataRequest schemaRequest) {
		
		ConnectionDetails connection = connectionService.findConnectionById(schemaRequest.connectionId());
		Connection dbConnection = ConnectionUtils.getConnection(connection);
		try {
			return  MetadataUtils.getSchema(dbConnection.getMetaData(),schemaRequest);
		}
		catch (SQLException  e) {
			throw new SQLClientException(e);
		}
		finally {
			ConnectionUtils.closeConnection(dbConnection);
		}
	}

	public Metadata getCatalog(Integer connectionId) {
		ConnectionDetails connection = connectionService.findConnectionById(connectionId.intValue());
		Connection dbConnection = ConnectionUtils.getConnection(connection);
		try {
			return  MetadataUtils.getCatalogs(dbConnection.getMetaData());
		}
		catch (SQLException  e) {
			throw new SQLClientException(e);
		}
		finally {
			ConnectionUtils.closeConnection(dbConnection);
		}
	}

}
