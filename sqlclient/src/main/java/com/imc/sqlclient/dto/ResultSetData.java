package com.imc.sqlclient.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ResultSetData {
	private List<Column> columns;
	private List<Map<String,Object>> result;
	private int size;

}
