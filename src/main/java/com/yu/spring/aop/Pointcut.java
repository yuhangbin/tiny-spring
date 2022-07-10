package com.yu.spring.aop;

/**
 * @author yuhangbin
 * @date 2022/5/4
 **/
public interface Pointcut {

	ClassFilter getClassFilter();

	MethodMatcher getMethodMatcher();
}
