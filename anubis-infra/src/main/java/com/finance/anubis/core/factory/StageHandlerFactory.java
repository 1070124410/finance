package com.finance.anubis.core.factory;

import lombok.CustomLog;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Nonnull;

@CustomLog
public abstract class StageHandlerFactory implements InitializingBean, ApplicationContextAware {

    public ApplicationContext appContext;


    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }
}
