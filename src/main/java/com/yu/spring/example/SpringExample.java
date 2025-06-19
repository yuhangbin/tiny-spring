package com.yu.spring.example;

import com.yu.spring.aop.*;
import com.yu.spring.beans.BeanDefinition;
import com.yu.spring.beans.BeanPostProcessor;
import com.yu.spring.beans.factory.support.DefaultListableBeanFactory;
import com.yu.spring.context.AnnotationConfigApplicationContext;
import com.yu.spring.context.annotation.Component;

/**
 * Example demonstrating the tiny-spring framework usage.
 * 
 * @author yuhangbin
 * @date 2022/5/3
 **/
public class SpringExample {

    public static void main(String[] args) {
        System.out.println("=== Tiny Spring Framework Example ===\n");

        // Example 1: Basic Bean Factory
        System.out.println("1. Basic Bean Factory Example:");
        basicBeanFactoryExample();

        // Example 2: Bean Post Processor
        System.out.println("\n2. Bean Post Processor Example:");
        beanPostProcessorExample();

        // Example 3: Annotation-based Configuration
        System.out.println("\n3. Annotation-based Configuration Example:");
        annotationConfigExample();

        // Example 4: AOP Proxy Example
        System.out.println("\n4. AOP Proxy Example:");
        aopProxyExample();
    }

    private static void basicBeanFactoryExample() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // Register a bean definition
        BeanDefinition beanDefinition = new BeanDefinition(HelloService.class);
        beanFactory.registerBeanDefinition("helloService", beanDefinition);

        // Get the bean
        HelloService service = (HelloService) beanFactory.getBean("helloService");
        System.out.println("Bean retrieved: " + service.getMessage());

        // Test singleton behavior
        HelloService service2 = (HelloService) beanFactory.getBean("helloService");
        System.out.println("Same instance: " + (service == service2));
    }

    private static void beanPostProcessorExample() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // Add a bean post processor
        beanFactory.addBeanPostProcessor(new LoggingBeanPostProcessor());

        // Register and get bean
        BeanDefinition beanDefinition = new BeanDefinition(HelloService.class);
        beanFactory.registerBeanDefinition("helloService", beanDefinition);

        HelloService service = (HelloService) beanFactory.getBean("helloService");
        System.out.println("Bean with post processor: " + service.getMessage());
    }

    private static void annotationConfigExample() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                AnnotatedHelloService.class);

        AnnotatedHelloService service = context.getBean("annotatedHelloService", AnnotatedHelloService.class);
        System.out.println("Annotated service: " + service.getMessage());
    }

    private static void aopProxyExample() {
        // Create target object
        HelloService target = new HelloService();

        // Create AOP configuration
        AdvisedSupport advised = new AdvisedSupport(target);
        advised.setPointcut(Pointcut.TRUE);

        // Create JDK proxy
        JdkDynamicAopProxy jdkProxy = new JdkDynamicAopProxy(advised);
        Object proxy = jdkProxy.getProxy();

        // Test proxy invocation
        HelloServiceInterface service = (HelloServiceInterface) proxy;
        System.out.println("AOP proxy result: " + service.getMessage());
    }

    // Example classes
    public static class HelloService implements HelloServiceInterface {
        private String message = "Hello from Tiny Spring!";

        @Override
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public interface HelloServiceInterface {
        String getMessage();
    }

    @Component("annotatedHelloService")
    public static class AnnotatedHelloService {
        public String getMessage() {
            return "Hello from annotated service!";
        }
    }

    public static class LoggingBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
            System.out.println("Before initialization: " + beanName);
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
            System.out.println("After initialization: " + beanName);
            if (bean instanceof HelloService) {
                ((HelloService) bean).setMessage("Modified by BeanPostProcessor");
            }
            return bean;
        }
    }
}