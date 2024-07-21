package com.imc.sqlclient.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imc.sqlclient.dto.Column;
import com.imc.sqlclient.dto.Metadata;
import com.imc.sqlclient.dto.MetadataRequest;
import com.imc.sqlclient.dto.Table;
import com.imc.sqlclient.handlers.ResponseBuilder;
import com.imc.sqlclient.service.MetadataService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/metadata")
@CrossOrigin(origins =  {"http://localhost:3000"})
public class MetadataController {
	
	private final MetadataService metadataService;
	private final ResponseBuilder responseBuilder;
	
	@PostMapping("/catalog")
	public ResponseEntity<Map<String,Object>>  getCatalog(@RequestBody MetadataRequest schemaRequest) {
		Metadata metadata =  metadataService.getCatalog(schemaRequest.connectionId());
		Map<String,Object> response =  responseBuilder.build(metadata);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/schema")
	public ResponseEntity<Map<String,Object>>  getSchema(@RequestBody MetadataRequest schemaRequest) {
		Metadata metadata =  metadataService.getSchema(schemaRequest);
		Map<String,Object> response =  responseBuilder.build(metadata);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/tables")
	public ResponseEntity<Map<String,Object>>  findSchemaTables(@RequestBody MetadataRequest tableRequest) {
		
		List<Table> tableList =  metadataService.getTables(tableRequest);
		Map<String,Object> response =  responseBuilder.build(tableList);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/columns")
	public ResponseEntity<Map<String,Object>>  findColumnsOfTable(@RequestBody MetadataRequest columnRequest) {
		List<Column> columnList =  metadataService.getColumns(columnRequest);
		Map<String,Object> response =  responseBuilder.build(columnList);
		return ResponseEntity.ok(response);
	}


}
