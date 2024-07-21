package com.imc.sqlclient.dto;

public record MetadataRequest(
		Integer connectionId,
		String schema,
		String catalog,
		String table
		) {

}
