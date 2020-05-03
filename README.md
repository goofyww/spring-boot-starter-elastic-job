# 版本说明
默认spring boot 版本为 2.1.5.RELEASE 下载完成项目后可更改为自己项目使用的spring boot版本  
elastic-job集成说明
## 1：maven导入坐标
     <dependency>
            <groupId>com.zcoox.boot</groupId>
            <artifactId>spring-boot-starter-elastic-job</artifactId>
            <version>2.1.5.RELEASE</version>
     </dependency>
## 2: spring.yml 中配置zookeeper注册中心地址
     
    elastic-job:
      zookeeper:
        server-list: 127.0.0.1:2181
        namespace: ${spring.application.name}
            
## 3：使用说明
   1. 启动类标注注解 @EnableElasticJob 表示开启使用 spring-boot-starter-elastic-job
   2. 配置扫描 @SpringBootApplication(scanBasePackages = {"com.zcoox.boot.*"})
   3. 使用者编写自己的Job作业，并且实现 SimplJob/DataFlowJob/ScriptJob 接口，编写对应的业务实现，使用注解 @ElasticJobBean 标注在自己编写的Job类上,通过注解属性或者配置文件配置项进行作业属性与定时配置

## 4：关于elastic-job的配置参数 详见注解 @ElasticJobBean 字段说明
    
## 使用说明(两种方式进行作业的配置) 
优先以spring.yml文件的配置为准，如果spring.yml文件中未配置作业属性，则以注解配置为准，注解的name属性为必填属性，其会作为spring.yml配置作业参数的一部分

1. 注解至少需要声明当前Job的name,其他属性在spring配置文件没有的情况会生效
      
      @ElasticJobBean(
              name = "fetchThirdOrderJob",
              cron = "0/15 * * * * ?",
              shardingTotalCount = 2,
              overwrite = true
      )
      
2. spring.properties 配置文件
       
       elastic-job:
          myShardingJob:
            cron: 0/15 * * * * ?
            shardingTotalCount: 10
            overwrite: true
            jobShardingStrategyClass: com.zcoox.mmalljob.order.job.sharding.OrderShardingStrategy
            eventTraceRdbDataSource: dataSource
            listeners: com.zcoox.mmalljob.order.job.listener.OrderListener,com.zcoox.mmalljob.order.job.listener.OrderTwoListener
            distributedListeners: com.zcoox.mmalljob.order.job.listener.OrderDistributeListener,com.zcoox.mmalljob.order.job.listener.OrderDistributeTwoListener
