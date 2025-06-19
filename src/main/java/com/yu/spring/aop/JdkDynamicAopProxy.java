package com.yu.spring.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK-based AopProxy implementation using dynamic proxies.
 * 
 * @author yuhangbin
 * @date 2022/5/3
 **/
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

	private final Advised advised;

	public JdkDynamicAopProxy(Advised advised) {
		this.advised = advised;
	}

	@Override
	public Object getProxy() {
		return getProxy(advised.getTargetClass().getClassLoader());
	}

	public Object getProxy(ClassLoader classLoader) {
		Class<?>[] interfaces = advised.getTargetClass().getInterfaces();
		if (interfaces.length == 0) {
			throw new RuntimeException("Target class must implement at least one interface for JDK proxy");
		}
		return Proxy.newProxyInstance(classLoader, interfaces, this);
	}

	@Override
	public Class<?> getProxyClass() {
		return getProxy().getClass();
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object target = advised.getTarget();

		// Check if method matches any pointcut
		if (advised.getPointcut() != null &&
				advised.getPointcut().getMethodMatcher().matches(method, target.getClass())) {

			// Apply advice (simplified - would normally handle different advice types)
			System.out.println("Before method: " + method.getName());
			Object result = method.invoke(target, args);
			System.out.println("After method: " + method.getName());
			return result;
		} else {
			// Direct invocation
			return method.invoke(target, args);
		}
	}
}
