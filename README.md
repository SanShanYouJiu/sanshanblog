# SanShanBlog
 
> 本来想的是作为一个自己的Blog系统使用 
1. 前端使用angular4+bootstrap 
2. 
 - 后端目前采用的图片存储于MongoDB数据库(以及User表)
 - 使用Redis作为缓存 mysql作为Blog数据库 
 - maven作为项目管理工具
 - 基本架构是SSM 不过因为因为Spring4的注解解决方案很成熟 所以基本除了pom.xml之外的xml基本消失了
 - 日志系统采用的是Log4j+slf4j 存储在mongoDB中

3. 目前使用的是JWT+Spring security进行单点登录的安全解决方案


##  总体设计 
DO DTO VO 三种实体对象
1. DO:数据库表模型,一张表对应一个DO
2. DTO:数据传输载体
3. VO 对应接口返回数据包装.简单情况下DTO可以直接作为VO使用

#### 查询快速
实现了一个次关键字的缓存层 获取博客具体内容时才会向Redis请求取数据
其他时刻通过维持Id唯一的 Date Title Tag 次关键字倒排索引
从而不使用高昂的数据库/Redis查询
> 在目前数量较小时是可以到达速度极快的

在内部
- 分别维护6个Map集合  维持为3个倒排索引
- 定时刷新到磁盘上 启动时自动创建加载


风格是采用的Restful与传统URL方式混合 
博客支持俩种风格的Blog 
> - 一种是Markdown风格的编辑  另外一种是富文本方式(富文本方式用的是百度的UEditor) 

#### 注意事项
properties文件默认不是UTF-8编码 需要自己在IDEA中设置为UTF-8 或者自己手动处理

在代码中采用lombok进行缩写代码
