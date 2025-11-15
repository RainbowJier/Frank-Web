package org.frank.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * AsyncConfig
 */
@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * Async thread pool for login log processing
     */
    @Bean("loginLogExecutor")
    public Executor loginLogExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Core pool size: number of threads initialized when thread pool is created
        executor.setCorePoolSize(2);

        // Maximum pool size: maximum number of threads in the thread pool, only applies when buffer queue is full
        executor.setMaxPoolSize(5);

        // Queue capacity: buffer queue for storing tasks
        executor.setQueueCapacity(100);

        // Keep alive time: idle time for threads beyond core pool size before being destroyed
        executor.setKeepAliveSeconds(60);

        // Thread name prefix: helps identify which thread pool is processing the task
        executor.setThreadNamePrefix("log-login-async-");

        // Rejection policy: strategy when thread pool queue is full and max threads are reached
        // Caller runs policy: the calling thread will execute the task
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // Wait for all tasks to complete before shutdown
        executor.setWaitForTasksToCompleteOnShutdown(true);

        // Wait time for shutdown
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();

        log.info("Login log async thread pool initialized, core threads: {}, max threads: {}, queue capacity: {}",
                executor.getCorePoolSize(), executor.getMaxPoolSize(), executor.getThreadPoolExecutor().getQueue().remainingCapacity());

        return executor;
    }

    /**
     * Default async executor
     */
    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Core pool size
        executor.setCorePoolSize(5);

        // Maximum pool size
        executor.setMaxPoolSize(10);

        // Queue capacity
        executor.setQueueCapacity(200);

        // Keep alive time for idle threads
        executor.setKeepAliveSeconds(60);

        // Thread name prefix
        executor.setThreadNamePrefix("task-async-");

        // Rejection policy
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // Wait for all tasks to complete before shutdown
        executor.setWaitForTasksToCompleteOnShutdown(true);

        // Wait time for shutdown
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();

        log.info("Default async thread pool initialized, core threads: {}, max threads: {}, queue capacity: {}",
                executor.getCorePoolSize(), executor.getMaxPoolSize(), executor.getThreadPoolExecutor().getQueue().remainingCapacity());

        return executor;
    }
}