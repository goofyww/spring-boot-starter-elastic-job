package com.zcoox.boot.elasticjob.dynamic.bean;

import java.io.Serializable;

public class Job implements Serializable {

    private static final long serialVersionUID = 5454391743052728418L;
    
    private String jobName;

    private String jobType;

    private String jobClass;

    private String cron;

    private int shardingTotalCount = 1;

    private String shardingItemParameters = "";

    private String jobParameter = "";

    private boolean failover = false;

    private boolean misfire = false;

    private String description = "";

    private boolean overwrite = false;

    private boolean streamingProcess = false;

    private String scriptCommandLine = "";

    private boolean monitorExecution = true;

    private int monitorPort = -1;

    private int maxTimeDiffSeconds = -1;

    private String jobShardingStrategyClass = "";

    private int reconcileIntervalMinutes = 10;

    private String eventTraceRdbDataSource = "";

    private String listener = "";

    private boolean disabled = false;

    private String distributedListener = "";

    private long startedTimeoutMilliseconds = Long.MAX_VALUE;

    private long completedTimeoutMilliseconds = Long.MAX_VALUE;

    private JobProperties jobProperties = new JobProperties();

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public int getShardingTotalCount() {
        return shardingTotalCount;
    }

    public void setShardingTotalCount(int shardingTotalCount) {
        this.shardingTotalCount = shardingTotalCount;
    }

    public String getShardingItemParameters() {
        return shardingItemParameters;
    }

    public void setShardingItemParameters(String shardingItemParameters) {
        this.shardingItemParameters = shardingItemParameters;
    }

    public String getJobParameter() {
        return jobParameter;
    }

    public void setJobParameter(String jobParameter) {
        this.jobParameter = jobParameter;
    }

    public boolean isFailover() {
        return failover;
    }

    public void setFailover(boolean failover) {
        this.failover = failover;
    }

    public boolean isMisfire() {
        return misfire;
    }

    public void setMisfire(boolean misfire) {
        this.misfire = misfire;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public boolean isStreamingProcess() {
        return streamingProcess;
    }

    public void setStreamingProcess(boolean streamingProcess) {
        this.streamingProcess = streamingProcess;
    }

    public String getScriptCommandLine() {
        return scriptCommandLine;
    }

    public void setScriptCommandLine(String scriptCommandLine) {
        this.scriptCommandLine = scriptCommandLine;
    }

    public boolean isMonitorExecution() {
        return monitorExecution;
    }

    public void setMonitorExecution(boolean monitorExecution) {
        this.monitorExecution = monitorExecution;
    }

    public int getMonitorPort() {
        return monitorPort;
    }

    public void setMonitorPort(int monitorPort) {
        this.monitorPort = monitorPort;
    }

    public int getMaxTimeDiffSeconds() {
        return maxTimeDiffSeconds;
    }

    public void setMaxTimeDiffSeconds(int maxTimeDiffSeconds) {
        this.maxTimeDiffSeconds = maxTimeDiffSeconds;
    }

    public String getJobShardingStrategyClass() {
        return jobShardingStrategyClass;
    }

    public void setJobShardingStrategyClass(String jobShardingStrategyClass) {
        this.jobShardingStrategyClass = jobShardingStrategyClass;
    }

    public int getReconcileIntervalMinutes() {
        return reconcileIntervalMinutes;
    }

    public void setReconcileIntervalMinutes(int reconcileIntervalMinutes) {
        this.reconcileIntervalMinutes = reconcileIntervalMinutes;
    }

    public String getEventTraceRdbDataSource() {
        return eventTraceRdbDataSource;
    }

    public void setEventTraceRdbDataSource(String eventTraceRdbDataSource) {
        this.eventTraceRdbDataSource = eventTraceRdbDataSource;
    }

    public String getListener() {
        return listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getDistributedListener() {
        return distributedListener;
    }

    public void setDistributedListener(String distributedListener) {
        this.distributedListener = distributedListener;
    }

    public long getStartedTimeoutMilliseconds() {
        return startedTimeoutMilliseconds;
    }

    public void setStartedTimeoutMilliseconds(long startedTimeoutMilliseconds) {
        this.startedTimeoutMilliseconds = startedTimeoutMilliseconds;
    }

    public long getCompletedTimeoutMilliseconds() {
        return completedTimeoutMilliseconds;
    }

    public void setCompletedTimeoutMilliseconds(long completedTimeoutMilliseconds) {
        this.completedTimeoutMilliseconds = completedTimeoutMilliseconds;
    }

    public JobProperties getJobProperties() {
        return jobProperties;
    }

    public void setJobProperties(JobProperties jobProperties) {
        this.jobProperties = jobProperties;
    }
}
