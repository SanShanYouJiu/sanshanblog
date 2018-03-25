# SanShanBlog

 一个完善的博客社区

分布式支持，微服务架构，docker容器化

> 演示 [地址][2]

> 这是后台代码 在下文中有技术选型的说明 前端代码在这里 [地址][1]
>
> docker编排文件 [地址][3]

![sanshanblog-1](https://github.com/SanShanYouJiu/ImageRepository/blob/master/SanShaBlog/sanshanblog-1.jpg?raw=true)

![sanshanblog-2.jpg](https://github.com/SanShanYouJiu/ImageRepository/blob/master/SanShaBlog/sanshanblog-2.jpg?raw=true)

![sanshanblog-3](https://github.com/SanShanYouJiu/ImageRepository/blob/master/SanShaBlog/sanshanblog-3.jpg?raw=true)



## 技术选型

1. 前端使用[angular4+bootstrap][1] 
2. 后台说明：
 - 后台日志存入MongoDB数据库(以及User，FeedBack信息)
 - 使用Redis作为缓存 mysql作为通用数据库
 - maven作为项目管理工具
 - 基本架构是SSM 不过因为因为Spring4的注解解决方案很成熟 所以基本除了maven的pom.xml之外的xml基本消失了

 - 采用ElasticSearch 作为搜索支持
 - 日志系统采用的是Log4j2+slf4j 存储在mongoDB中
 - REST API 风格的URL 以及事务的完整支持


3. 目前使用的是JWT+Spring security的安全方案
4. 使用Spring Cloud 微服务框架支持
 - Zuul 网关 
 - eureka 服务注册 
 - hystrix 服务保护 
 - feign 服务消费
 - rabbitmq 消息总线
5. 整体项目使用[Docker][3]部署

##  领域模型设计 
主要为 DO DTO VO 三种实体对象
1. DO:数据库表模型,一张表对应一个DO
2. DTO:数据传输载体
3. VO 对应接口返回数据包装.简单情况下DTO可以直接作为VO使用




在代码中采用lombok进行缩写代码

[1]: https://github.com/SanShanYouJiu/SanShanBlog-Web
[2]: https://sanshan.xyz/
[3]: https://github.com/SanShanYouJiu/sanshanblog-docker-file