package com.ykuee.datamaintenance.common.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: // TODO
 * @Author: heguangyue
 * @Date: 2020/7/20 
 */
@Data
@Component
@ConfigurationProperties(prefix = "http")
public class HttpProp {
	/**代理开关*/
	private Integer proxySwitch;
	/**代理IP*/
	private String proxyHost;
	/**代理端口*/
	private Integer proxyPort;
	/**连接池的最大连接数*/
	private Integer maxTotal;
	/**同路由的并发数*/
	private Integer defaultMaxPerRoute;
	/**客户端和服务器建立连接超时，默认2s*/
	private Integer connectTimeout;
	/**从连接池获取连接的超时时间,不宜过长,单位ms*/
	private Integer connectionRequestTimeout;
	/**指客户端从服务器读取数据包的间隔超时时间,不是总读取时间,也就是socket timeout,ms*/
	private Integer socketTimeout;
	/**重试次数*/
	private Integer retryTimes;
	/**长连接保持时间 单位s,不宜过长*/
	private Integer keepAliveTime;

	private Integer validateAfterInactivity;
	/**字符集*/
	private String charset;









}
