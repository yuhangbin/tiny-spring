package com.yu.spring.aop;

/**
 * @author yuhangbin
 * @date 2022/5/4
 **/
public interface AopProxyFactory {

	AopProxy createAopProxy() throws Exception;
}
