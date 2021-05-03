package com.ykuee.datamaintenance.common.dynamictablename;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties("dynamictablenames")
public class TableNames {

	  private List<String> tableNames;
	  
	  public List<String> getTableNames() {
	    return tableNames;
	  }
	 
	  public void setTableNames(List<String> tableNames) {
	    this.tableNames = tableNames;
	  }
	  
}
