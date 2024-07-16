package com.imc.sqlclient.utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JacksonUtils {
	
	public static ObjectNode emptyNode() {
		return JsonNodeFactory.instance.objectNode();
	}
	
	public static ArrayNode emptyArray() {
		return JsonNodeFactory.instance.arrayNode();
	}

}
