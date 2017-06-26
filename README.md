# SanShanBlog
 
> 本来想的是作为一个自己的Blog系统使用 
1. 前端使用NG2+bootstrap 
> 目前还没有放到Github上 
2. 
- 后端目前采用的图片存储于MongoDB数据库
 (以及User表)
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

内部维持ID唯一作为缓存的唯一标识 通过将其他的Date Title Tag次关键字属性
- 分别维护6个Map集合  维持为3个倒排索引
- 定时刷新到磁盘上 启动时加载
- 从而通过次关键字的方式查找缓存 进而不使用高昂的数据库查询


风格是采用的Restful与传统URL方式混合 
博客支持俩种风格的Blog 
> - 一种是Markdown风格的编辑  另外一种是富文本方式(富文本方式用的是百度的UEditor) 

#### 注意事项
properties文件默认不是UTF-8编码 需要自己在IDEA中设置为UTF-8 或者自己手动处理

