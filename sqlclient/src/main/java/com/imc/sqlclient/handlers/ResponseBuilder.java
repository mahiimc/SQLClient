package com.imc.sqlclient.handlers;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.imc.sqlclient.core.Driver;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ResponseBuilder  {

	public Map<String,Object> build(@NotNull Object body) {
		Map<String,Object> response = new HashMap<>();
		response.put("data", body);
		return response;
	}
	
	
	public Map<String,Object> buildPluginObject(String key, Driver plugin) {
		Map<String, Object> driverObj = new HashMap<>();
		driverObj.put("id",plugin.getId());
		driverObj.put("name",key);
		driverObj.put("title",Paths.get(key).getFileName().toString());
		driverObj.put("lastUpdatedTime",Paths.get(key).toFile().lastModified());
		return driverObj;
	}

}
