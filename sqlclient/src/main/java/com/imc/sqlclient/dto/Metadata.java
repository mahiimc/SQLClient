package com.imc.sqlclient.dto;

import java.util.List;

import lombok.Data;

@Data
public class Metadata {
	
	private List<Table> tables;
	private List<String> columns;
	

}
