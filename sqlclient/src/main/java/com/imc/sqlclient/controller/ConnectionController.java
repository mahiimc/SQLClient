package com.imc.sqlclient.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imc.sqlclient.dto.ConnectionDetails;
import com.imc.sqlclient.handlers.ResponseBuilder;
import com.imc.sqlclient.handlers.mappers.ConnectionDataMapper;
import com.imc.sqlclient.model.Connection;
import com.imc.sqlclient.service.ConnectionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/connection")
@CrossOrigin(origins =  {"http://localhost:3000"})
public class ConnectionController {

	private final ConnectionService connectionService;
	private final ResponseBuilder responseBuilder;

	@PostMapping("/test")
	public ResponseEntity<Map<String,Object>> testConnection(@RequestBody ConnectionDetails body) {
		boolean isSuccess = connectionService.test(body);
		String message = isSuccess ? "Connection test successful" : "Unable to establish connection";
		HttpStatus status = isSuccess ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		Map<String,Object> response = new HashMap<>();
		response.put("message", message);
		return ResponseEntity.status(status).body(response);
	}
	@GetMapping("/test/{id}")
	public ResponseEntity<Map<String,Object>> testExistingConnection(@PathVariable("id") int connectionId) {
		boolean isSuccess = connectionService.test(connectionId);
		String message = isSuccess ? "Connection test successful" : "Unable to establish connection";
		HttpStatus status = isSuccess ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		Map<String,Object> response = new HashMap<>();
		response.put("message", message);
		return ResponseEntity.status(status).body(response);
	}

	@PostMapping("/save")
	public ResponseEntity<Map<String, Object>> saveConnection(@RequestBody ConnectionDetails body) {
		ConnectionDetails connectionDetails = connectionService.save(body);
		String message = "Connection saved successfully.";
		Map<String, Object> response = responseBuilder.build(connectionDetails);
		response.put("message", message);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> listConnections() {

		List<ConnectionDetails> connectionDetails = connectionService.findAll().stream()
				.map(connection -> ConnectionDataMapper.mapToDTO(connection)).toList();

		Map<String, Object> response = responseBuilder.build(connectionDetails);
		response.put("size", connectionDetails.size());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findConnectionById(@PathVariable("id") int connectionId) {
		Connection connection = connectionService.findById(connectionId);
		Map<String, Object> response = responseBuilder.build(ConnectionDataMapper.mapToDTO(connection));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> deleteConnectionById(@PathVariable("id") int connectionId) {
		int deletedConnectionId = connectionService.deleteById(connectionId);
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Connection deleted successfully.");
		response.put("id",deletedConnectionId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}
	
	@PutMapping("/edit")
	public ResponseEntity<Map<String, Object>> updateConnection(@RequestBody ConnectionDetails connectionDetail) {
		ConnectionDetails dbDetails = connectionService.save(connectionDetail);
		String message = "Connection updated successfully.";
		Map<String, Object> response = responseBuilder.build(dbDetails);
		response.put("message", message);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}
	

}
