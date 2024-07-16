package com.imc.sqlclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Column {
	private String name;
	private Integer dataType;
	private String dataTypeName;
}
