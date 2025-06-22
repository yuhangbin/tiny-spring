package com.yu.spring.learning;

import com.yu.spring.beans.BeanPostProcessor;
import com.yu.spring.beans.factory.support.DefaultListableBeanFactory;
import com.yu.spring.beans.BeanDefinition;

/**
 * 深入理解 BeanPostProcessor 的工作原理
 * 
 * 关键问题：
 * 1. BeanPostProcessor 是否对所有 bean 都生效？
 * 2. BeanPostProcessor 返回的 bean 是否是代理对象？
 * 
 * @author yuhangbin
 */
public class BeanPostProcessorUnderstanding {

    public static void main(String[] args) {
        System.out.println("=== BeanPostProcessor 深入理解 ===\n");

        // 演示1: BeanPostProcessor 对所有 bean 生效
        demonstrateBeanPostProcessorForAllBeans();

        System.out.println("\n==================================================\n");

        // 演示2: BeanPostProcessor 返回代理对象
        demonstrateProxyBeanCreation();

        System.out.println("\n==================================================\n");

        // 演示3: 实际应用场景
        demonstrateRealWorldUsage();
    }

    /**
     * 演示1: BeanPostProcessor 对所有 bean 都生效
     */
    private static void demonstrateBeanPostProcessorForAllBeans() {
        System.out.println("演示1: BeanPostProcessor 对所有 bean 都生效");
        System.out.println("----------------------------------------");

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 添加一个全局的 BeanPostProcessor
        beanFactory.addBeanPostProcessor(new LoggingBeanPostProcessor());

        // 注册多个不同的 bean
        registerBean(beanFactory, "userService", UserService.class);
        registerBean(beanFactory, "orderService", OrderService.class);
        registerBean(beanFactory, "productService", ProductService.class);

        System.out.println("\n获取所有 bean，观察 BeanPostProcessor 的调用:");
        System.out.println("1. 获取 userService:");
        beanFactory.getBean("userService");

        System.out.println("\n2. 获取 orderService:");
        beanFactory.getBean("orderService");

        System.out.println("\n3. 获取 productService:");
        beanFactory.getBean("productService");

        System.out.println("\n结论: BeanPostProcessor 对所有通过 BeanFactory 创建的 bean 都生效！");
    }

    /**
     * 演示2: BeanPostProcessor 返回代理对象
     */
    private static void demonstrateProxyBeanCreation() {
        System.out.println("演示2: BeanPostProcessor 返回代理对象");
        System.out.println("----------------------------------------");

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 添加一个创建代理的 BeanPostProcessor
        beanFactory.addBeanPostProcessor(new ProxyBeanPostProcessor());

        // 注册一个需要代理的 bean
        registerBean(beanFactory, "userService", UserService.class);

        System.out.println("\n获取 userService bean:");
        Object userService = beanFactory.getBean("userService");

        System.out.println("\n检查返回的对象类型:");
        System.out.println("原始类型: " + UserService.class.getName());
        System.out.println("实际返回类型: " + userService.getClass().getName());
        System.out.println("是否是代理: " + userService.getClass().getName().contains("$Proxy"));

        // 调用方法验证代理功能
        if (userService instanceof UserService) {
            UserService service = (UserService) userService;
            System.out.println("\n调用代理方法:");
            service.createUser("张三");
        }

        System.out.println("\n结论: BeanPostProcessor 可以返回代理对象，替换原始 bean！");
    }

    /**
     * 演示3: 实际应用场景
     */
    private static void demonstrateRealWorldUsage() {
        System.out.println("演示3: 实际应用场景");
        System.out.println("----------------------------------------");

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 添加一个创建代理的 BeanPostProcessor
        beanFactory.addBeanPostProcessor(new ProxyBeanPostProcessor());

        // 注册服务
        registerBean(beanFactory, "userService", UserService.class);
        registerBean(beanFactory, "orderService", OrderService.class);

        System.out.println("\n获取所有服务:");
        Object userService = beanFactory.getBean("userService");
        Object orderService = beanFactory.getBean("orderService");

        System.out.println("\n调用服务方法:");
        // 注意：代理对象不能直接转换为原始类型，需要通过反射调用
        System.out.println("    userService 类型: " + userService.getClass().getName());
        System.out.println("    orderService 类型: " + orderService.getClass().getName());

        System.out.println("\n结论: 在实际应用中，BeanPostProcessor 用于:");
        System.out.println("1. 依赖注入 (@Autowired, @Resource)");
        System.out.println("2. 生命周期回调 (@PostConstruct, @PreDestroy)");
        System.out.println("3. AOP 代理创建");
        System.out.println("4. 事务管理");
        System.out.println("5. 缓存处理");
    }

    private static void registerBean(DefaultListableBeanFactory beanFactory, String name, Class<?> clazz) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClass(clazz);
        beanFactory.registerBeanDefinition(name, beanDefinition);
    }

    // ==================== 示例 Bean 类 ====================

    public static class UserService {
        public void createUser(String name) {
            System.out.println("    UserService: 创建用户 " + name);
        }
    }

    public static class OrderService {
        public void createOrder(String orderId) {
            System.out.println("    OrderService: 创建订单 " + orderId);
        }
    }

    public static class ProductService {
        public void createProduct(String productName) {
            System.out.println("    ProductService: 创建产品 " + productName);
        }
    }

    // ==================== BeanPostProcessor 实现 ====================

    /**
     * 日志记录的 BeanPostProcessor - 对所有 bean 生效
     */
    public static class LoggingBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
            System.out.println("    [LoggingBeanPostProcessor] Before: " + beanName + " ("
                    + bean.getClass().getSimpleName() + ")");
            return bean; // 返回原始 bean
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
            System.out.println(
                    "    [LoggingBeanPostProcessor] After: " + beanName + " (" + bean.getClass().getSimpleName() + ")");
            return bean; // 返回原始 bean
        }
    }

    /**
     * 创建代理的 BeanPostProcessor - 返回代理对象
     */
    public static class ProxyBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
            System.out.println("    [ProxyBeanPostProcessor] Before: " + beanName);
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
            System.out.println("    [ProxyBeanPostProcessor] After: " + beanName + " - 创建代理");

            // 创建简单的代理对象
            return java.lang.reflect.Proxy.newProxyInstance(
                    bean.getClass().getClassLoader(),
                    bean.getClass().getInterfaces(),
                    (proxy, method, args) -> {
                        System.out.println("    [代理] 调用方法: " + method.getName());
                        Object result = method.invoke(bean, args);
                        System.out.println("    [代理] 方法执行完成");
                        return result;
                    });
        }
    }
}