package com.ykuee.datamaintenance.common.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 * 
 */
@Component
@ConfigurationProperties(prefix = "datamaintenance")
public class DataMaintenanceProp
{
    /** 项目名称 */
    private String name;
    /** 版本 */
    private String version;
    
    /** 上传路径 */
    private static String defaultBaseDir;
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

	public static String getDefaultBaseDir() {
		return defaultBaseDir;
	}

	public  void setDefaultBaseDir(String defaultBaseDir) {
		DataMaintenanceProp.defaultBaseDir = defaultBaseDir;
	}

}
