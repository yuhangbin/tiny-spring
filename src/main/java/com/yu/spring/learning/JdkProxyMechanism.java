package com.yu.spring.learning;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 深入解析为什么 JDK 动态代理只能代理接口
 * 
 * 关键原因：
 * 1. JDK 动态代理的底层实现机制
 * 2. 继承限制
 * 3. 方法调用机制
 * 4. 字节码生成原理
 * 
 * @author yuhangbin
 */
public class JdkProxyMechanism {

    public static void main(String[] args) {
        System.out.println("=== 为什么 JDK 动态代理只能代理接口 ===\n");

        // 演示1: JDK 动态代理的工作原理
        demonstrateJdkProxyWorking();

        System.out.println("\n============================================================\n");

        // 演示2: 为什么不能代理类
        demonstrateWhyCannotProxyClass();

        System.out.println("\n============================================================\n");

        // 演示3: 代理类的内部结构
        demonstrateProxyClassStructure();

        System.out.println("\n============================================================\n");

        // 演示4: 与 CGLIB 的对比
        demonstrateComparisonWithCglib();
    }

    /**
     * 演示1: JDK 动态代理的工作原理
     */
    private static void demonstrateJdkProxyWorking() {
        System.out.println("演示1: JDK 动态代理的工作原理");
        System.out.println("----------------------------------------");

        // 创建目标对象
        UserService target = new UserServiceImpl();

        System.out.println("\n1. 创建 JDK 动态代理:");
        UserService proxy = (UserService) Proxy.newProxyInstance(
                UserService.class.getClassLoader(),
                new Class<?>[] { UserService.class }, // 注意：这里必须是接口数组
                new CustomInvocationHandler(target));

        System.out.println("    代理对象类型: " + proxy.getClass().getName());
        System.out.println("    代理对象父类: " + proxy.getClass().getSuperclass().getName());
        System.out.println("    代理对象实现的接口: ");
        for (Class<?> iface : proxy.getClass().getInterfaces()) {
            System.out.println("        - " + iface.getName());
        }

        System.out.println("\n2. 调用代理方法:");
        proxy.createUser("张三");

        System.out.println("\n关键点: JDK 动态代理创建的是一个实现了指定接口的新类！");
    }

    /**
     * 演示2: 为什么不能代理类
     */
    private static void demonstrateWhyCannotProxyClass() {
        System.out.println("演示2: 为什么不能代理类");
        System.out.println("----------------------------------------");

        System.out.println("\n1. 尝试代理普通类:");
        OrderService target = new OrderService();

        try {
            // 尝试用类作为代理接口 - 这会失败
            Object failedProxy = Proxy.newProxyInstance(
                    OrderService.class.getClassLoader(),
                    new Class<?>[] { OrderService.class }, // 错误：OrderService 不是接口
                    new CustomInvocationHandler(target));
        } catch (Exception e) {
            System.out.println("    ❌ 失败原因: " + e.getMessage());
        }

        System.out.println("\n2. 为什么不能代理类？");
        System.out.println("    - JDK 动态代理通过实现接口来创建代理类");
        System.out.println("    - 普通类不能被 '实现'，只能被继承");
        System.out.println("    - Java 不支持多重继承，无法同时继承目标类和实现接口");
        System.out.println("    - 代理类需要继承 Proxy 类，无法再继承其他类");

        System.out.println("\n3. 代理类的继承关系:");
        System.out.println("    java.lang.Object");
        System.out.println("    ↓");
        System.out.println("    java.lang.reflect.Proxy  ← 代理类必须继承这个");
        System.out.println("    ↓");
        System.out.println("    $Proxy0 (我们的代理类)");
        System.out.println("    ↓");
        System.out.println("    实现: UserService 接口");
    }

    /**
     * 演示3: 代理类的内部结构
     */
    private static void demonstrateProxyClassStructure() {
        System.out.println("演示3: 代理类的内部结构");
        System.out.println("----------------------------------------");

        UserService target = new UserServiceImpl();
        UserService proxy = (UserService) Proxy.newProxyInstance(
                UserService.class.getClassLoader(),
                new Class<?>[] { UserService.class },
                new CustomInvocationHandler(target));

        System.out.println("\n1. 代理类的详细信息:");
        Class<?> proxyClass = proxy.getClass();

        System.out.println("    类名: " + proxyClass.getName());
        System.out.println("    修饰符: " + java.lang.reflect.Modifier.toString(proxyClass.getModifiers()));
        System.out.println("    父类: " + proxyClass.getSuperclass().getName());

        System.out.println("\n2. 代理类的方法:");
        for (Method method : proxyClass.getMethods()) {
            if (method.getDeclaringClass() != Object.class) {
                System.out.println("    - " + method.getName() + "()");
            }
        }

        System.out.println("\n3. 代理类的字段:");
        for (java.lang.reflect.Field field : proxyClass.getDeclaredFields()) {
            System.out.println("    - " + field.getName() + " : " + field.getType().getName());
        }

        System.out.println("\n关键理解:");
        System.out.println("    JDK 动态代理在运行时动态生成一个类，这个类:");
        System.out.println("    1. 继承 java.lang.reflect.Proxy");
        System.out.println("    2. 实现指定的接口");
        System.out.println("    3. 将方法调用委托给 InvocationHandler");
    }

    /**
     * 演示4: 与 CGLIB 的对比
     */
    private static void demonstrateComparisonWithCglib() {
        System.out.println("演示4: 与 CGLIB 的对比");
        System.out.println("----------------------------------------");

        System.out.println("\nJDK 动态代理 vs CGLIB 代理:");
        System.out.println();
        System.out.println("JDK 动态代理:");
        System.out.println("    ✓ 继承: java.lang.reflect.Proxy");
        System.out.println("    ✓ 实现: 指定的接口");
        System.out.println("    ✓ 限制: 只能代理接口");
        System.out.println("    ✓ 性能: 创建快，调用快");
        System.out.println("    ✓ 依赖: 仅 JDK，无额外依赖");
        System.out.println();
        System.out.println("CGLIB 代理:");
        System.out.println("    ✓ 继承: 目标类");
        System.out.println("    ✓ 实现: 可以代理任何非 final 类");
        System.out.println("    ✓ 限制: 不能代理 final 类");
        System.out.println("    ✓ 性能: 创建慢，调用快");
        System.out.println("    ✓ 依赖: 需要 CGLIB 库");

        System.out.println("\n总结:");
        System.out.println("    JDK 动态代理通过 '实现接口' 来创建代理");
        System.out.println("    CGLIB 通过 '继承目标类' 来创建代理");
        System.out.println("    这就是为什么 JDK 只能代理接口的根本原因！");
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
    }

    // ==================== 自定义 InvocationHandler ====================

    /**
     * 自定义 InvocationHandler - 展示代理方法调用的内部机制
     */
    public static class CustomInvocationHandler implements java.lang.reflect.InvocationHandler {
        private final Object target;

        public CustomInvocationHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("    [CustomInvocationHandler] 拦截方法调用");
            System.out.println("    [CustomInvocationHandler] 代理对象: " + proxy.getClass().getName());
            System.out.println("    [CustomInvocationHandler] 目标对象: " + target.getClass().getName());
            System.out.println("    [CustomInvocationHandler] 调用方法: " + method.getName());

            // 前置处理
            System.out.println("    [CustomInvocationHandler] 前置处理");

            // 调用目标方法
            Object result = method.invoke(target, args);

            // 后置处理
            System.out.println("    [CustomInvocationHandler] 后置处理");
            System.out.println("    [CustomInvocationHandler] 返回结果: " + result);

            return result;
        }
    }
}