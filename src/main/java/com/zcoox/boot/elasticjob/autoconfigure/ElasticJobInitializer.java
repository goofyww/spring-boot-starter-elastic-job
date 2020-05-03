package com.zcoox.boot.elasticjob.autoconfigure;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.executor.handler.JobProperties;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.zcoox.boot.elasticjob.annotation.ElasticJobBean;
import com.zcoox.boot.elasticjob.comm.JobAttributeTag;
import com.zcoox.boot.elasticjob.dynamic.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * elastic-job 作业初始化
 */
public class ElasticJobInitializer implements ApplicationContextAware {

    @Autowired
    private CoordinatorRegistryCenter zkCenter;

    @Autowired(required = false)
    private JobService jobService;

    private String prefix = "elastic-job.";

    private Environment env;

    private List<String> jobTypeNameList = Arrays.asList("SimpleJob", "DataflowJob", "ScriptJob");

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        env = ctx.getEnvironment();
        Map<String, Object> beans = ctx.getBeansWithAnnotation(ElasticJobBean.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object instance = entry.getValue();
            Class clz = instance.getClass();
            Class<?>[] interfaces = clz.getInterfaces();
            for (Class<?> superInterface : interfaces) {
                String jobTypeName = superInterface.getSimpleName();
                if (jobTypeNameList.contains(jobTypeName)) {
                    ElasticJobBean conf = AnnotationUtils.findAnnotation(clz, ElasticJobBean.class);
                    String jobClass = clz.getName();
                    String jobName = conf.name();
                    String cron = getEnvironmentStringValue(jobName, JobAttributeTag.CRON, conf.cron());
                    String shardingItemParameters = getEnvironmentStringValue(jobName, JobAttributeTag.SHARDING_ITEM_PARAMETERS, conf.shardingItemParameters());
                    String description = getEnvironmentStringValue(jobName, JobAttributeTag.DESCRIPTION, conf.description());
                    String jobParameter = getEnvironmentStringValue(jobName, JobAttributeTag.JOB_PARAMETER, conf.jobParameter());
                    String jobExceptionHandler = getEnvironmentStringValue(jobName, JobAttributeTag.JOB_EXCEPTION_HANDLER, conf.jobExceptionHandler());
                    String executorServiceHandler = getEnvironmentStringValue(jobName, JobAttributeTag.EXECUTOR_SERVICE_HANDLER, conf.executorServiceHandler());

                    String jobShardingStrategyClass = getEnvironmentStringValue(jobName, JobAttributeTag.JOB_SHARDING_STRATEGY_CLASS, conf.jobShardingStrategyClass().getCanonicalName());
                    String eventTraceRdbDataSource = getEnvironmentStringValue(jobName, JobAttributeTag.EVENT_TRACE_RDB_DATA_SOURCE, conf.eventTraceRdbDataSource());
                    String scriptCommandLine = getEnvironmentStringValue(jobName, JobAttributeTag.SCRIPT_COMMAND_LINE, conf.scriptCommandLine());

                    boolean failover = getEnvironmentBooleanValue(jobName, JobAttributeTag.FAILOVER, conf.failover());
                    boolean misfire = getEnvironmentBooleanValue(jobName, JobAttributeTag.MISFIRE, conf.misfire());
                    boolean overwrite = getEnvironmentBooleanValue(jobName, JobAttributeTag.OVERWRITE, conf.overwrite());
                    boolean disabled = getEnvironmentBooleanValue(jobName, JobAttributeTag.DISABLED, conf.disabled());
                    boolean monitorExecution = getEnvironmentBooleanValue(jobName, JobAttributeTag.MONITOR_EXECUTION, conf.monitorExecution());
                    boolean streamingProcess = getEnvironmentBooleanValue(jobName, JobAttributeTag.STREAMING_PROCESS, conf.streamingProcess());

                    int shardingTotalCount = getEnvironmentIntValue(jobName, JobAttributeTag.SHARDING_TOTAL_COUNT, conf.shardingTotalCount());
                    int monitorPort = getEnvironmentIntValue(jobName, JobAttributeTag.MONITOR_PORT, conf.monitorPort());
                    int maxTimeDiffSeconds = getEnvironmentIntValue(jobName, JobAttributeTag.MAX_TIME_DIFF_SECONDS, conf.maxTimeDiffSeconds());
                    int reconcileIntervalMinutes = getEnvironmentIntValue(jobName, JobAttributeTag.RECONCILE_INTERVAL_MINUTES, conf.reconcileIntervalMinutes());

                    /** 核心配置 **/
                    JobCoreConfiguration coreConfiguration = JobCoreConfiguration.newBuilder(jobName, cron, shardingTotalCount)
                            .shardingItemParameters(shardingItemParameters)
                            .description(description)
                            .failover(failover)
                            .jobParameter(jobParameter)
                            .misfire(misfire)
                            .jobProperties(JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER.getKey(), jobExceptionHandler)
                            .jobProperties(JobProperties.JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER.getKey(), executorServiceHandler)
                            .build();

                    /** 类型作业配置 **/
                    JobTypeConfiguration typeConfiguration = null;
                    switch (jobTypeName) {
                        case "DataflowJob":
                            typeConfiguration = new DataflowJobConfiguration(coreConfiguration, jobClass, streamingProcess);
                            break;
                        case "ScriptJob":
                            typeConfiguration = new ScriptJobConfiguration(coreConfiguration, scriptCommandLine);
                            break;
                        default:
                            typeConfiguration = new SimpleJobConfiguration(coreConfiguration, jobClass);
                    }

                    /** 根配置 **/
                    LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(typeConfiguration)
                            .overwrite(overwrite)
                            .disabled(disabled)
                            .monitorPort(monitorPort)
                            .monitorExecution(monitorExecution)
                            .maxTimeDiffSeconds(maxTimeDiffSeconds)
                            .jobShardingStrategyClass(jobShardingStrategyClass)
                            .reconcileIntervalMinutes(reconcileIntervalMinutes)
                            .build();

                    List<BeanDefinition> elasticJobListeners = getTargetElasticJobListeners(conf);
                    // 构建SpringJobScheduler对象来初始化任务
                    BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(SpringJobScheduler.class);
                    factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
                    if ("ScriptJob".equals(jobTypeName)) {
                        factory.addConstructorArgValue(null);
                    } else {
                        factory.addConstructorArgValue(instance);
                    }
                    factory.addConstructorArgValue(zkCenter);
                    factory.addConstructorArgValue(liteJobConfiguration);
                    // 任务执行日志数据源，以名称获取
                    if (StringUtils.hasText(eventTraceRdbDataSource)) {
                        BeanDefinitionBuilder rdbFactory = BeanDefinitionBuilder.rootBeanDefinition(JobEventRdbConfiguration.class);
                        rdbFactory.addConstructorArgReference(eventTraceRdbDataSource);
                        factory.addConstructorArgValue(rdbFactory.getBeanDefinition());
                    }
                    factory.addConstructorArgValue(elasticJobListeners);

                    DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
                    defaultListableBeanFactory.registerBeanDefinition(jobName + "SpringJobScheduler", factory.getBeanDefinition());
                    SpringJobScheduler springJobScheduler = (SpringJobScheduler) ctx.getBean(jobName + "SpringJobScheduler");
                    springJobScheduler.init();
                    log.info("【" + jobName + "】\t" + jobClass + "\t init success");
                }
            }
        }

        //开启任务监听,当有任务添加时，监听zk中的数据增加，自动在其他节点也初始化该任务
        if (jobService != null) {
            jobService.monitorJobRegister();
        }
    }

    private List<BeanDefinition> getTargetElasticJobListeners(ElasticJobBean conf) {
        List<BeanDefinition> result = new ManagedList<BeanDefinition>();
        String listeners = getEnvironmentStringValue(conf.name(), JobAttributeTag.LISTENERS, strArr2Strs(conf.listeners(), ","));
        if (StringUtils.hasText(listeners)) {
            for (String listener : listeners.split(",")) {
                BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(listener);
                factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
                result.add(factory.getBeanDefinition());
            }
        }
        String distributedListeners = getEnvironmentStringValue(conf.name(), JobAttributeTag.DISTRIBUTED_LISTENERS, strArr2Strs(conf.distributedListeners(), ","));
        long startedTimeoutMilliseconds = getEnvironmentLongValue(conf.name(), JobAttributeTag.DISTRIBUTED_LISTENER_STARTED_TIMEOUT_MILLISECONDS, conf.startedTimeoutMilliseconds());
        long completedTimeoutMilliseconds = getEnvironmentLongValue(conf.name(), JobAttributeTag.DISTRIBUTED_LISTENER_COMPLETED_TIMEOUT_MILLISECONDS, conf.completedTimeoutMilliseconds());
        if (StringUtils.hasText(distributedListeners)) {
            for (String distributedListener : distributedListeners.split(",")) {
                BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(distributedListener);
                factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
                factory.addConstructorArgValue(startedTimeoutMilliseconds);
                factory.addConstructorArgValue(completedTimeoutMilliseconds);
                result.add(factory.getBeanDefinition());
            }
        }
        return result;
    }

    private String getEnvironmentStringValue(String jobName, String fieldName, String defaultValue) {
        String key = prefix + jobName + "." + fieldName;
        String value = env.getProperty(key);
        if (StringUtils.hasText(value)) {
            return value;
        }
        return defaultValue;
    }

    private int getEnvironmentIntValue(String jobName, String fieldName, int defaultValue) {
        String key = prefix + jobName + "." + fieldName;
        String value = env.getProperty(key);
        if (StringUtils.hasText(value)) {
            return Integer.valueOf(value);
        }
        return defaultValue;
    }

    private long getEnvironmentLongValue(String jobName, String fieldName, long defaultValue) {
        String key = prefix + jobName + "." + fieldName;
        String value = env.getProperty(key);
        if (StringUtils.hasText(value)) {
            return Long.valueOf(value);
        }
        return defaultValue;
    }

    private boolean getEnvironmentBooleanValue(String jobName, String fieldName, boolean defaultValue) {
        String key = prefix + jobName + "." + fieldName;
        String value = env.getProperty(key);
        if (StringUtils.hasText(value)) {
            return Boolean.valueOf(value);
        }
        return defaultValue;
    }

    private String strArr2Strs(String[] strArr, String comma) {
        StringBuffer buffer = new StringBuffer();
        if (strArr == null || strArr.length < 1) {
            return "";
        }
        for (String s : strArr) {
            buffer.append(s).append(comma);
        }
        return buffer.substring(0, buffer.length() - 1);
    }

    private Logger log = LoggerFactory.getLogger(ElasticJobInitializer.class);

}
