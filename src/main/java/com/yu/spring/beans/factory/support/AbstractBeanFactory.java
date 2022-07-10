package com.yu.spring.beans.factory;

import com.yu.spring.beans.BeanDefinition;
import com.yu.spring.beans.BeanPostProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yuhangbin
 * @date 2022/5/3
 **/
public abstract class AbstractBeanFactory implements BeanFactory {

	private final static Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

	private final static List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();


	@Override public Object getBean(String name) {
		return null;
	}

	@Override public boolean containBean(String name) {
		return false;
	}
}
