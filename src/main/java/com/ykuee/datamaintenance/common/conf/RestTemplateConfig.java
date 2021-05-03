package com.ykuee.datamaintenance.common.conf;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.ykuee.datamaintenance.common.prop.HttpProp;

/**
 * @Description: RestTemplate
 * @Author: heguangyue
 * @Date: Created in 2020/4/29
 */
@Configuration
public class RestTemplateConfig {
  @Autowired
  private HttpProp httpProp;

  @Bean
  public ClientHttpRequestFactory httpRequestFactory() {
    if (httpProp.getMaxTotal() <= 0) {
      throw new IllegalArgumentException("invalid maxTotalConnection: " + httpProp.getMaxTotal());
    }
    if (httpProp.getDefaultMaxPerRoute() <= 0) {
      throw new IllegalArgumentException("invalid maxConnectionPerRoute: " + httpProp.getDefaultMaxPerRoute());
    }
    return new HttpComponentsClientHttpRequestFactory(httpClient());
  }

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
    List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
    //重新设置StringHttpMessageConverter字符集为UTF-8，解决中文乱码问题
    HttpMessageConverter<?> converterTarget = null;
    for (HttpMessageConverter<?> item : converterList) {
      if (StringHttpMessageConverter.class == item.getClass()) {
        converterTarget = item;
        break;
      }
    }
    if (null != converterTarget) {
      converterList.remove(converterTarget);
    }
    converterList.add(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    return restTemplate;
  }

  @Bean
  public HttpClient httpClient() {
    Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.getSocketFactory())
            .register("https", SSLConnectionSocketFactory.getSocketFactory())
            .build();
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
    connectionManager.setMaxTotal(httpProp.getMaxTotal());
    connectionManager.setDefaultMaxPerRoute(httpProp.getDefaultMaxPerRoute());
    connectionManager.setValidateAfterInactivity(httpProp.getValidateAfterInactivity());

    RequestConfig requestConfig = null;
    if(1 == httpProp.getProxySwitch()){
      requestConfig = RequestConfig.custom()
              .setSocketTimeout(httpProp.getSocketTimeout())
              .setConnectTimeout(httpProp.getConnectTimeout())
              //从连接池中获取连接的超时时间，超时间未拿到可用连接，
              // 会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
              .setConnectionRequestTimeout(httpProp.getConnectionRequestTimeout())
              .setProxy(new HttpHost(httpProp.getProxyHost(), httpProp.getProxyPort()))
              .build();
    }else{
      requestConfig = RequestConfig.custom()
              .setSocketTimeout(httpProp.getSocketTimeout())
              .setConnectTimeout(httpProp.getConnectTimeout())
              .setConnectionRequestTimeout(httpProp.getConnectionRequestTimeout())
              .build();
    }
    return HttpClientBuilder.create()
            .setDefaultRequestConfig(requestConfig)
            .setConnectionManager(connectionManager)
            .build();
  }


}
