package com.zcoox.boot.elasticjob.comm;

public interface JobAttributeTag {

	String CRON = "cron";

	String SHARDING_TOTAL_COUNT = "shardingTotalCount";

	String SHARDING_ITEM_PARAMETERS = "shardingItemParameters";

	String JOB_PARAMETER = "jobParameter";

	String MONITOR_EXECUTION = "monitorExecution";

	String MONITOR_PORT = "monitorPort";

	String FAILOVER = "failover";

	String MAX_TIME_DIFF_SECONDS = "maxTimeDiffSeconds";

	String MISFIRE = "misfire";

	String JOB_SHARDING_STRATEGY_CLASS = "jobShardingStrategyClass";

	String DESCRIPTION = "description";

	String DISABLED = "disabled";

	String OVERWRITE = "overwrite";

	String LISTENERS = "listeners";

	String DISTRIBUTED_LISTENERS = "distributedListeners";

	String DISTRIBUTED_LISTENER_STARTED_TIMEOUT_MILLISECONDS = "startedTimeoutMilliseconds";

	String DISTRIBUTED_LISTENER_COMPLETED_TIMEOUT_MILLISECONDS = "completedTimeoutMilliseconds";

	String EXECUTOR_SERVICE_HANDLER = "executorServiceHandler";

	String JOB_EXCEPTION_HANDLER = "jobExceptionHandler";

	String EVENT_TRACE_RDB_DATA_SOURCE = "eventTraceRdbDataSource";

	String RECONCILE_INTERVAL_MINUTES = "reconcileIntervalMinutes";
	
	String SCRIPT_COMMAND_LINE = "scriptCommandLine";
	
	String STREAMING_PROCESS = "streamingProcess";
}
