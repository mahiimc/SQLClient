package com.imc.sqlclient.handlers;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.imc.sqlclient.core.Plugin;

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
	
	
	public Map<String,Object> buildPluginObject(String key, Plugin plugin) {
		Map<String, Object> pluginObj = new HashMap<>();
		pluginObj.put("id",plugin.getId());
		pluginObj.put("name",key);
		pluginObj.put("title",Paths.get(key).getFileName().toString());
		pluginObj.put("lastUpdatedTime",Paths.get(key).toFile().lastModified());
		return pluginObj;
	}

}
