**项目说明** 
- 采用SpringBoot、MyBatis、Shiro框架，开发的一套系统，极低门槛，拿来即用。设计之初，就非常注重安全性，为企业系统保驾护航，让一切都变得如此简单。
- 支持MySQL主流数据库
<br>

**具有如下特点** 
- 灵活的权限控制，可控制到页面或按钮，满足绝大部分的权限需求
- 完善的部门管理及数据权限，通过注解实现数据权限的控制
- 完善的XSS防范及脚本过滤，彻底杜绝XSS攻击
- 支持分布式部署，session存储在redis中
- 友好的代码结构及注释，便于阅读及二次开发
- 引入quartz定时任务，可动态完成任务的添加、修改、删除、暂停、恢复及日志查看等功能
- 页面交互使用Vue2.x，极大的提高了开发效率
- 引入swagger文档支持，方便编写API接口文档

<br>

**数据权限设计思想** 
- 管理员管理、角色管理、部门管理，可操作本部门及子部门数据
- 菜单管理、定时任务、参数管理、字典管理、系统日志，没有数据权限
- 业务功能，按照用户数据权限，查询、操作数据【没有本部门数据权限，也能查询本人数据】

<br> 

**项目结构** 

demo-security
├─common     公共模块
│ 
├─admin      管理后台
│    ├─db  数据库SQL脚本
│    │ 
│    ├─modules  模块
│    │    ├─job 定时任务
│    │    ├─oss 文件存储
│    │    └─sys 系统管理(核心)
│    │ 
│    └─resources 
│        ├─mapper   MyBatis文件
│        ├─statics  静态资源
│        ├─template 系统页面
│        │    ├─modules      模块页面
│        │    ├─index.html   AdminLTE主题风格（默认主题）
│        │    └─index1.html  Layui主题风格
│        └─application.yml   全局配置文件
│       
│ 
├─api        API服务
│ 
├─generator  代码生成器
│        └─resources 
│           ├─mapper   MyBatis文件
│           ├─template 代码生成器模板（可增加或修改相应模板）
│           ├─application.yml    全局配置文件
│           └─generator.properties   代码生成器，配置文件
│



 **技术选型：** 
- 核心框架：Spring Boot 2.0
- 安全框架：Apache Shiro 1.4
- 视图框架：Spring MVC 5.0
- 持久层框架：MyBatis 3.3
- 定时器：Quartz 2.3
- 数据库连接池：Druid 1.1
- 日志管理：SLF4J 1.7、Log4j
- 页面交互：Vue2.x

<br>

 **软件需求** 
- JDK1.8
- MySQL5.5+
- Maven3.0+

<br>

 **本地部署**
- 通过git下载源码
- 创建数据库demo_security，数据库编码为UTF-8
- 执行db/mysql.sql文件，初始化数据【按需导入表结构及数据】
- 修改application-dev.yml文件，更新MySQL账号和密码
- 在demo-security目录下，执行mvn clean install
<br>

- Eclipse、IDEA运行AdminApplication.java，则可启动项目【admin】
- renren-admin访问路径：http://localhost:8080/admin
- swagger文档路径：http://localhost:8080/admin/swagger/index.html
- 账号密码：admin/admin

<br>

- Eclipse、IDEA运行ApiApplication.java，则可启动项目【api】
- api访问路径：http://localhost:8081/api/swagger-ui.html

<br>

- Eclipse、IDEA运行GeneratorApplication.java，则可启动项目【generator】
- generator访问路径：http://localhost:8082/generator


<br>

 **分布式部署**
- 分布式部署，需要安装redis，并配置config.properties里的redis信息
- 需要配置【XX.redis.open=true】，表示开启redis缓存
- 需要配置【XX.cluster=true】，表示开启集群环境

<br>

 **项目演示**
- 演示地址：
- 账号密码：

<br>


**接口文档效果图：** 
![输入图片说明]("在这里输入图片标题")

<br>

**Layui主题风格：**
![输入图片说明]( "在这里输入图片标题")

<br>

**AdminLTE主题风格：**
![输入图片说明]( "在这里输入图片标题")

<br>


一、框架 版本
1.版本3.2.0-XX院版本，新增包含如下技术
ehcache、solr、阿里oss、阿里短信服务、网站前端登录使用session+cookies进行管理、 百度ueditor内容编辑器等

2.版本4.0.0 在版本3.2.0的基础上集成redis、token、支付功能
（1）redis适用于验证码粗存储、登录token存储
（2）采用token进行登录验证，解决移动端与web端登录不兼容问题
（3）支付支持支付宝。。。。

二、common工具包版本
1.版本3.2.0-XX版本
2.版本4.0.2集成redis

