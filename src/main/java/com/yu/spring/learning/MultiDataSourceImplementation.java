package com.yu.spring.learning;

import com.yu.spring.beans.BeanPostProcessor;
import com.yu.spring.beans.factory.support.DefaultListableBeanFactory;
import com.yu.spring.beans.BeanDefinition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 演示 Spring 在多数据源环境下如何确定使用哪个连接
 * 
 * 关键机制：
 * 1. @Transactional 注解指定数据源
 * 2. 数据源路由策略
 * 3. 线程本地存储 (ThreadLocal)
 * 4. 事务传播机制
 * 
 * @author yuhangbin
 */
public class MultiDataSourceImplementation {

    public static void main(String[] args) {
        System.out.println("=== Spring 多数据源连接路由机制 ===\n");

        // 演示1: 基本的多数据源路由
        demonstrateBasicDataSourceRouting();

        System.out.println("\n======================================================================\n");

        // 演示2: 事务传播和数据源切换
        demonstrateTransactionPropagation();

        System.out.println("\n======================================================================\n");

        // 演示3: 动态数据源切换
        demonstrateDynamicDataSourceSwitching();
    }

    /**
     * 演示1: 基本的多数据源路由
     */
    private static void demonstrateBasicDataSourceRouting() {
        System.out.println("演示1: 基本的多数据源路由");
        System.out.println("----------------------------------------");

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.addBeanPostProcessor(new MultiDataSourceBeanPostProcessor());

        // 注册多数据源服务
        registerBean(beanFactory, "userService", UserService.class);
        registerBean(beanFactory, "orderService", OrderService.class);
        registerBean(beanFactory, "logService", LogService.class);

        System.out.println("\n调用不同数据源的服务:");
        Object userService = beanFactory.getBean("userService");
        Object orderService = beanFactory.getBean("orderService");
        Object logService = beanFactory.getBean("logService");

        System.out.println("\n1. 用户服务 (主库):");
        if (userService instanceof UserService) {
            UserService service = (UserService) userService;
            service.createUser("张三");
        }

        System.out.println("\n2. 订单服务 (订单库):");
        if (orderService instanceof OrderService) {
            OrderService service = (OrderService) orderService;
            service.createOrder("订单001");
        }

        System.out.println("\n3. 日志服务 (日志库):");
        if (logService instanceof LogService) {
            LogService service = (LogService) logService;
            service.logOperation("用户登录");
        }
    }

    /**
     * 演示2: 事务传播和数据源切换
     */
    private static void demonstrateTransactionPropagation() {
        System.out.println("演示2: 事务传播和数据源切换");
        System.out.println("----------------------------------------");

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.addBeanPostProcessor(new MultiDataSourceBeanPostProcessor());

        registerBean(beanFactory, "businessService", BusinessService.class);
        registerBean(beanFactory, "userService", UserService.class);
        registerBean(beanFactory, "orderService", OrderService.class);

        Object businessService = beanFactory.getBean("businessService");

        System.out.println("\n执行业务操作 (涉及多个数据源):");
        System.out.println("BusinessService 类型: " + businessService.getClass().getName());
        System.out.println("BusinessService 是代理: " + businessService.getClass().getName().contains("$Proxy"));

        // 注意：代理对象不能直接转换为原始类型
        // 在实际使用中，Spring 会通过接口或依赖注入来访问代理对象
        System.out.println("\n结论: 多数据源环境下，每个服务都会根据 @Transactional 注解确定使用哪个数据源！");
    }

    /**
     * 演示3: 动态数据源切换
     */
    private static void demonstrateDynamicDataSourceSwitching() {
        System.out.println("演示3: 动态数据源切换");
        System.out.println("----------------------------------------");

        // 模拟动态切换数据源
        DataSourceContextHolder.setDataSource("slave");

        System.out.println("\n当前数据源: " + DataSourceContextHolder.getDataSource());

        // 模拟在不同数据源上执行操作
        executeOnCurrentDataSource("查询操作");

        DataSourceContextHolder.setDataSource("master");
        System.out.println("\n切换到主库");
        System.out.println("当前数据源: " + DataSourceContextHolder.getDataSource());
        executeOnCurrentDataSource("写操作");

        DataSourceContextHolder.clear();
    }

    private static void registerBean(DefaultListableBeanFactory beanFactory, String name, Class<?> clazz) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClass(clazz);
        beanFactory.registerBeanDefinition(name, beanDefinition);
    }

    private static void executeOnCurrentDataSource(String operation) {
        System.out.println("    [数据源: " + DataSourceContextHolder.getDataSource() + "] 执行: " + operation);
    }

    // ==================== 注解定义 ====================

    /**
     * @Transactional 注解 - 支持指定数据源
     */
    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Transactional {
        String value() default "";

        String dataSource() default "master"; // 指定数据源

        boolean readOnly() default false;
    }

    // ==================== 数据源上下文持有者 ====================

    /**
     * 数据源上下文持有者 - 使用 ThreadLocal 存储当前数据源
     * 这是 Spring 多数据源的核心机制
     */
    public static class DataSourceContextHolder {
        private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

        public static void setDataSource(String dataSource) {
            contextHolder.set(dataSource);
        }

        public static String getDataSource() {
            return contextHolder.get();
        }

        public static void clear() {
            contextHolder.remove();
        }
    }

    // ==================== 数据源路由器 ====================

    /**
     * 数据源路由器 - 决定使用哪个数据源
     */
    public static class DataSourceRouter {
        private static final Map<String, String> dataSourceMap = new HashMap<>();

        static {
            dataSourceMap.put("master", "主数据库连接");
            dataSourceMap.put("slave", "从数据库连接");
            dataSourceMap.put("order", "订单数据库连接");
            dataSourceMap.put("log", "日志数据库连接");
        }

        public static String getDataSource(String dataSourceName) {
            return dataSourceMap.getOrDefault(dataSourceName, "默认数据库连接");
        }

        public static String determineDataSource(Method method, Class<?> targetClass) {
            // 1. 优先使用方法上的注解
            if (method.isAnnotationPresent(Transactional.class)) {
                String dataSource = method.getAnnotation(Transactional.class).dataSource();
                if (!"master".equals(dataSource)) {
                    return dataSource;
                }
            }

            // 2. 检查类上的注解
            if (targetClass.isAnnotationPresent(Transactional.class)) {
                String dataSource = targetClass.getAnnotation(Transactional.class).dataSource();
                if (!"master".equals(dataSource)) {
                    return dataSource;
                }
            }

            // 3. 根据方法名或类名推断
            String className = targetClass.getSimpleName().toLowerCase();
            if (className.contains("order")) {
                return "order";
            } else if (className.contains("log")) {
                return "log";
            }

            // 4. 使用当前线程的数据源上下文
            String currentDataSource = DataSourceContextHolder.getDataSource();
            if (currentDataSource != null) {
                return currentDataSource;
            }

            // 5. 默认使用主库
            return "master";
        }
    }

    // ==================== 服务类 ====================

    /**
     * 用户服务 - 使用主库
     */
    public static class UserService {

        @Transactional(dataSource = "master")
        public void createUser(String name) {
            String dataSource = DataSourceRouter.getDataSource("master");
            System.out.println("    [用户服务] 使用数据源: " + dataSource);
            System.out.println("    [用户服务] 创建用户: " + name);
        }

        @Transactional(dataSource = "master", readOnly = true)
        public String getUser(String id) {
            String dataSource = DataSourceRouter.getDataSource("master");
            System.out.println("    [用户服务] 使用数据源: " + dataSource);
            System.out.println("    [用户服务] 查询用户: " + id);
            return "用户" + id;
        }
    }

    /**
     * 订单服务 - 使用订单库
     */
    public static class OrderService {

        @Transactional(dataSource = "order")
        public void createOrder(String orderId) {
            String dataSource = DataSourceRouter.getDataSource("order");
            System.out.println("    [订单服务] 使用数据源: " + dataSource);
            System.out.println("    [订单服务] 创建订单: " + orderId);
        }
    }

    /**
     * 日志服务 - 使用日志库
     */
    public static class LogService {

        @Transactional(dataSource = "log")
        public void logOperation(String operation) {
            String dataSource = DataSourceRouter.getDataSource("log");
            System.out.println("    [日志服务] 使用数据源: " + dataSource);
            System.out.println("    [日志服务] 记录操作: " + operation);
        }
    }

    /**
     * 业务服务 - 涉及多个数据源
     */
    public static class BusinessService {

        private UserService userService;
        private OrderService orderService;

        public BusinessService() {
            this.userService = new UserService();
            this.orderService = new OrderService();
        }

        @Transactional(dataSource = "master")
        public void processBusinessLogic(String businessId) {
            System.out.println("    [业务服务] 开始处理业务: " + businessId);

            // 在主库中创建用户
            userService.createUser("业务用户");

            // 切换到订单库创建订单
            DataSourceContextHolder.setDataSource("order");
            orderService.createOrder("业务订单");
            DataSourceContextHolder.clear();

            System.out.println("    [业务服务] 业务处理完成");
        }
    }

    // ==================== 事务拦截器 ====================

    /**
     * 多数据源事务拦截器
     */
    public static class MultiDataSourceTransactionInterceptor {

        public Object invoke(Object target, Method method, Object[] args) {
            // 确定使用哪个数据源
            String dataSource = DataSourceRouter.determineDataSource(method, target.getClass());

            System.out.println("    [多数据源事务拦截器] 方法: " + method.getName());
            System.out.println("    [多数据源事务拦截器] 确定数据源: " + dataSource);

            // 设置数据源上下文
            DataSourceContextHolder.setDataSource(dataSource);

            try {
                // 开启事务
                beginTransaction(dataSource);

                // 执行业务逻辑
                Object result = method.invoke(target, args);

                // 提交事务
                commitTransaction(dataSource);

                return result;

            } catch (Exception e) {
                // 回滚事务
                rollbackTransaction(dataSource);
                System.out.println("    [多数据源事务拦截器] 事务回滚: " + e.getMessage());
                throw new RuntimeException(e);
            } finally {
                // 清理数据源上下文
                DataSourceContextHolder.clear();
            }
        }

        private void beginTransaction(String dataSource) {
            System.out.println("    [事务管理] 在数据源 [" + dataSource + "] 上开启事务");
        }

        private void commitTransaction(String dataSource) {
            System.out.println("    [事务管理] 在数据源 [" + dataSource + "] 上提交事务");
        }

        private void rollbackTransaction(String dataSource) {
            System.out.println("    [事务管理] 在数据源 [" + dataSource + "] 上回滚事务");
        }
    }

    // ==================== BeanPostProcessor 实现 ====================

    /**
     * 多数据源 BeanPostProcessor
     */
    public static class MultiDataSourceBeanPostProcessor implements BeanPostProcessor {

        private final MultiDataSourceTransactionInterceptor interceptor = new MultiDataSourceTransactionInterceptor();

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
            System.out.println("    [MultiDataSourceBeanPostProcessor] 检查 bean: " + beanName);

            if (needsTransactionalProxy(bean)) {
                System.out.println("    [MultiDataSourceBeanPostProcessor] 为 " + beanName + " 创建多数据源代理");
                return createMultiDataSourceProxy(bean);
            }

            return bean;
        }

        private boolean needsTransactionalProxy(Object bean) {
            Class<?> clazz = bean.getClass();

            if (clazz.isAnnotationPresent(Transactional.class)) {
                return true;
            }

            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(Transactional.class)) {
                    return true;
                }
            }

            return false;
        }

        private Object createMultiDataSourceProxy(Object bean) {
            return java.lang.reflect.Proxy.newProxyInstance(
                    bean.getClass().getClassLoader(),
                    bean.getClass().getInterfaces(),
                    (proxy, method, args) -> {
                        if (method.isAnnotationPresent(Transactional.class)) {
                            return interceptor.invoke(bean, method, args);
                        } else {
                            return method.invoke(bean, args);
                        }
                    });
        }
    }
}