package com.youben.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtils implements ApplicationContextAware, DisposableBean
{
    private static final Logger logger = LoggerFactory.getLogger(SpringUtils.class);
    
    public SpringUtils()
    {
    }
    
    private static ApplicationContext applicationContext = null;

    public  ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }
    
    @Override
    public void destroy() throws Exception
    {
        clearHolder();
    }
    

    public static <T> T getBean(String name)
    {
        return (T) applicationContext.getBean(name);
    }
    

    public static <T> T getBean(Class<T> requiredType)
    {
        return applicationContext.getBean(requiredType);
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext; 
    }

    public  void clearHolder()
    {
         applicationContext = null;
    }
    
}
