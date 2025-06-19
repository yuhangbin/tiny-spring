package com.yu.spring;

import com.yu.spring.beans.BeanDefinition;
import com.yu.spring.beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic test to verify the framework works.
 * 
 * @author yuhangbin
 * @date 2022/5/3
 **/
public class BasicTest {

    @Test
    public void testBasicBeanCreation() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // Register a simple bean
        BeanDefinition beanDefinition = new BeanDefinition(TestBean.class);
        beanFactory.registerBeanDefinition("testBean", beanDefinition);

        // Get the bean
        Object bean = beanFactory.getBean("testBean");
        assertNotNull(bean);
        assertTrue(bean instanceof TestBean);

        // Test singleton behavior
        Object bean2 = beanFactory.getBean("testBean");
        assertSame(bean, bean2);
    }

    @Test
    public void testBeanDefinition() {
        BeanDefinition beanDefinition = new BeanDefinition(TestBean.class);

        assertEquals(TestBean.class, beanDefinition.getBeanClass());
        assertEquals("com.yu.spring.BasicTest$TestBean", beanDefinition.getClassName());
        assertTrue(beanDefinition.isSingleton());
        assertFalse(beanDefinition.isPrototype());
    }

    public static class TestBean {
        private String message = "Hello from TestBean";

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}