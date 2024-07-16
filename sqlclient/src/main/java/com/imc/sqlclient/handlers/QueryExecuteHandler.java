package com.imc.sqlclient.handlers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Component;

import com.imc.sqlclient.dto.ResultSetData;
import com.imc.sqlclient.exception.SQLClientException;
import com.imc.sqlclient.utils.ResultSetUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class QueryExecuteHandler {
	
	
	
	public ResultSetData executeQuery(Connection connection ,  String query) {
		log.debug("Executing Query: {}",query);
		try (Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery(query);
			return ResultSetUtils.getDataFromResultSet(resultSet);
		} catch (SQLException e) {
			throw new SQLClientException(e);
		}
	}

}
