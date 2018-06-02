# SanShanBlog

 一个完善的分布式天然支持的博客社区

SanShanBlog是基于Spring Cloud微服务化博客社区平台，具有统一授权、主要业务系统、搜索系统、监控与链路追踪系统，其中包含 权限管理，搜索管理，网关API管理等多个模块，支持多业务系统并行开发

核心技术采用Spring Boot1.5.2以及Spring Cloud (Dalston.SR1)相关核心组件

> 演示 [地址][2]

> 这是后台代码 在下文中有技术选型的说明 前端代码在这里 [地址][1]
>
> docker编排文件 [地址][3]
>
> 项目部署 [快速开始][5]

![sanshanblog-1](https://github.com/SanShanYouJiu/ImageRepository/blob/master/SanShaBlog/sanshanblog-1.jpg?raw=true)

![sanshanblog-2.jpg](https://github.com/SanShanYouJiu/ImageRepository/blob/master/SanShaBlog/sanshanblog-2.jpg?raw=true)

![sanshanblog-3](https://github.com/SanShanYouJiu/ImageRepository/blob/master/SanShaBlog/sanshanblog-3.jpg?raw=true)





## 架构说明

![sanshanblog-4](https://github.com/SanShanYouJiu/ImageRepository/blob/master/SanShaBlog/SanShanBlog%20%E6%9E%B6%E6%9E%84%E5%9B%BE%20(1).png?raw=true)

## 技术选型

1. 前端使用[angular4+bootstrap][1] 
2. 后台说明：
 - 使用Redis作为缓存 mysql作为通用数据库
 - maven作为项目管理工具
 - 采用ElasticSearch 作为搜索支持
 - 日志系统采用的是Log4j2+slf4j 存储在mongoDB中
 - REST API 风格的URL 以及事务的完整支持
 - 使用RabbitMQ作为消息总线
3. 目前使用的是借鉴了AG-admin自实现的以JWT为基础的权限方案
4. 使用Spring Cloud 微服务框架支持
 - Zuul 网关 
 - eureka 服务注册 
 - hystrix 服务保护 
 - feign 服务消费
 - zikpin 链路追踪
5. 整体项目使用[Docker][3]部署
6. 基于Jenkins实现CI

##  领域模型设计 
主要为 DO DTO VO 三种实体对象
1. DO:数据库表模型,一张表对应一个DO
2. DTO:数据传输载体
3. VO 对应接口返回数据包装.简单情况下DTO可以直接作为VO使用

> 开发中需要的配置模板可以在[这里][4]获取 

在代码中采用lombok进行缩写代码

 

[1]: https://github.com/SanShanYouJiu/SanShanBlog-Web
[2]: https://sanshan.xyz/
[3]: https://github.com/SanShanYouJiu/sanshanblog-docker-file
[4]: https://gitee.com/SanShanYouJiu/config-repo-demo
[5]: https://github.com/SanShanYouJiu/sanshanblog/wiki/%E5%BF%AB%E9%80%9F%E5%BC%80%E5%A7%8B