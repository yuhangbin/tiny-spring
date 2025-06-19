package com.yu.spring.aop;

import java.lang.reflect.Method;

/**
 * Part of a Pointcut: checks whether the target method is eligible for advice.
 * 
 * @author yuhangbin
 * @date 2022/5/3
 **/
public interface MethodMatcher {

	/**
	 * Perform static checking whether the given method matches.
	 * 
	 * @param method      the candidate method
	 * @param targetClass the target class
	 * @return whether this method matches statically
	 */
	boolean matches(Method method, Class<?> targetClass);

	/**
	 * Canonical instance that matches all methods.
	 */
	MethodMatcher TRUE = new MethodMatcher() {
		@Override
		public boolean matches(Method method, Class<?> targetClass) {
			return true;
		}

		@Override
		public String toString() {
			return "MethodMatcher.TRUE";
		}
	};
}
