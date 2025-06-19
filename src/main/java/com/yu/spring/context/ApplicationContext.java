package com.yu.spring.context;

import com.yu.spring.beans.factory.BeanFactory;

/**
 * Central interface to provide configuration for an application.
 * 
 * @author yuhangbin
 * @date 2022/5/3
 **/
public interface ApplicationContext extends BeanFactory {

    /**
     * Return the bean instance that uniquely matches the given object type.
     * 
     * @param requiredType type the bean must match
     * @return an instance of the single matching bean
     */
    <T> T getBean(Class<T> requiredType);

    /**
     * Return the bean instance that uniquely matches the given object type.
     * 
     * @param name         the name of the bean to retrieve
     * @param requiredType type the bean must match
     * @return an instance of the single matching bean
     */
    <T> T getBean(String name, Class<T> requiredType);
}
