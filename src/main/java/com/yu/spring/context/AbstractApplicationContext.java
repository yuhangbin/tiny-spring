package com.yu.spring.context;

import com.yu.spring.beans.BeanPostProcessor;
import com.yu.spring.beans.factory.support.DefaultListableBeanFactory;
import com.yu.spring.io.DefaultResourceLoader;
import com.yu.spring.io.Resource;
import com.yu.spring.io.ResourceLoader;

/**
 * Abstract implementation of the ApplicationContext interface.
 * 
 * @author yuhangbin
 * @date 2022/5/3
 **/
public abstract class AbstractApplicationContext implements ApplicationContext, ResourceLoader {

    private DefaultListableBeanFactory beanFactory;
    private ResourceLoader resourceLoader;

    public AbstractApplicationContext() {
        this.beanFactory = new DefaultListableBeanFactory();
        this.resourceLoader = new DefaultResourceLoader();
    }

    @Override
    public Object getBean(String name) {
        return beanFactory.getBean(name);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        // Simple implementation - find bean by type
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Object bean = beanFactory.getBean(beanName);
            if (requiredType.isInstance(bean)) {
                return requiredType.cast(bean);
            }
        }
        throw new RuntimeException("No bean found of type: " + requiredType.getName());
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        Object bean = beanFactory.getBean(name);
        if (requiredType.isInstance(bean)) {
            return requiredType.cast(bean);
        }
        throw new RuntimeException("Bean '" + name + "' is not of required type: " + requiredType.getName());
    }

    @Override
    public boolean containBean(String name) {
        return beanFactory.containBean(name);
    }

    @Override
    public Resource getResource(String location) {
        return resourceLoader.getResource(location);
    }

    @Override
    public ClassLoader getClassLoader() {
        return resourceLoader.getClassLoader();
    }

    /**
     * Refresh the application context.
     * This involves loading bean definitions and instantiating singletons.
     */
    public void refresh() {
        // 1. Prepare the bean factory
        prepareBeanFactory();

        // 2. Load bean definitions
        loadBeanDefinitions(beanFactory);

        // 3. Register bean post processors
        registerBeanPostProcessors(beanFactory);

        // 4. Instantiate non-lazy singleton beans
        finishBeanFactoryInitialization(beanFactory);
    }

    /**
     * Prepare the bean factory for use in this context.
     */
    protected void prepareBeanFactory() {
        // Can be overridden by subclasses
    }

    /**
     * Load bean definitions into the given bean factory.
     * 
     * @param beanFactory the bean factory to load definitions into
     */
    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);

    /**
     * Register bean post processors.
     * 
     * @param beanFactory the bean factory
     */
    protected void registerBeanPostProcessors(DefaultListableBeanFactory beanFactory) {
        // Find all BeanPostProcessor beans and register them
        String[] postProcessorNames = beanFactory.getBeanDefinitionNames();
        for (String ppName : postProcessorNames) {
            if (BeanPostProcessor.class.isAssignableFrom(beanFactory.getBeanDefinition(ppName).getBeanClass())) {
                BeanPostProcessor pp = (BeanPostProcessor) beanFactory.getBean(ppName);
                beanFactory.addBeanPostProcessor(pp);
            }
        }
    }

    /**
     * Finish the initialization of this context's bean factory,
     * initializing all remaining singleton beans.
     * 
     * @param beanFactory the bean factory
     */
    protected void finishBeanFactoryInitialization(DefaultListableBeanFactory beanFactory) {
        // Instantiate all remaining (non-lazy-init) singletons
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            if (beanFactory.getBeanDefinition(beanName).isSingleton() &&
                    !beanFactory.getBeanDefinition(beanName).isLazyInit()) {
                beanFactory.getBean(beanName);
            }
        }
    }

    protected DefaultListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
