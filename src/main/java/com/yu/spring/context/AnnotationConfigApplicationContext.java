package com.yu.spring.context;

import com.yu.spring.beans.BeanDefinition;
import com.yu.spring.beans.factory.support.DefaultListableBeanFactory;
import com.yu.spring.context.annotation.AnnotatedBeanDefinitionReader;

/**
 * Standalone application context that accepts annotated classes as input.
 * 
 * @author yuhangbin
 * @date 2022/5/3
 **/
public class AnnotationConfigApplicationContext extends AbstractApplicationContext {

    private AnnotatedBeanDefinitionReader reader;
    private Class<?>[] annotatedClasses;

    public AnnotationConfigApplicationContext() {
        this.reader = new AnnotatedBeanDefinitionReader(getBeanFactory());
    }

    public AnnotationConfigApplicationContext(Class<?>... annotatedClasses) {
        this();
        register(annotatedClasses);
        refresh();
    }

    /**
     * Register one or more annotated classes to be processed.
     * 
     * @param annotatedClasses one or more annotated classes
     */
    public void register(Class<?>... annotatedClasses) {
        this.annotatedClasses = annotatedClasses;
        for (Class<?> annotatedClass : annotatedClasses) {
            reader.register(annotatedClass);
        }
    }

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        // Bean definitions are loaded through the AnnotatedBeanDefinitionReader
        // when register() is called
    }

    /**
     * Manually register a bean definition.
     * 
     * @param beanName       the name of the bean
     * @param beanDefinition the bean definition
     */
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        getBeanFactory().registerBeanDefinition(beanName, beanDefinition);
    }

    /**
     * Get the underlying bean factory.
     * 
     * @return the bean factory
     */
    public DefaultListableBeanFactory getDefaultListableBeanFactory() {
        return getBeanFactory();
    }
}
