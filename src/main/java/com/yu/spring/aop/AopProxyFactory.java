package com.yu.spring.aop;

/**
 * Interface to be implemented by factories that are able to create
 * AOP proxies based on configuration.
 * 
 * @author yuhangbin
 * @date 2022/5/3
 **/
public interface AopProxyFactory {

	/**
	 * Create an AOP proxy for the given configuration.
	 * 
	 * @param config the configuration
	 * @return the AOP proxy
	 */
	AopProxy createAopProxy(Advised config);
}
