package com.imc.sqlclient.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.imc.sqlclient.dto.Column;
import com.imc.sqlclient.dto.ResultSetData;
import com.imc.sqlclient.exception.SQLClientException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResultSetUtils {

	private ResultSetUtils() {
		// NOOP
	}

	public static ResultSetData getDataFromResultSet(final ResultSet resultSet) {
		try {
			ResultSetData resultSetData = new ResultSetData();

			ResultSetMetaData metadata = resultSet.getMetaData();
			int columnCount = metadata.getColumnCount();
			List<Map<String, Object>> data = new ArrayList<>();
			List<Column> columnList = new ArrayList<Column>();

			for (int i = 1; i <= columnCount; i++) {
				String columnName = metadata.getColumnName(i);
				int dataType = metadata.getColumnType(i);
				String typeName = metadata.getColumnTypeName(i);
				Column column = new Column(columnName, dataType, typeName);
				columnList.add(column);
			}

			while (resultSet.next()) {
				Map<String, Object> values = new LinkedHashMap<>();
				for (int i = 1; i <= columnCount; i++) {
					String columnName = metadata.getColumnName(i);
					values.put(columnName, resultSet.getObject(i));
				}
				data.add(values);
			}

			resultSetData.setColumns(columnList);
			resultSetData.setResult(data);
			resultSetData.setSize(data.size());
			return resultSetData;
		} catch (SQLException e) {
			log.error("",e);
			throw new SQLClientException(e);
		}

	}
}
