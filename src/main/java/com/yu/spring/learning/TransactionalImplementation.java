package com.yu.spring.learning;

import com.yu.spring.beans.BeanPostProcessor;
import com.yu.spring.beans.factory.support.DefaultListableBeanFactory;
import com.yu.spring.beans.BeanDefinition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 * 演示 Spring 如何实现 @Transactional 代理创建
 * 
 * 关键组件：
 * 1. @Transactional 注解
 * 2. TransactionalBeanPostProcessor - 检测需要事务的 bean
 * 3. TransactionInterceptor - 事务拦截器
 * 4. AOP 代理创建机制
 * 
 * @author yuhangbin
 */
public class TransactionalImplementation {

    public static void main(String[] args) {
        System.out.println("=== Spring @Transactional 实现原理 ===\n");

        // 演示1: 基本的事务代理创建
        demonstrateBasicTransactionalProxy();

        System.out.println("\n============================================================\n");

        // 演示2: 事务拦截器的工作机制
        demonstrateTransactionInterceptor();

        System.out.println("\n============================================================\n");

        // 演示3: 完整的事务管理流程
        demonstrateCompleteTransactionFlow();
    }

    /**
     * 演示1: 基本的事务代理创建
     */
    private static void demonstrateBasicTransactionalProxy() {
        System.out.println("演示1: 基本的事务代理创建");
        System.out.println("----------------------------------------");

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 添加事务 BeanPostProcessor
        beanFactory.addBeanPostProcessor(new TransactionalBeanPostProcessor());

        // 注册带有 @Transactional 的服务
        registerBean(beanFactory, "userService", UserService.class);
        registerBean(beanFactory, "orderService", OrderService.class);

        System.out.println("\n获取服务并检查代理:");
        Object userService = beanFactory.getBean("userService");
        Object orderService = beanFactory.getBean("orderService");

        System.out.println("UserService 类型: " + userService.getClass().getName());
        System.out.println("OrderService 类型: " + orderService.getClass().getName());
        System.out.println("UserService 是代理: " + userService.getClass().getName().contains("$Proxy"));
        System.out.println("OrderService 是代理: " + orderService.getClass().getName().contains("$Proxy"));

        System.out.println("\n调用事务方法:");
        if (userService instanceof UserService) {
            UserService service = (UserService) userService;
            service.createUser("张三");
        }
    }

    /**
     * 演示2: 事务拦截器的工作机制
     */
    private static void demonstrateTransactionInterceptor() {
        System.out.println("演示2: 事务拦截器的工作机制");
        System.out.println("----------------------------------------");

        // 创建事务拦截器
        TransactionInterceptor interceptor = new TransactionInterceptor();

        // 模拟事务方法调用
        System.out.println("\n模拟事务方法调用:");
        interceptor.invoke("createUser", new Object[] { "李四" });
        interceptor.invoke("updateUser", new Object[] { "王五" });
        interceptor.invoke("deleteUser", new Object[] { "赵六" });
    }

    /**
     * 演示3: 完整的事务管理流程
     */
    private static void demonstrateCompleteTransactionFlow() {
        System.out.println("演示3: 完整的事务管理流程");
        System.out.println("----------------------------------------");

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.addBeanPostProcessor(new TransactionalBeanPostProcessor());

        registerBean(beanFactory, "bankService", BankService.class);

        Object bankService = beanFactory.getBean("bankService");

        System.out.println("\n执行银行转账操作:");
        System.out.println("BankService 类型: " + bankService.getClass().getName());
        System.out.println("BankService 是代理: " + bankService.getClass().getName().contains("$Proxy"));

        // 注意：代理对象不能直接转换为原始类型
        // 在实际使用中，Spring 会通过接口或父类来访问代理对象
        System.out.println("\n结论: @Transactional 注解会触发代理创建！");
    }

    private static void registerBean(DefaultListableBeanFactory beanFactory, String name, Class<?> clazz) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClass(clazz);
        beanFactory.registerBeanDefinition(name, beanDefinition);
    }

    // ==================== 注解定义 ====================

    /**
     * @Transactional 注解 - 标记需要事务的方法
     */
    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Transactional {
        String value() default "";

        boolean readOnly() default false;

        int timeout() default -1;
    }

    // ==================== 服务类 ====================

    /**
     * 用户服务 - 有事务方法
     */
    public static class UserService {

        @Transactional
        public void createUser(String name) {
            System.out.println("    [业务逻辑] 创建用户: " + name);
            // 模拟数据库操作
            if ("error".equals(name)) {
                throw new RuntimeException("创建用户失败");
            }
        }

        @Transactional(readOnly = true)
        public String getUser(String id) {
            System.out.println("    [业务逻辑] 查询用户: " + id);
            return "用户" + id;
        }

        public void updateUser(String name) {
            System.out.println("    [业务逻辑] 更新用户: " + name);
        }
    }

    /**
     * 订单服务 - 无事务方法
     */
    public static class OrderService {
        public void createOrder(String orderId) {
            System.out.println("    [业务逻辑] 创建订单: " + orderId);
        }
    }

    /**
     * 银行服务 - 复杂事务场景
     */
    public static class BankService {

        @Transactional
        public void transfer(String fromAccount, String toAccount, double amount) {
            System.out.println("    [业务逻辑] 开始转账");
            System.out.println("    [业务逻辑] 从 " + fromAccount + " 转出 " + amount);
            System.out.println("    [业务逻辑] 转入 " + toAccount);

            // 模拟转账过程中的异常
            if (amount > 5000) {
                throw new RuntimeException("转账金额超过限制");
            }

            System.out.println("    [业务逻辑] 转账完成");
        }
    }

    // ==================== 事务拦截器 ====================

    /**
     * 事务拦截器 - 处理事务逻辑
     */
    public static class TransactionInterceptor {

        public Object invoke(String methodName, Object[] args) {
            System.out.println("    [事务拦截器] 开始处理事务");

            try {
                // 1. 开启事务
                beginTransaction();

                // 2. 执行业务逻辑（这里只是模拟）
                System.out.println("    [事务拦截器] 执行业务方法: " + methodName);

                // 3. 提交事务
                commitTransaction();

                System.out.println("    [事务拦截器] 事务处理完成");
                return null;

            } catch (Exception e) {
                // 4. 回滚事务
                rollbackTransaction();
                System.out.println("    [事务拦截器] 事务回滚: " + e.getMessage());
                throw e;
            }
        }

        private void beginTransaction() {
            System.out.println("    [事务管理] 开启事务");
        }

        private void commitTransaction() {
            System.out.println("    [事务管理] 提交事务");
        }

        private void rollbackTransaction() {
            System.out.println("    [事务管理] 回滚事务");
        }
    }

    // ==================== BeanPostProcessor 实现 ====================

    /**
     * 事务 BeanPostProcessor - Spring 中的核心组件
     * 对应 Spring 的 AbstractAutoProxyCreator
     */
    public static class TransactionalBeanPostProcessor implements BeanPostProcessor {

        private final TransactionInterceptor transactionInterceptor = new TransactionInterceptor();

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
            System.out.println("    [TransactionalBeanPostProcessor] 检查 bean: " + beanName);

            // 检查是否需要事务代理
            if (needsTransactionalProxy(bean)) {
                System.out.println("    [TransactionalBeanPostProcessor] 为 " + beanName + " 创建事务代理");
                return createTransactionalProxy(bean);
            }

            System.out.println("    [TransactionalBeanPostProcessor] " + beanName + " 不需要事务代理");
            return bean;
        }

        /**
         * 检查 bean 是否需要事务代理
         */
        private boolean needsTransactionalProxy(Object bean) {
            Class<?> clazz = bean.getClass();

            // 检查类级别注解
            if (clazz.isAnnotationPresent(Transactional.class)) {
                return true;
            }

            // 检查方法级别注解
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(Transactional.class)) {
                    return true;
                }
            }

            return false;
        }

        /**
         * 创建事务代理 - 简化版本，直接使用 JDK 动态代理
         */
        private Object createTransactionalProxy(Object bean) {
            return java.lang.reflect.Proxy.newProxyInstance(
                    bean.getClass().getClassLoader(),
                    bean.getClass().getInterfaces(),
                    (proxy, method, args) -> {
                        // 检查方法是否有 @Transactional 注解
                        if (method.isAnnotationPresent(Transactional.class)) {
                            System.out.println("    [事务代理] 拦截事务方法: " + method.getName());
                            return transactionInterceptor.invoke(method.getName(), args);
                        } else {
                            // 非事务方法，直接调用
                            return method.invoke(bean, args);
                        }
                    });
        }
    }
}