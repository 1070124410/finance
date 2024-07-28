package com.finance.anubis.core.config;

import com.finance.anubis.core.task.stage.offlineHandler.OffLineActionExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;


@Configuration
public class AsyncThreadPoolConfiguration {

    public final OffLineActionExceptionHandler exceptionHandler;

    public AsyncThreadPoolConfiguration(OffLineActionExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Bean("executor")
    public Executor asyncExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 20,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setUncaughtExceptionHandler(exceptionHandler);
                    return thread;
                },
                new ThreadPoolExecutor.CallerRunsPolicy());

        return executor;
    }

}
