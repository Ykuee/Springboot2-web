> ### 项目背景

基于springboot2的web项目手脚架，包含了基本的用户模块。有时需要快速搭建一套框架做一些杂七杂八的事情，直接在这个框架的基础上再魔改就可以了。

项目基于Restful风格的接口开发，属于前后端分离，该项目属于后端部分。

>### 安装

1. 项目使用maven，依赖完成后构建。（默认数据库使用oracle，jdbc驱动需要自行下载）
2. 项目中使用Lombok与Mapstruct，需要构建
```
mvn install
mvn build
```

>### 使用

启动类
```
com.ykuee.SpringbootStart.java
```
项目默认打包为war已排除自带tomcat，可以放到外部tomcat中运行。

>### 技术架构

目前所用到的技术

| 整合技术架构          | 说明                                                |
| --------------------- | --------------------------------------------------- |
| Spring Boot 2.3.9     | 核心框架                                            |
| SpringMVC             | MVC框架                                             |
| Maven 3.2.5           | 项目管理工具                                        |
| Shiro-Spring 1.6.0    | shiro整合jwt redis做缓存                            |
| JWT 3.8.0             | Token                                               |
| Redis                 | Lettuce RedisTemplate 整合Shiro缓存                 |
| PageHelper            | 通过传入参数配合ThreadLocal分页                     |
| Mybatis 2.1.3         | 操作数据库                                          |
| MybatisPlus 3.2.0     | 基础CRUD，默认参数自动填充，动态表名，LambdaWrapper |
| Fastjson              | 参数Json转换 序列化                                 |
| Druid 1.1.24          | 动态数据源 监控                                     |
| Lombok 1.18.12        | 自动生成get set                                     |
| Hutool 5.5.9          | 各种工具类                                          |
| Kaptcha 2.3.2         | google的验证码生成工具                              |
| Mapstruct 1.3.1.Final | beanCopy                                            |
| Pinyin4j 2.5.0        | 拼音生成                                            |
| Mica-Xss 2.4.3-GA     | xss拦截                                             |
| SysLog                | 异步日志记录                                        |
| IdGenerator           | id生成器 Snowflake                                  |
| Response              | 统一的response格式返回                              |

