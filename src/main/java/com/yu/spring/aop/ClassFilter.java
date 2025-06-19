package com.yu.spring.aop;

/**
 * Filter that restricts matching of a pointcut or introduction to
 * a given set of target classes.
 * 
 * @author yuhangbin
 * @date 2022/5/3
 **/
public interface ClassFilter {

	/**
	 * Should the pointcut apply to the given interface or target class?
	 * 
	 * @param clazz the candidate target class
	 * @return whether the advice should apply to the given target class
	 */
	boolean matches(Class<?> clazz);

	/**
	 * Canonical instance of a ClassFilter that matches all classes.
	 */
	ClassFilter TRUE = new ClassFilter() {
		@Override
		public boolean matches(Class<?> clazz) {
			return true;
		}

		@Override
		public String toString() {
			return "ClassFilter.TRUE";
		}
	};
}
