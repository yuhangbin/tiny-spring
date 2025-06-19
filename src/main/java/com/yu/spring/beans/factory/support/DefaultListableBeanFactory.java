package com.yu.spring.beans.factory.support;

import com.yu.spring.beans.BeanDefinition;

/**
 * Default implementation of the BeanFactory interface.
 * Provides full bean factory functionality including bean registration and
 * retrieval.
 * 
 * @author yuhangbin
 * @date 2022/5/3
 **/
public class DefaultListableBeanFactory extends AbstractBeanFactory {

    /**
     * Register a bean definition with the given name.
     * 
     * @param beanName       the name of the bean
     * @param beanDefinition the bean definition
     */
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        super.registerBeanDefinition(beanName, beanDefinition);
    }

    /**
     * Check if this factory contains a bean definition for the given name.
     * 
     * @param beanName the name of the bean
     * @return true if bean definition exists
     */
    public boolean containsBeanDefinition(String beanName) {
        return getBeanDefinition(beanName) != null;
    }

    /**
     * Get the number of bean definitions registered in this factory.
     * 
     * @return the count of bean definitions
     */
    public int getBeanDefinitionCount() {
        return getBeanDefinitionNames().length;
    }
}