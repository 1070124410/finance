package com.finance.anubis.core.task.runner;

public interface SourceRunner {

    /**
     * 启动资源监听
     */
    public void start();

    /**
     * 终止资源监听
     */
    public void stop();

}
