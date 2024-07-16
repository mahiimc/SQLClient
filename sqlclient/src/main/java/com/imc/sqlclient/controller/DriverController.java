package com.imc.sqlclient.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.imc.sqlclient.core.Plugin;
import com.imc.sqlclient.core.PluginClassesRepository;
import com.imc.sqlclient.core.PluginResgistry;
import com.imc.sqlclient.core.utils.PluginUtils;
import com.imc.sqlclient.handlers.ResponseBuilder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/driver")
@CrossOrigin(origins =  {"http://localhost:3000"})
public class DriverController {
	private static final String DRIVER_PATH = "D:\\Project\\sqlclient\\src\\main\\resources\\drivers";
	private final ResponseBuilder responseBuilder;

	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> listDrivers() {

		PluginClassesRepository repo = PluginClassesRepository.getInstance();
		List<Map<String,Object>> plugins = new ArrayList<>();
		repo.getRepository().forEach((key, plugin) -> {
			Map<String, Object> pluginObj =  responseBuilder.buildPluginObject(key, plugin);
			plugins.add(pluginObj);
		});
		Map<String,Object> response =  responseBuilder.build(plugins);
		response.put("size", plugins.size());
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(path = "/" , consumes = "multipart/form-data")
	public ResponseEntity<Map<String,Object>> uploadDriver(@RequestParam(value ="file", required = false) MultipartFile multiPartFile ) {
		String message =  "";
		HttpStatus status = null;
		Map<String, Object> pluginObj = new HashMap<>();
		try {
			String absPath = DRIVER_PATH+File.separator+multiPartFile.getOriginalFilename();
			Files.copy(multiPartFile.getInputStream(), Path.of(absPath));
			message = "file uploaded successfully.";
			status = HttpStatus.CREATED;
			pluginObj = responseBuilder.buildPluginObject(absPath,
					new Plugin(absPath,null));
		}
		catch (IOException e) {
			message = "Could not upload file due to " + e.getMessage();
			status = HttpStatus.BAD_REQUEST;
		}
		
		Map<String,Object> response = new HashMap<>();
		response.put("data",pluginObj);
		response.put("message", message);
		return ResponseEntity.status(status).body(response);
	}
	
	@PostMapping("/delete")
	public ResponseEntity<Map<String, Object>> deleteDriver(@RequestBody Map<String,String> body) {
		String path = body.get("path");
		File file = Path.of(path).toFile();
		file.delete();
		Map<String, Object> response = new HashMap<>();
		response.put("message", "file deleted successfully.");
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}
	
	@GetMapping("/registered")
	public ResponseEntity<Map<String,Object>> findRegisteredClasses() {
		
		Set<String> entryPoints =  PluginResgistry.getInstance().findEntryPoints();
		return ResponseEntity.ok(responseBuilder.build(entryPoints));
		
	}

}
