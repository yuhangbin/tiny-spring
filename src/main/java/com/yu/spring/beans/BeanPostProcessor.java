package com.yu.spring.beans;

/**
 * See org.springframework.beans.factory.config.BeanPostProcessor
 * Factory hook that allows for custom modification of new bean instance.
 * Such as: wrapping beans with proxies.
 * @author yuhangbin
 * @date 2022/5/4
 **/
public interface BeanPostProcessor {

	Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception;

	Object postProcessAfterInitialization(Object bean, String beanName) throws Exception;

}
