package com.yu.spring.context.annotation;

import com.yu.spring.beans.BeanDefinition;
import com.yu.spring.beans.factory.support.DefaultListableBeanFactory;

/**
 * Reads bean definitions from annotated classes.
 * 
 * @author yuhangbin
 * @date 2022/5/3
 **/
public class AnnotatedBeanDefinitionReader {

    private DefaultListableBeanFactory beanFactory;

    public AnnotatedBeanDefinitionReader(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * Register the given annotated class.
     * 
     * @param annotatedClass the annotated class
     */
    public void register(Class<?> annotatedClass) {
        // Check if class is annotated with @Component
        if (annotatedClass.isAnnotationPresent(Component.class)) {
            String beanName = generateBeanName(annotatedClass);
            BeanDefinition beanDefinition = new BeanDefinition(annotatedClass);
            beanFactory.registerBeanDefinition(beanName, beanDefinition);
        }
    }

    /**
     * Generate a bean name for the given class.
     * 
     * @param clazz the class
     * @return the bean name
     */
    private String generateBeanName(Class<?> clazz) {
        Component component = clazz.getAnnotation(Component.class);
        String value = component.value();

        if (value != null && !value.isEmpty()) {
            return value;
        }

        // Default: class name with first letter lowercase
        String className = clazz.getSimpleName();
        return Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }
}
