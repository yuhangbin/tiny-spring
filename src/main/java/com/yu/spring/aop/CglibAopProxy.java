package com.yu.spring.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLIB-based AopProxy implementation.
 * 
 * @author yuhangbin
 * @date 2022/5/3
 **/
public class CglibAopProxy implements AopProxy, MethodInterceptor {

	private final Advised advised;

	public CglibAopProxy(Advised advised) {
		this.advised = advised;
	}

	@Override
	public Object getProxy() {
		return getProxy(advised.getTargetClass().getClassLoader());
	}

	public Object getProxy(ClassLoader classLoader) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(advised.getTargetClass());
		enhancer.setInterfaces(advised.getTargetClass().getInterfaces());
		enhancer.setCallback(this);
		enhancer.setClassLoader(classLoader);

		return enhancer.create();
	}

	@Override
	public Class<?> getProxyClass() {
		return getProxy().getClass();
	}

	@Override
	public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		Object target = advised.getTarget();

		// Check if method matches any pointcut
		if (advised.getPointcut() != null &&
				advised.getPointcut().getMethodMatcher().matches(method, target.getClass())) {

			// Apply advice (simplified - would normally handle different advice types)
			System.out.println("Before method: " + method.getName());
			Object result = methodProxy.invoke(target, args);
			System.out.println("After method: " + method.getName());
			return result;
		} else {
			// Direct invocation
			return methodProxy.invoke(target, args);
		}
	}
}
