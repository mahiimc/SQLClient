package com.imc.sqlclient.core.utils;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.imc.sqlclient.dto.Column;
import com.imc.sqlclient.dto.Metadata;
import com.imc.sqlclient.dto.MetadataRequest;
import com.imc.sqlclient.dto.Table;
import com.imc.sqlclient.exception.SQLClientException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MetadataUtils {
	private static final String[] TYPES = {"TABLE","VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY","LOCAL TEMPORARY", "ALIAS", "SYNONYM"};
	public static Metadata getSchema(DatabaseMetaData metadata, MetadataRequest schemaRequest) {
		Metadata resultMetadata = new Metadata();
		List<String> schemaList = new ArrayList<>();
		try {
			ResultSet resultSet = metadata.getSchemas(schemaRequest.catalog(),null);
			while (resultSet.next()) {
				schemaList.add(resultSet.getString("TABLE_SCHEM"));
			}
		} catch (SQLException e) {
			log.error("Error occurred while fetching schemas and catalogs");
			throw new SQLClientException(e);
		}
		resultMetadata.setSchemas(schemaList.isEmpty() ? null : schemaList);
		return resultMetadata;
	}

	public static List<Table> getTables(DatabaseMetaData metadata, MetadataRequest request) {
		List<Table> tableList = new ArrayList<>();
		try {
			ResultSet resultSet = metadata.getTables(request.catalog(), request.schema(), null,TYPES);

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

	public static List<Column> getColumns(DatabaseMetaData metadata, MetadataRequest request) {
		List<Column> columnList = new ArrayList<>();
		try {
			ResultSet resultSet = metadata.getColumns(request.catalog(), request.schema(), request.table(), null);

			while (resultSet.next()) {
				String name = resultSet.getString("COLUMN_NAME");
				String typeName = resultSet.getString("TYPE_NAME");
				Integer type = resultSet.getInt("DATA_TYPE");
				columnList.add(new Column(name, type, typeName));
			}

		} catch (SQLException e) {
			log.error("Error occurred while fetching tables");
		}
		return columnList;
	}

	public static Metadata getCatalogs(DatabaseMetaData metadata) {
		List<String> catalogs = new ArrayList<>();
		try {
			ResultSet catalogSet = metadata.getCatalogs();
			while (catalogSet.next()) {
				String cat = catalogSet.getString("TABLE_CAT");
				if (cat != null && !cat.equals("null"))
					catalogs.add(cat);
			}
		} catch (SQLException e) {
			log.error("Error occurred while fetching catalogs");
			throw new SQLClientException(e);
		}
		Metadata resultMetadata = new Metadata();
		resultMetadata.setCatalogs(catalogs.isEmpty() ? null : catalogs);
		return resultMetadata;
	}

}
