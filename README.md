# MyBatis-Plus 自动生成表
## 说在前面的话
一开始我使用的是 spring data jpa作为我的数据库操作框架，因为它不仅实现了基础的增删查找接口，还可以做到表的自动创建和更新，节省了很多开发时间
但是，当业务变得复杂的时候，要动态的分页查找数据，jpa在这一方面就使不上力气，我转而投向了mybatis-plus的怀抱。 

 相比较于mybatis，mybatis-plus也已经实现了基本的增删查改接口，用起来也十分方便，对于复杂的业务，也可以自己手写sql去实现。美中不足的就是表结构不能自动生成，新增字段时需要改动实体类，项目初始化sql文件，测试时还需要再使用数据库连接工具连入数据库去新增字段。
 
 久而久之，我便萌生了将两个框架的功能联合使用的想法。一开始，我在想，可以让jpa框架也支持xml式的手写sql实现，一番源码查探后发现实现起来相对比较复杂，转换思想，既然mybatis-plus已经实现了大部分的功能，只有自动创建表上有点瑕疵(毕竟它的原理和jpa不一样)。
 
 因此，基于mybatis-plus的增强工具，mybaits-plus-auto-ddl便应运而生了。
 
 ## 使用方法
 整体的流程包括，启动时扫描实体类文件，解析实体类，生成ddl sql语句，执行sql语句。其中，包含了扫描路径的配置，生成策略的配置，表字段定义的配置。
 1. 扫描路径配置
 
    扫描路径提供了两种配置方式和一种默认配置。
    
    默认配置下，不需要用户指定，扫描主模块的项目启动类下的目录，即在被@SpringBootApplication注解的启动类的下层目录
    
    配置文件配置，在application.properties中，指定配置项 mybatis-plus.table.ddl.auto.scan-package,多个目录用 , 进行隔开
    
    工具注解配置，可以在项目启动类中添加注解，@EnableMyBatisAutoDDL配置路径，如：@EnableMyBatisAutoDDL(basePackages = "")
    
 2.  生成策略配置  
 
     生成策略包含none(啥也不执行)，create(删除已存在，新建表)，update(只做更新)，默认的表生成策略是 none
     
     配置文件配置，在application.properties中，指定配置项mybatis-plus.table.ddl.auto.strategy=none|create|update
     
     工具注解配置，可以在项目启动类中添加注解，@EnableMyBatisAutoDDL配置路径，如：@EnableMyBatisAutoDDL(ddl = "none|create|update")
     
 3. 表字段定义
 
    由于mybatis原来的注解没有长度的定义，字段备注说明的定义，因此，新增了两个注解。
    
    @Comments，注解在类或这成员变量上，进行表/字段的备注说明；
    
    @FieldLength，注解在成员变量上，用于定义字段的长度
    
 ## 其他说明
  当前只做了一个比较简单的版本，基本实现了以上的流程，策略为update的还在开发中
  
 


