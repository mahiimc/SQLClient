package com.imc.sqlclient.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imc.sqlclient.dto.Query;
import com.imc.sqlclient.dto.ResultSetData;
import com.imc.sqlclient.handlers.ResponseBuilder;
import com.imc.sqlclient.service.DataService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/data")
@CrossOrigin(origins =  {"http://localhost:3000"})
public class DataController {
	
	private final DataService dataService;
	private final ResponseBuilder responseBuilder;
	
	@GetMapping("/{connectionId}/{tableName}")
	public ResponseEntity<Map<String,Object>>  fetchData(@PathVariable("connectionId") Integer connectionId, 
																  @PathVariable("tableName")	String tableName) {
		ResultSetData data =  dataService.fetchData(connectionId.intValue(), tableName);
		Map<String,Object> response =  responseBuilder.build(data);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/query")
	public ResponseEntity<Map<String,Object>>  executeQuery(@RequestBody Query query) {
		ResultSetData data =  dataService.executeQuery(query);
		Map<String,Object> response =  responseBuilder.build(data);
		return ResponseEntity.ok(response);
	}


}
