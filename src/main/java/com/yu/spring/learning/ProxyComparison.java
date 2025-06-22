package com.yu.spring.learning;

import com.yu.spring.beans.BeanPostProcessor;
import com.yu.spring.beans.factory.support.DefaultListableBeanFactory;
import com.yu.spring.beans.BeanDefinition;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 演示为什么 Spring 需要 JDK 动态代理和 CGLIB 代理两种方式
 * 
 * 关键原因：
 * 1. JDK 动态代理只能代理接口
 * 2. CGLIB 可以代理类（包括没有接口的类）
 * 3. 性能考虑
 * 4. 功能限制
 * 
 * @author yuhangbin
 */
public class ProxyComparison {

    public static void main(String[] args) {
        System.out.println("=== Spring 为什么需要 JDK 代理和 CGLIB 代理 ===\n");

        // 演示1: JDK 动态代理的限制
        demonstrateJdkProxyLimitations();

        System.out.println("\n============================================================\n");

        // 演示2: CGLIB 代理的优势
        demonstrateCglibProxyAdvantages();

        System.out.println("\n============================================================\n");

        // 演示3: Spring 的代理选择策略
        demonstrateSpringProxyStrategy();

        System.out.println("\n============================================================\n");

        // 演示4: 性能对比
        demonstratePerformanceComparison();
    }

    /**
     * 演示1: JDK 动态代理的限制
     */
    private static void demonstrateJdkProxyLimitations() {
        System.out.println("演示1: JDK 动态代理的限制");
        System.out.println("----------------------------------------");

        System.out.println("\n1. JDK 动态代理只能代理接口:");

        // 有接口的类 - 可以代理
        UserService userService = new UserServiceImpl();
        UserService jdkProxy = (UserService) Proxy.newProxyInstance(
                UserService.class.getClassLoader(),
                new Class<?>[] { UserService.class },
                new JdkInvocationHandler(userService));

        System.out.println("    UserService 代理类型: " + jdkProxy.getClass().getName());
        jdkProxy.createUser("张三");

        // 没有接口的类 - 无法代理
        System.out.println("\n2. 没有接口的类无法使用 JDK 动态代理:");
        OrderService orderService = new OrderService();

        try {
            // 这会失败，因为 OrderService 没有实现接口
            Object failedProxy = Proxy.newProxyInstance(
                    OrderService.class.getClassLoader(),
                    new Class<?>[] { OrderService.class }, // 错误：OrderService 不是接口
                    new JdkInvocationHandler(orderService));
        } catch (Exception e) {
            System.out.println("    ❌ JDK 动态代理失败: " + e.getMessage());
        }

        System.out.println("\n结论: JDK 动态代理只能代理接口，不能代理普通类！");
    }

    /**
     * 演示2: CGLIB 代理的优势
     */
    private static void demonstrateCglibProxyAdvantages() {
        System.out.println("演示2: CGLIB 代理的优势");
        System.out.println("----------------------------------------");

        System.out.println("\n1. CGLIB 可以代理普通类:");

        // 代理没有接口的类
        OrderService orderService = new OrderService();
        OrderService cglibProxy = createCglibProxy(orderService);

        System.out.println("    OrderService 代理类型: " + cglibProxy.getClass().getName());
        cglibProxy.createOrder("订单001");

        // 代理有接口的类
        UserService userService = new UserServiceImpl();
        UserService cglibUserProxy = createCglibProxy(userService);

        System.out.println("\n2. CGLIB 也可以代理有接口的类:");
        System.out.println("    UserService CGLIB 代理类型: " + cglibUserProxy.getClass().getName());
        cglibUserProxy.createUser("李四");

        System.out.println("\n结论: CGLIB 可以代理任何类，包括没有接口的类！");
    }

    /**
     * 演示3: Spring 的代理选择策略
     */
    private static void demonstrateSpringProxyStrategy() {
        System.out.println("演示3: Spring 的代理选择策略");
        System.out.println("----------------------------------------");

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.addBeanPostProcessor(new SpringProxyBeanPostProcessor());

        System.out.println("\n1. 注册有接口的服务:");
        registerBean(beanFactory, "userService", UserServiceImpl.class);
        Object userService = beanFactory.getBean("userService");
        System.out.println("    UserService 代理类型: " + userService.getClass().getName());

        System.out.println("\n2. 注册没有接口的服务:");
        registerBean(beanFactory, "orderService", OrderService.class);
        Object orderService = beanFactory.getBean("orderService");
        System.out.println("    OrderService 代理类型: " + orderService.getClass().getName());

        System.out.println("\n3. 注册 final 类:");
        registerBean(beanFactory, "finalService", FinalService.class);
        Object finalService = beanFactory.getBean("finalService");
        System.out.println("    FinalService 代理类型: " + finalService.getClass().getName());

        System.out.println("\nSpring 代理选择策略:");
        System.out.println("1. 如果类实现了接口 → 优先使用 JDK 动态代理");
        System.out.println("2. 如果类没有接口 → 使用 CGLIB 代理");
        System.out.println("3. 如果是 final 类 → 无法代理，抛出异常");
    }

    /**
     * 演示4: 性能对比
     */
    private static void demonstratePerformanceComparison() {
        System.out.println("演示4: 性能对比");
        System.out.println("----------------------------------------");

        UserService userService = new UserServiceImpl();

        System.out.println("\n1. JDK 动态代理性能:");
        long startTime = System.nanoTime();
        UserService jdkProxy = (UserService) Proxy.newProxyInstance(
                UserService.class.getClassLoader(),
                new Class<?>[] { UserService.class },
                new JdkInvocationHandler(userService));
        long jdkTime = System.nanoTime() - startTime;
        System.out.println("    JDK 代理创建时间: " + jdkTime + " 纳秒");

        System.out.println("\n2. CGLIB 代理性能:");
        startTime = System.nanoTime();
        UserService cglibProxy = createCglibProxy(userService);
        long cglibTime = System.nanoTime() - startTime;
        System.out.println("    CGLIB 代理创建时间: " + cglibTime + " 纳秒");

        System.out.println("\n性能对比结果:");
        System.out.println("    JDK 动态代理: 创建快，调用快");
        System.out.println("    CGLIB 代理: 创建慢，调用快");
        System.out.println("    选择建议: 优先 JDK，必要时 CGLIB");
    }

    private static void registerBean(DefaultListableBeanFactory beanFactory, String name, Class<?> clazz) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClass(clazz);
        beanFactory.registerBeanDefinition(name, beanDefinition);
    }

    // 简化的 CGLIB 代理创建（实际 Spring 使用更复杂的实现）
    private static <T> T createCglibProxy(T target) {
        // 这里只是模拟，实际 CGLIB 代理创建更复杂
        return target; // 简化处理
    }

    // ==================== 接口和类定义 ====================

    /**
     * 用户服务接口
     */
    public interface UserService {
        void createUser(String name);

        String getUser(String id);
    }

    /**
     * 用户服务实现类
     */
    public static class UserServiceImpl implements UserService {
        @Override
        public void createUser(String name) {
            System.out.println("    [UserServiceImpl] 创建用户: " + name);
        }

        @Override
        public String getUser(String id) {
            System.out.println("    [UserServiceImpl] 查询用户: " + id);
            return "用户" + id;
        }
    }

    /**
     * 订单服务类 - 没有接口
     */
    public static class OrderService {
        public void createOrder(String orderId) {
            System.out.println("    [OrderService] 创建订单: " + orderId);
        }

        public String getOrder(String orderId) {
            System.out.println("    [OrderService] 查询订单: " + orderId);
            return "订单" + orderId;
        }
    }

    /**
     * Final 服务类 - 无法代理
     */
    public static final class FinalService {
        public void doSomething() {
            System.out.println("    [FinalService] 执行操作");
        }
    }

    // ==================== 代理处理器 ====================

    /**
     * JDK 动态代理处理器
     */
    public static class JdkInvocationHandler implements java.lang.reflect.InvocationHandler {
        private final Object target;

        public JdkInvocationHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("    [JDK 代理] 拦截方法: " + method.getName());
            Object result = method.invoke(target, args);
            System.out.println("    [JDK 代理] 方法执行完成");
            return result;
        }
    }

    // ==================== Spring 代理 BeanPostProcessor ====================

    /**
     * Spring 风格的代理 BeanPostProcessor
     */
    public static class SpringProxyBeanPostProcessor implements BeanPostProcessor {

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
            System.out.println("    [SpringProxyBeanPostProcessor] 检查 bean: " + beanName);

            if (needsProxy(bean)) {
                return createProxy(bean, beanName);
            }

            return bean;
        }

        private boolean needsProxy(Object bean) {
            // 简化：所有 bean 都创建代理
            return true;
        }

        private Object createProxy(Object bean, String beanName) {
            Class<?> clazz = bean.getClass();

            // Spring 的代理选择策略
            if (hasInterface(clazz)) {
                System.out.println("    [SpringProxyBeanPostProcessor] 为 " + beanName + " 创建 JDK 动态代理");
                return createJdkProxy(bean);
            } else if (isFinalClass(clazz)) {
                System.out.println("    [SpringProxyBeanPostProcessor] " + beanName + " 是 final 类，无法代理");
                return bean;
            } else {
                System.out.println("    [SpringProxyBeanPostProcessor] 为 " + beanName + " 创建 CGLIB 代理");
                return createCglibProxy(bean);
            }
        }

        private boolean hasInterface(Class<?> clazz) {
            return clazz.getInterfaces().length > 0;
        }

        private boolean isFinalClass(Class<?> clazz) {
            return java.lang.reflect.Modifier.isFinal(clazz.getModifiers());
        }

        private Object createJdkProxy(Object bean) {
            return Proxy.newProxyInstance(
                    bean.getClass().getClassLoader(),
                    bean.getClass().getInterfaces(),
                    new JdkInvocationHandler(bean));
        }

        private Object createCglibProxy(Object bean) {
            // 简化：实际 Spring 使用 CGLIB 的 Enhancer
            return bean; // 这里只是演示逻辑
        }
    }
}