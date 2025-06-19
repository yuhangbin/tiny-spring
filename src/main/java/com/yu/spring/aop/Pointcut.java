package com.yu.spring.aop;

/**
 * Core Spring pointcut abstraction.
 * 
 * @author yuhangbin
 * @date 2022/5/3
 **/
public interface Pointcut {

	/**
	 * Return the ClassFilter for this pointcut.
	 * 
	 * @return the ClassFilter
	 */
	ClassFilter getClassFilter();

	/**
	 * Return the MethodMatcher for this pointcut.
	 * 
	 * @return the MethodMatcher
	 */
	MethodMatcher getMethodMatcher();

	/**
	 * Canonical Pointcut instance that always matches.
	 */
	Pointcut TRUE = new Pointcut() {
		@Override
		public ClassFilter getClassFilter() {
			return ClassFilter.TRUE;
		}

		@Override
		public MethodMatcher getMethodMatcher() {
			return MethodMatcher.TRUE;
		}
	};
}
