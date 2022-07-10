package com.yu.spring.aop;

import java.lang.reflect.Method;

/**
 * @author yuhangbin
 * @date 2022/5/4
 **/
public interface MethodMatcher {

	boolean matches(Method method, Class<?> targetClass);
}
