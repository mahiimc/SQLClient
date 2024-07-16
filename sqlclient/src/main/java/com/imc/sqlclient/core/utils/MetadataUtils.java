package com.imc.sqlclient.core.utils;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.imc.sqlclient.dto.Column;
import com.imc.sqlclient.dto.Table;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MetadataUtils {

	public static List<Table> getTables(DatabaseMetaData metadata) {
		List<Table> tableList = new ArrayList<>();
		try {
			ResultSet resultSet = metadata.getTables(null, null, "", null);

			while (resultSet.next()) {
				String name = resultSet.getString("TABLE_NAME");
				String type = resultSet.getString("TABLE_TYPE");
				tableList.add(new Table(name, type));
			}

		} catch (SQLException e) {
			log.error("Error occurred while fetching tables");
		}
		return tableList;
	}

	public static List<Column> getColumns(DatabaseMetaData metadata, String tableName) {
		List<Column> columnList = new ArrayList<>();
		try {
			ResultSet resultSet = metadata.getColumns(null, null, tableName, null);

			while (resultSet.next()) {
				String name = resultSet.getString("COLUMN_NAME");
				String typeName = resultSet.getString("TYPE_NAME");
				Integer type = resultSet.getInt("DATA_TYPE");
				columnList.add(new Column(name, type,typeName));
			}

		} catch (SQLException e) {
			log.error("Error occurred while fetching tables");
		}
		return columnList;
	}

}
