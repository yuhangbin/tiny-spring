package com.yu.spring;

import com.yu.spring.aop.*;
import com.yu.spring.beans.BeanDefinition;
import com.yu.spring.beans.BeanPostProcessor;
import com.yu.spring.beans.factory.support.DefaultListableBeanFactory;
import com.yu.spring.context.AnnotationConfigApplicationContext;
import com.yu.spring.context.annotation.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the tiny-spring framework.
 * 
 * @author yuhangbin
 * @date 2022/5/3
 **/
public class SpringFrameworkTest {

    @Test
    public void testBasicBeanFactory() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // Register a bean definition
        BeanDefinition beanDefinition = new BeanDefinition(TestService.class);
        beanFactory.registerBeanDefinition("testService", beanDefinition);

        // Get the bean
        Object bean = beanFactory.getBean("testService");
        assertNotNull(bean);
        assertTrue(bean instanceof TestService);

        // Test singleton behavior
        Object bean2 = beanFactory.getBean("testService");
        assertSame(bean, bean2);
    }

    @Test
    public void testBeanPostProcessor() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // Add a bean post processor
        beanFactory.addBeanPostProcessor(new TestBeanPostProcessor());

        // Register and get bean
        BeanDefinition beanDefinition = new BeanDefinition(TestService.class);
        beanFactory.registerBeanDefinition("testService", beanDefinition);

        TestService service = (TestService) beanFactory.getBean("testService");
        assertEquals("Processed", service.getMessage());
    }

    @Test
    public void testAnnotationConfigApplicationContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AnnotatedTestService.class);

        AnnotatedTestService service = context.getBean("annotatedTestService", AnnotatedTestService.class);
        assertNotNull(service);
        assertEquals("Hello from annotated service", service.getMessage());
    }

    @Test
    public void testAopProxy() {
        // Create target object
        TestService target = new TestService();

        // Create AOP configuration
        AdvisedSupport advised = new AdvisedSupport(target);
        advised.setPointcut(Pointcut.TRUE);

        // Create JDK proxy
        JdkDynamicAopProxy jdkProxy = new JdkDynamicAopProxy(advised);
        Object proxy = jdkProxy.getProxy();

        assertNotNull(proxy);
        assertTrue(proxy instanceof TestServiceInterface);

        // Test proxy invocation
        TestServiceInterface service = (TestServiceInterface) proxy;
        String result = service.getMessage();
        assertEquals("Hello World", result);
    }

    // Test classes
    public static class TestService implements TestServiceInterface {
        private String message = "Hello World";

        @Override
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public interface TestServiceInterface {
        String getMessage();
    }

    @Component("annotatedTestService")
    public static class AnnotatedTestService {
        public String getMessage() {
            return "Hello from annotated service";
        }
    }

    public static class TestBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
            if (bean instanceof TestService) {
                ((TestService) bean).setMessage("Processed");
            }
            return bean;
        }
    }
}