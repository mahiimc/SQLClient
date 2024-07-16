package com.imc.sqlclient.exception.handler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.imc.sqlclient.exception.SQLClientException;

@RestControllerAdvice
public class GlobalExceptonHandler {
	
	@ExceptionHandler(value = NoResourceFoundException.class)
	public ResponseEntity<Map<String,Object>> handleResourceNotFoundException(NoResourceFoundException ex) {
		Map<String,Object> error = new HashMap<>();
		error.put("message", "Page not found.");
		error.put("path", ex.getResourcePath());
		error.put("timeStamp", Instant.now().toEpochMilli());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(value = SQLClientException.class)
	public ResponseEntity<Map<String,Object>> handleSQLClientException(SQLClientException ex) {
		Map<String,Object> error = new HashMap<>();
		error.put("message", ex.getMessage());
		error.put("timeStamp", Instant.now().toEpochMilli());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Map<String,Object>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		Map<String,Object> error = new HashMap<>();
		error.put("message", "Invalid type provided");
		error.put("timeStamp", Instant.now().toEpochMilli());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
}
