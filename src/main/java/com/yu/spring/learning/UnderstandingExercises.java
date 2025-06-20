package com.yu.spring.learning;

import com.yu.spring.aop.*;
import com.yu.spring.beans.BeanDefinition;
import com.yu.spring.beans.BeanPostProcessor;
import com.yu.spring.beans.factory.support.DefaultListableBeanFactory;
import com.yu.spring.context.AnnotationConfigApplicationContext;
import com.yu.spring.context.annotation.Component;

/**
 * Practical exercises to understand tiny-spring concepts.
 * Run each exercise and observe the output to understand how things work.
 * 
 * @author yuhangbin
 * @date 2022/5/3
 **/
public class UnderstandingExercises {

    public static void main(String[] args) {
        System.out.println("=== Tiny Spring Understanding Exercises ===\n");

        // Exercise 1: Basic IoC Container
        System.out.println("Exercise 1: Basic IoC Container");
        exercise1_BasicIoC();
        System.out.println();

        // Exercise 2: Bean Lifecycle
        System.out.println("Exercise 2: Bean Lifecycle");
        exercise2_BeanLifecycle();
        System.out.println();

        // Exercise 3: Bean Post Processor
        System.out.println("Exercise 3: Bean Post Processor");
        exercise3_BeanPostProcessor();
        System.out.println();

        // Exercise 4: Annotation-based Configuration
        System.out.println("Exercise 4: Annotation-based Configuration");
        exercise4_AnnotationConfig();
        System.out.println();

        // Exercise 5: AOP Proxy
        System.out.println("Exercise 5: AOP Proxy");
        exercise5_AopProxy();
        System.out.println();

        // Exercise 6: Singleton vs Prototype
        System.out.println("Exercise 6: Singleton vs Prototype");
        exercise6_BeanScopes();
        System.out.println();
    }

    /**
     * Exercise 1: Understand basic IoC container
     * - How beans are registered and retrieved
     * - How the container manages object creation
     */
    private static void exercise1_BasicIoC() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // Step 1: Register a bean definition
        System.out.println("1. Registering UserService bean...");
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        // Step 2: Get the bean (first time - creates new instance)
        System.out.println("2. Getting UserService bean (first time)...");
        UserService service1 = (UserService) beanFactory.getBean("userService");
        System.out.println("   Service message: " + service1.getMessage());

        // Step 3: Get the bean again (second time - returns same instance)
        System.out.println("3. Getting UserService bean (second time)...");
        UserService service2 = (UserService) beanFactory.getBean("userService");
        System.out.println("   Service message: " + service2.getMessage());

        // Step 4: Verify singleton behavior
        System.out.println("4. Are they the same instance? " + (service1 == service2));
    }

    /**
     * Exercise 2: Understand bean lifecycle
     * - How init methods work
     * - How the container manages bean initialization
     */
    private static void exercise2_BeanLifecycle() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // Create bean definition with init method
        BeanDefinition beanDefinition = new BeanDefinition(LifecycleService.class);
        beanDefinition.setInitMethodName("initialize");
        beanFactory.registerBeanDefinition("lifecycleService", beanDefinition);

        System.out.println("Getting lifecycle service...");
        LifecycleService service = (LifecycleService) beanFactory.getBean("lifecycleService");
        System.out.println("Service state: " + service.getState());
    }

    /**
     * Exercise 3: Understand BeanPostProcessor
     * - How post processors modify beans
     * - When they are called in the lifecycle
     */
    private static void exercise3_BeanPostProcessor() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // Add a post processor
        beanFactory.addBeanPostProcessor(new LoggingBeanPostProcessor());

        // Register and get bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        System.out.println("Getting user service with post processor...");
        UserService service = (UserService) beanFactory.getBean("userService");
        System.out.println("Final message: " + service.getMessage());
    }

    /**
     * Exercise 4: Understand annotation-based configuration
     * - How @Component works
     * - How beans are discovered automatically
     */
    private static void exercise4_AnnotationConfig() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AnnotatedService.class);

        System.out.println("Getting annotated service...");
        AnnotatedService service = context.getBean("annotatedService", AnnotatedService.class);
        System.out.println("Annotated service: " + service.getMessage());
    }

    /**
     * Exercise 5: Understand AOP proxy
     * - How proxies intercept method calls
     * - How pointcuts determine which methods to intercept
     */
    private static void exercise5_AopProxy() {
        // Create target object
        UserService target = new UserService();

        // Configure AOP
        AdvisedSupport advised = new AdvisedSupport(target);
        advised.setPointcut(Pointcut.TRUE); // Match all methods

        // Create JDK proxy
        JdkDynamicAopProxy jdkProxy = new JdkDynamicAopProxy(advised);
        Object proxy = jdkProxy.getProxy();

        System.out.println("Calling method on proxy...");
        if (proxy instanceof UserServiceInterface) {
            UserServiceInterface service = (UserServiceInterface) proxy;
            String result = service.getMessage();
            System.out.println("Proxy result: " + result);
        }
    }

    /**
     * Exercise 6: Understand bean scopes
     * - How singleton and prototype scopes work
     * - When to use each scope
     */
    private static void exercise6_BeanScopes() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // Singleton bean (default)
        BeanDefinition singletonDef = new BeanDefinition(UserService.class);
        singletonDef.setScope("singleton");
        beanFactory.registerBeanDefinition("singletonService", singletonDef);

        // Prototype bean
        BeanDefinition prototypeDef = new BeanDefinition(UserService.class);
        prototypeDef.setScope("prototype");
        beanFactory.registerBeanDefinition("prototypeService", prototypeDef);

        // Test singleton behavior
        UserService singleton1 = (UserService) beanFactory.getBean("singletonService");
        UserService singleton2 = (UserService) beanFactory.getBean("singletonService");
        System.out.println("Singleton beans same instance? " + (singleton1 == singleton2));

        // Test prototype behavior
        UserService prototype1 = (UserService) beanFactory.getBean("prototypeService");
        UserService prototype2 = (UserService) beanFactory.getBean("prototypeService");
        System.out.println("Prototype beans same instance? " + (prototype1 == prototype2));
    }

    // Test classes for exercises

    public static class UserService implements UserServiceInterface {
        private String message = "Hello from UserService";

        @Override
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public interface UserServiceInterface {
        String getMessage();
    }

    public static class LifecycleService {
        private String state = "uninitialized";

        public void initialize() {
            System.out.println("   Initializing LifecycleService...");
            this.state = "initialized";
        }

        public String getState() {
            return state;
        }
    }

    public static class LoggingBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) {
            System.out.println("   Before initialization: " + beanName);
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) {
            System.out.println("   After initialization: " + beanName);
            if (bean instanceof UserService) {
                ((UserService) bean).setMessage("Modified by BeanPostProcessor");
            }
            return bean;
        }
    }

    @Component("annotatedService")
    public static class AnnotatedService {
        public String getMessage() {
            return "Hello from annotated service!";
        }
    }
}