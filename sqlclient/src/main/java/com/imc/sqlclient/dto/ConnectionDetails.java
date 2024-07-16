package com.imc.sqlclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ConnectionDetails {
	
	private int id;
	
	private String name;
	
	private String url;
	
	@JsonProperty(value = "username")
	private String userName;
	
	private String password;
	
	@JsonProperty(value = "driver")
	private String driverClassName;
}
