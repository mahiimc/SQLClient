package com.imc.sqlclient.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class Metadata {
	
	private List<String> schemas;
	private List<String> catalogs;
	private List<Table> tables;
	private List<String> columns;
	

}
