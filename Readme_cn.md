# DB MetaData Serve


> 基于DB元数据的快速开发框架，工程的缩写DBMS与数据库DBMS(Database Management System)殊途同归,融入了基本业务元素.
> 对于快速开发框架和快速开发平台的定义,一个具备基本开发工具的整合后框架,同时具备一些基本的模块(RBAC权限,菜单,用户,字典等)的脚手架.
> 这些框架的初衷都是想让开发不必在重头搭建一套完备的系统后,才开始开发和自己业务相关的模块,尽可能把"公共"模块抽离,达到复用的目的;
>
> 但现状是一个大型项目或系统中通常有很多子系统或子模块组成,并不是每个子模块都要具有完整的用户权限、登录、菜单管理、字典等等功能。
> 如果是在人员有限且项目规模不大,而且又是从0->1这个阶段开始的话，选择任何一个开源的快速开发框架都是合适的，如果系统非0->1这个阶段已经上线且具备一些功能了，再引入快速开发框架的效果和成本就值得考虑了；
>
> DMBS 的定位首先是一套功能引擎,其次才是开发平台
> 


### 技术栈
- SpringBoot
- JFinal ActiveRecord
- Mysql 8.0 / Druid 
- JDK 17
- Guava
- FastJson

### 能干什么
- 0代码构建一个CRUD模块，支持树表、主子表、单表的增删改查
- Form 支持在线图形拖拽配置设计
- 可当纯后端提供数据接口
- 可前后端一起使用
- 可作为内置模块 SpringBoot支持

## 快速开始
### 工程结构
```bash
├── db
├── db-metadata-analysis-springboot
├── db-metadata-parent
├── db-metadata-server-springboot
└── db-metadata-web
```
### 安装部署
```shell
docker-compose up
```

- phpmyadmin : `http://localhost:8000`  用户名：`root`  密码：`root123` 
- 系统前端 : `http://localhost` 如图所示完成初始化； 
  - ![初始化](db/images/init.png)

### 依赖配置
### 集成

## 基础术语
#### 元对象
《Thinking in Java》开篇就写到“一切皆对象”，Linux世界中“一切皆文件”，在DBMS中可以理解成一切的一切都离不开“元对象”；
什么是元对象，“元数据”是描述数据的数据，那么元对象就是描述对象的对象,狭隘的理解，一个元对象可以代表一个“表”，“一个视图”，“一类字段集合”
关系型数据库中可以通过DB的元信息来构建元对象，非关系型场景下可以用过一些灵活的schema来定义

#### 元子段
依托于元对象，元子段描述对象中的字段,定义了字段的属性和页面编辑的行为，比如字段类型、字段长度、字段是否为空、字段是否为索引、字段是否为主键等等;
页面行为诸如表单新增、修改时状态设置（只读，只写，可读写,字典自动转义等常规业务支持）。

#### Component
组件的抽象是为了服务与前端做的展示层配置，每一个前端框架种组件都可以抽象为一个组件，组件有自己独立的前端设置

```
- 数据展示(ViewComponent)
    - 表格组件(依赖元对象{1,n})(TableView)
    - 表单组件(依赖元对象{1,n})(FormView)
    - 树型组件(依赖元对象{1,n})(TreeView)
    - 搜索组件(依赖元对象{1,n})
- 表单(FormView)
    - 单选组件(元子段)FormField
    - 多选组件(元子段)
    - 输入组件(元子段)
    - 业务查询组件
    - 开关组件(元子段)
    - 日期组件(元子段)
    - 范围组件(元子段)
    - 上传组件(元子段)
```
#### Component实例
> 单纯的组件是没有灵魂的，元对象和元字段是组件的数据灵魂
> 
#### 模板
什么是模板，模板是针对场景的抽象，一对一CURD，一对多CRUD等等
模板需要具备：前端的模板、后端的模板逻辑、输入和输出、可预见的一些扩展；

#### 功能
> 什么定义成功能？
> 功能可以是一个按钮+背后的逻辑  
> 功能可以是一个纯背后的逻辑  
> 功能可以是一个页面  
> 公式:
```
功能 = (SearchBar(Component) + UIConfig + 元对象) * n
功能 = Table(Component) + UIConfig + 元对象
功能 = 功能 * n
```
### 设计

#### 总体架构图
> 元对象,元子段,component等概念的层次结构,对应关系 
![](db/images/架构图.png)
#### 通信图
> 前端请求渲染-> 后端数据装配->配置载入->merge
#### 数据库E-R图
![](db/images/e-r.png)
#### 元对象-类图
![元对象接口](db/images/MetaObject.png)
#### Component-类图
![元对象接口](db/images/component.png)
#### Query模块
![](db/images/IQueryCondition.png)
#### 扩展
> 为了能更好的融入其他系统，DBMS对常见的模块做了抽象，用少量的接口保证足够的灵活性
##### 用户体系
> 用户体系单独拎出可以作为一个庞大的子系统来开发，在DBMS种对用户做了一定抽象

> 核心接口 User(用户实体接口)，LoginService（登录服务），UserService（用户查询服务）,UserFactory(工厂)
![元对象接口](db/images/user.png)
##### 权限体系
>DBMS并未实现RBAC之列的权限控制模块，而是留了扩展接口

>DBMS种权限核心接口是MResource(资源)，MRPermit(资源判定器),MRLoader(资源加载器)
![元对象接口](db/images/auth.png)


### 系统截图

![](db/images/screenshot/1.png)
![](db/images/screenshot/2.png)
![](db/images/screenshot/3.png)
![](db/images/screenshot/4.png)
![](db/images/screenshot/5.png)
![](db/images/screenshot/6.png)


### 核心数据结构

#### 各类配置
- 元对象 config
- 元子段 config
- 全局组件 config
- 组件实例 config
- 功能 config
> 配置的使用了全局版本号,参考svn,即任一config变更后,版本号递增?

## 技术债务
- 系统为了获得动态能力,同时为了快速上线第一个版,底层采用了json存储配置,导致了上层数据搬运时不得不大量使用Kv对象(Map),调用链过长时很难确认当前Kv对象内部的数据
为代码阅读和调试带来了成本和风险
    > 在数据结构稳定后,关键的配置使用具体的模型来存放
- DB上配置信息存放在json字段,虽保证了灵活度,但是不利于检索,Mysql5.7对于Json字段的支持有限
- 元对象config,字段config,component.config,实例config,为了做到各层独立,分别设置了不同作用,不同意义的配置信息.在开始接触的时候对于这些
配置信息需要消化的成本太高,而且config未做结构校验,很可能在功能大面积爆发时出现无用无效的脏字段

## RoadMap
- server 源代码方式集成,剥离db-metadata-server业务逻辑和容器有关的逻辑,目的为了上层使用其他mvc框架做支持;
- 数据权限的设计
    > 以元对象为基础,配合自定义的模板脚本片段,生成带有权限过滤内容的sql,以此来做数据权限
    ```
        模板片段可以内置类似User,Group,Company等对象
        权限判断可能是
        if(user.role.is('组长'))
            return Query,Add,Update
        if(user.role.is('小弟'))
            return Query
        查看小组下的数据可能是
        if(user.role.is('组长')&&user.role.has(query))
            return create_by in GroupIds
        
    ```
- 丰富MResource的实现，增加不同纬度的验证（元对象纬度，模板纬度）,对Query模块的查询权限进行控制
- 丰富对数据库视图支持ViewMetaObject,增加元对象属性设定,参考linux文件系统drwxrwxrwx
- formbuilder 覆盖常用模板
- Jsondiff的支持
- springboot 深度集成(用spring完全接管datasource),充分支持spring方式创建router,controller,intercepter等jfinal组件
- 耗时操作的缓存支持(ehcache+redis)
- 对"功能"做版本控制,因功能渲染完全依靠数据配置,必须要保证发布后版本可控,目前元对象配置更新过以后,上游所有组件config会重新计算;
- 目前表单仅支持单个元对象,复杂业务场景可能有同时编辑多张表的需求
- 完善对spring容器的支持,目前还只是初步集成;

##### 一些参考
- 做一切有利于"快"的需求
- 计算机科学领域的任何问题都可以通过增加一个间接的中间层来解决
    > Any problem  in computer science can be solved by anther layer of indirection.  
     https://cloud.tencent.com/developer/article/1491973
- Eova ,jeesite,jeecg-boot,普元
- Extjs,vue component
- APIJSON
- GraphQL
