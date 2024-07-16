package com.imc.sqlclient.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imc.sqlclient.dto.Table;
import com.imc.sqlclient.dto.Column;
import com.imc.sqlclient.handlers.ResponseBuilder;
import com.imc.sqlclient.service.MetadataService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/metadata")
public class MetadataController {
	
	private final MetadataService metadataService;
	private final ResponseBuilder responseBuilder;
	
	@GetMapping("/{connectionId}")
	public ResponseEntity<Map<String,Object>>  findAllTables(@PathVariable("connectionId") Integer connectionId) {
		List<Table> tableList =  metadataService.getTables(connectionId);
		Map<String,Object> response =  responseBuilder.build(tableList);
		return ResponseEntity.ok(response);
	}
	@GetMapping("/{connectionId}/{tableName}")
	public ResponseEntity<Map<String,Object>>  findColumnsOfTable(@PathVariable("connectionId") Integer connectionId, 
																  @PathVariable("tableName")	String tableName) {
		List<Column> columnList =  metadataService.getColumns(connectionId,tableName);
		Map<String,Object> response =  responseBuilder.build(columnList);
		return ResponseEntity.ok(response);
	}


}
