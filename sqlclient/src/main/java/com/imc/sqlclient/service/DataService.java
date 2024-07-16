package com.imc.sqlclient.service;

import com.imc.sqlclient.dto.Query;
import com.imc.sqlclient.dto.ResultSetData;

public interface DataService {
	
	ResultSetData fetchData(int connectionId, String tableName); 
	ResultSetData executeQuery(Query query);
}
