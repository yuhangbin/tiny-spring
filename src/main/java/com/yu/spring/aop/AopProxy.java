package com.yu.spring.aop;

/**
 * @author yuhangbin
 * @date 2022/5/3
 **/
public interface AopProxy {

	Object getProxy();

	Class<?> getProxyClass();
}
