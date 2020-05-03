package com.zcoox.boot.elasticjob.dynamic.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JobProperties {

    /**
     * 自定义异常处理类
     **/
    @JsonProperty("job_exception_handler")
    private String jobExceptionHandler = "com.dangdang.ddframe.job.executor.handler.impl.DefaultJobExceptionHandler";

    /**
     * 自定义业务处理线程池
     **/
    @JsonProperty("executor_service_handler")
    private String executorServiceHandler = "com.dangdang.ddframe.job.executor.handler.impl.DefaultExecutorServiceHandler";

    public String getJobExceptionHandler() {
        return jobExceptionHandler;
    }

    public String getExecutorServiceHandler() {
        return executorServiceHandler;
    }
}
